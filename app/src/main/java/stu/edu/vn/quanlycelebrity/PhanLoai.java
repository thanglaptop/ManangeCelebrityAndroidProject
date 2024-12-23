package stu.edu.vn.quanlycelebrity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import stu.edu.vn.quanlycelebrity.adapter.TypeAdapter;
import stu.edu.vn.quanlycelebrity.model.Type;
import stu.edu.vn.quanlycelebrity.util.DBConfigUtil;

public class PhanLoai extends AppCompatActivity {
    Toolbar toolbar;
    EditText edtMaloai, edtTenloai;
    Button btnBoChon, btnLuu;
    ListView lvDsPhanLoai;
    TypeAdapter adapter;
    ArrayList<Type> dsPL;
    int ma = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phan_loai);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControls();
        addEvents();
        loadPhanloai();
    }
    private void loadPhanloai(){
        SQLiteDatabase database = openOrCreateDatabase(DBConfigUtil.DB_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("Select * From type", null);
        dsPL.clear();
        while (cursor.moveToNext()){
            int ma = cursor.getInt(0);
            String ten = cursor.getString(1);

            dsPL.add(new Type(ma,ten));
        }
        cursor.close();
        database.close();
        adapter.notifyDataSetChanged();
    }

    private void addControls(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        edtMaloai = findViewById(R.id.edtMaloai);
        edtTenloai =findViewById(R.id.edtTenloai);
        btnBoChon = findViewById(R.id.btnBoChon);
        btnLuu =findViewById(R.id.btnLuu);
        lvDsPhanLoai=findViewById(R.id.lvDsPhanLoai);
        dsPL = new ArrayList<>();
        adapter = new TypeAdapter(PhanLoai.this,R.layout.item_type, dsPL);
        lvDsPhanLoai.setAdapter(adapter);
    }

    private void addEvents(){
        btnBoChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtMaloai.setText("");
                edtTenloai.setText("");
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenpl = edtTenloai.getText().toString().trim();
                SQLiteDatabase database = openOrCreateDatabase(DBConfigUtil.DB_NAME, MODE_PRIVATE, null);
                String query = "SELECT * FROM type WHERE tenloai = ?";
                String[] selectionArgs = new String[]{String.valueOf(tenpl)};
                Cursor cursor = database.rawQuery(query, selectionArgs);
                if (cursor != null && cursor.getCount() > 0) {
                    Toast.makeText(PhanLoai.this, getString(R.string.s_warning4), Toast.LENGTH_LONG).show();
                    return;
                }
                cursor.close();
                database.close();
                if(!tenpl.isEmpty()){
                    if(ma == -1){
                        database = openOrCreateDatabase(DBConfigUtil.DB_NAME, MODE_PRIVATE, null);
                        ContentValues row = new ContentValues();
                        row.put("tenloai", tenpl);
                        long newID = database.insert("type", null, row);
                        database.close();
                        loadPhanloai();
                        Toast.makeText(PhanLoai.this, getString(R.string.s_addsuccess2) +" " + newID, Toast.LENGTH_LONG).show();
                        edtTenloai.setText("");
                        edtMaloai.setText("");
                    }else{
                        database = openOrCreateDatabase(DBConfigUtil.DB_NAME, MODE_PRIVATE, null);
                        ContentValues row = new ContentValues();
                        row.put("tenloai", tenpl);
                        database.update("type", row, "maloai = ?", new String[]{ma + ""});
                        database.close();
                        loadPhanloai();
                        Toast.makeText(PhanLoai.this, getString(R.string.s_updatesuccess2) +" "+ ma, Toast.LENGTH_LONG).show();
                        ma = -1;
                        edtTenloai.setText("");
                        edtMaloai.setText("");
                    }
                }else{
                    edtTenloai.setText("");
                    Toast.makeText(PhanLoai.this, getString(R.string.s_warning2), Toast.LENGTH_LONG).show();
                }

            }
        });
        lvDsPhanLoai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Type pl = dsPL.get(position);
                ma = pl.getMaloai();
                edtTenloai.setText(pl.getTenloai());
                edtMaloai.setText(ma+"");
            }
        });

        lvDsPhanLoai.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PhanLoai.this);
                builder.setTitle(getString(R.string.s_deletetitle2));
                builder.setMessage(getString(R.string.s_deleteMessage2));
                builder.setPositiveButton(getString(R.string.s_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Type pl = dsPL.get(position);
                        SQLiteDatabase database = openOrCreateDatabase(DBConfigUtil.DB_NAME, MODE_PRIVATE, null);
                        String query = "SELECT * FROM celebrity WHERE phanloai = ?";
                        String[] selectionArgs = new String[]{String.valueOf(pl.getMaloai()+"")};
                        Cursor cursor = database.rawQuery(query, selectionArgs);
                        if (cursor != null && cursor.getCount() > 0) {
                            Toast.makeText(PhanLoai.this, getString(R.string.s_warning5), Toast.LENGTH_LONG).show();
                            return;
                        }
                        database.delete("type", "maloai=?", new String[]{pl.getMaloai()+""});
                        database.close();
                        loadPhanloai();
                        Toast.makeText(PhanLoai.this, getString(R.string.s_deletesuccess2) + " " + pl.getMaloai(), Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuMain) {
            Intent intent = new Intent(PhanLoai.this, TrangChu.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.menuAbout){
            Intent intent = new Intent(PhanLoai.this, About.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPhanloai();
    }
}