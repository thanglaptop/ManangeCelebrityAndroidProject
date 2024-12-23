package stu.edu.vn.quanlycelebrity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import stu.edu.vn.quanlycelebrity.model.Celebrity;
import stu.edu.vn.quanlycelebrity.model.Type;
import stu.edu.vn.quanlycelebrity.util.DBConfigUtil;

public class AddEditCelebrity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnChonhinh, btnSave, btnExit;
    EditText edtId, edtHoten, edtQue, edtNS;
    Spinner spnPhanloai;
    ImageView imageView;
    ArrayList<Type> dsPL;
    ArrayAdapter<Type> adapter;
    Celebrity celeb = null;
    private static final int PICK_IMAGE_REQUEST = 1;
    int maloai = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_celebrity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControls();
        addEvents();
        loadPhanloai();
        getCeleb();
        checkPermissions();
    }

    private void getCeleb() {
        Intent intent = getIntent();
        if (intent.hasExtra("DATA") && intent.hasExtra("HINH")) {
            celeb = (Celebrity) intent.getSerializableExtra("DATA");
            Uri uriimg = intent.getParcelableExtra("HINH");
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriimg);
                // Hiển thị bitmap trong ImageView
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.e(getString(R.string.s_error), e.toString());
            }
            edtId.setText(celeb.getMa() + "");
            edtHoten.setText(celeb.getTen());
            edtQue.setText(celeb.getQuequan());
            edtNS.setText(celeb.getNamsinh() + "");
            int mapl = celeb.getPhanloai();
            int vitri = -1;
            for (int i = 0; i < dsPL.size(); i++) {
                if (dsPL.get(i).getMaloai() == mapl) {
                    vitri = i;
                    break;
                }
            }
            if (vitri != -1) {
                spnPhanloai.setSelection(vitri);
            }
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp
                Toast.makeText(this, getString(R.string.s_grant), Toast.LENGTH_SHORT).show();
            } else {
                // Quyền bị từ chối
                Toast.makeText(this, getString(R.string.s_deny), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadPhanloai() {
        SQLiteDatabase database = openOrCreateDatabase(DBConfigUtil.DB_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("Select * From type", null);

        while (cursor.moveToNext()) {
            int ma = cursor.getInt(0);
            String ten = cursor.getString(1);

            dsPL.add(new Type(ma, ten));
        }
        cursor.close();
        database.close();
        adapter.notifyDataSetChanged();
    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnChonhinh = findViewById(R.id.btnChonhinh);
        btnSave = findViewById(R.id.btnSave);
        btnExit = findViewById(R.id.btnExit);
        edtHoten = findViewById(R.id.edtHoten);
        edtQue = findViewById(R.id.edtQue);
        edtId = findViewById(R.id.edtId);
        edtNS = findViewById(R.id.edtNS);
        imageView = findViewById(R.id.imageView);
        spnPhanloai = findViewById(R.id.spnPhanloai);
        dsPL = new ArrayList<>();
        adapter = new ArrayAdapter<>(AddEditCelebrity.this, android.R.layout.simple_spinner_item, dsPL);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPhanloai.setAdapter(adapter);
    }

    private void addEvents() {
        btnChonhinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        spnPhanloai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Type typedachon = (Type) adapterView.getItemAtPosition(position);
                maloai = typedachon.getMaloai();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] hinh = getImageDataFromImageView();
                String hoten = edtHoten.getText().toString().trim();
                String que = edtQue.getText().toString().trim();
                int namsinh = 0;
                String namsinhInput = edtNS.getText().toString().trim();
                int pl = maloai;
                if (hinh != null && !hoten.isEmpty() && !que.isEmpty() && !namsinhInput.isEmpty()) {
                    if(namsinhInput.matches("\\d{4}")){
                        namsinh = Integer.parseInt(namsinhInput);
                    }else{
                        Toast.makeText(AddEditCelebrity.this, getString(R.string.s_warning3), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (celeb == null) {
                        SQLiteDatabase database = openOrCreateDatabase(DBConfigUtil.DB_NAME, MODE_PRIVATE, null);
                        ContentValues row = new ContentValues();
                        row.put("ten", hoten);
                        row.put("phanloai", pl);
                        row.put("hinh", hinh);
                        row.put("quequan", que);
                        row.put("namsinh", namsinh);
                        long newID = database.insert("celebrity", null, row);
                        database.close();
                        Toast.makeText(AddEditCelebrity.this, getString(R.string.s_addsuccess1) + " " + newID, Toast.LENGTH_LONG).show();
                    } else {
                        SQLiteDatabase database = openOrCreateDatabase(DBConfigUtil.DB_NAME, MODE_PRIVATE, null);
                        ContentValues row = new ContentValues();
                        row.put("ten", hoten);
                        row.put("phanloai", pl);
                        row.put("hinh", hinh);
                        row.put("quequan", que);
                        row.put("namsinh", namsinh);
                        database.update("celebrity", row, "ma = ?", new String[]{celeb.getMa() + ""});
                        database.close();
                        Toast.makeText(AddEditCelebrity.this, getString(R.string.s_updatesuccess1) + " " + celeb.getMa(), Toast.LENGTH_LONG).show();
                    }
                    finish();
                } else {
                    if(hoten.isEmpty()){
                        edtHoten.setText("");
                    }
                    if(que.isEmpty()){
                        edtQue.setText("");
                    }
                    if(namsinh  == 0){
                        edtNS.setText("");
                    }
                    Toast.makeText(AddEditCelebrity.this, getString(R.string.s_warning), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private byte[] getImageDataFromImageView() {
        // Lấy hình ảnh từ ImageView dưới dạng Drawable
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {

            // Chuyển Drawable thành Bitmap
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            // Chuyển Bitmap thành byte[]
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } else {
            return null; // Nếu không có hình ảnh, trả về null
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuMain) {
            Intent intent = new Intent(AddEditCelebrity.this, TrangChu.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menuAbout) {
            Intent intent = new Intent(AddEditCelebrity.this, About.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(AddEditCelebrity.this, PhanLoai.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            imageView.setImageURI(imageUri);
        }
    }
}