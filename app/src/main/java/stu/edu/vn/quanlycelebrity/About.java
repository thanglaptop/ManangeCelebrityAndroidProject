package stu.edu.vn.quanlycelebrity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class About extends AppCompatActivity implements OnMapReadyCallback {
    Toolbar toolbar;
    String userStateFile = "UserState";
    Button btnDial, btnCall;
    TextView tvSDT;
    GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControls();
        addEvents();
    }

    private void addControls(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnDial = findViewById(R.id.btnDial);
        btnCall = findViewById(R.id.btnCall);
        tvSDT = findViewById(R.id.tvSDT);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void addEvents(){
        btnDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sdt = tvSDT.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + sdt));
                startActivity(intent);
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCall();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        LatLng stu = new LatLng(10.738034082813927, 106.67782672990728);
        map.addMarker(new MarkerOptions()
                .position(stu)
                .title(getString(R.string.s_markertitle))
                .snippet(getString(R.string.s_snippet))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
        );
        float zoomlevel = 17.0f;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                stu, zoomlevel
        );
        map.moveCamera(cameraUpdate);
    }

    private void doCall() {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.checkSelfPermission(
                        About.this, Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            About.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            100
                    );
                } else {
                    String sdt = tvSDT.getText().toString();
                    Intent intent = new Intent(
                            Intent.ACTION_CALL,
                            Uri.parse("tel:" + sdt)
                    );
                    startActivity(intent);
                }
            } else {
                String sdt = tvSDT.getText().toString();
                Intent intent = new Intent(
                        Intent.ACTION_CALL,
                        Uri.parse("tel:" + sdt)
                );
                startActivity(intent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doCall();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences preferences = getSharedPreferences(userStateFile, MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        if(!isLoggedIn){
            getMenuInflater().inflate(R.menu.menu_about,menu);
        }else{
            getMenuInflater().inflate(R.menu.menu_main,menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuMain) {
            Intent intent = new Intent(About.this, TrangChu.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.menuCate){
            Intent intent = new Intent(About.this, PhanLoai.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.menuLogin){
            startActivity(new Intent(About.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}