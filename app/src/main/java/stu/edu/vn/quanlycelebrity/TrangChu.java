package stu.edu.vn.quanlycelebrity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import stu.edu.vn.quanlycelebrity.adapter.CelebrityAdapter;
import stu.edu.vn.quanlycelebrity.model.Celebrity;
import stu.edu.vn.quanlycelebrity.util.DBConfigUtil;

public class TrangChu extends AppCompatActivity {
    ImageView imgHinh;
    TextView txtMa, txtTen, txtPhanloai;
    ListView lvDsCeleb;
    ArrayList<Celebrity> dsCeleb;
    CelebrityAdapter adapter;
    Toolbar toolbar;
    FloatingActionButton fabThem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_chu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        DBConfigUtil.copyFileFromAssets(TrangChu.this);
        addControls();
        addEvents();
        loadCelebrity();
    }
    private void loadCelebrity() {
        SQLiteDatabase database = openOrCreateDatabase(DBConfigUtil.DB_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("Select * From celebrity", null);
        dsCeleb.clear();
        while (cursor.moveToNext()) {
            int ma = cursor.getInt(0);
            String ten = cursor.getString(1);
            int phanloai = cursor.getInt(2);
            byte[] hinh = cursor.getBlob(3);
            String que = cursor.getString(4);
            int namsinh = cursor.getInt(5);

            dsCeleb.add(new Celebrity(ma, ten, phanloai, hinh, que, namsinh));
        }
        cursor.close();
        database.close();
        adapter.notifyDataSetChanged();
    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imgHinh = findViewById(R.id.imgHinh);
        txtMa = findViewById(R.id.txtMa);
        txtPhanloai = findViewById(R.id.txtPhanloai);
        txtTen = findViewById(R.id.txtTen);
        fabThem = findViewById(R.id.fabThem);
        dsCeleb = new ArrayList<>();
        adapter = new CelebrityAdapter(TrangChu.this, R.layout.item_celeb, dsCeleb);
        lvDsCeleb = findViewById(R.id.lvDsCeleb);
        lvDsCeleb.setAdapter(adapter);
    }

    private void addEvents() {
        fabThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrangChu.this, AddEditCelebrity.class));
            }
        });

        lvDsCeleb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Celebrity celeb = dsCeleb.get(position);
                Bitmap bitmap = celeb.getHinhAsBitmap();
                // Lưu bitmap vào file (để tránh vượt quá kích thước Intent)
                File file = new File(getFilesDir(), "temp.jpg");
                try(FileOutputStream fos = new FileOutputStream(file)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                }catch (Exception e){
                    Log.e(getString(R.string.s_error), e.toString());
                }
                Uri imageUri = Uri.fromFile(file);
                Celebrity dataNoImg = new Celebrity(celeb.getMa(),celeb.getTen(),celeb.getPhanloai(),celeb.getQuequan(),celeb.getNamsinh());
                Intent intent = new Intent(TrangChu.this, AddEditCelebrity.class);
                intent.putExtra("HINH", imageUri);
                intent.putExtra("DATA", dataNoImg);
                startActivity(intent);
            }
        });

        lvDsCeleb.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TrangChu.this);
                builder.setTitle(getString(R.string.s_deletetitle1));
                builder.setMessage(getString(R.string.s_deleteMessage1));
                builder.setPositiveButton(getString(R.string.s_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Celebrity celeb = dsCeleb.get(position);
                        SQLiteDatabase database = openOrCreateDatabase(DBConfigUtil.DB_NAME, MODE_PRIVATE, null);
                        database.delete("celebrity", "ma=?", new String[]{celeb.getMa()+""});
                        database.close();
                        loadCelebrity();
                        Toast.makeText(TrangChu.this, getString(R.string.s_deletesuccess1) +" "+ celeb.getMa(), Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton(getString(R.string.s_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setCancelable(false);
                builder.show();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuAbout) {
            Intent intent = new Intent(TrangChu.this, About.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menuCate) {
            Intent intent = new Intent(TrangChu.this, PhanLoai.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCelebrity();
    }
}