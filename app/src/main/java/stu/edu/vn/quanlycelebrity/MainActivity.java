package stu.edu.vn.quanlycelebrity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import stu.edu.vn.quanlycelebrity.util.DBConfigUtil;

public class MainActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnDangNhap;
    Toolbar toolbar;
    String userStateFile = "UserState";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        DBConfigUtil.copyFileFromAssets(MainActivity.this);

        SharedPreferences preferences = getSharedPreferences("UserState", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        addControls();
        addEvents();
    }
    private void addControls(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnDangNhap = findViewById(R.id.btnDangNhap);
    }

    private void addEvents(){

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                SQLiteDatabase database = openOrCreateDatabase(DBConfigUtil.DB_NAME, MODE_PRIVATE, null);
                Cursor cursor = database.rawQuery("Select * From user where username =? and password =?",new String[]{username,password});
                if(cursor != null && cursor.getCount() > 0){
                    Toast.makeText(MainActivity.this, getString(R.string.s_loginsuccess), Toast.LENGTH_LONG).show();
                    SharedPreferences preferences = getSharedPreferences(userStateFile, MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();
                    startActivity(new Intent(MainActivity.this, PhanLoai.class));
                }else{
                    edtUsername.setText("");
                    edtPassword.setText("");
                    Toast.makeText(MainActivity.this, getString(R.string.s_loginfail), Toast.LENGTH_LONG).show();

                }
                database.close();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuAbout) {
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}