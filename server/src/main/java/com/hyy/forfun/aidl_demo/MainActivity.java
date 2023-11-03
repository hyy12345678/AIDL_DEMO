package com.hyy.forfun.aidl_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//import com.hyy.forfun.aidl_demo.client.BookManagerActivity;

public class MainActivity extends AppCompatActivity {

//    Button btnBookManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        btnBookManager = findViewById(R.id.btnBookManager);
//
//        btnBookManager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, BookManagerActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}