package com.mingroup.onthuchanhsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnQLPB, btnQLNV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnQLPB = findViewById(R.id.btnQLPB);
        btnQLNV = findViewById(R.id.btnQLNV);

        btnQLPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), QLPhongBanActivity.class));
            }
        });

        btnQLNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), QLNhanVienActivity.class));
            }
        });
    }
}