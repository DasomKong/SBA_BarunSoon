package com.example.sba_project.Adapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sba_project.R;

public class gridPrac extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_main);

        GridView gridView;

        gridView = (GridView)findViewById(R.id.grid_game);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        gridView.setAdapter(imageAdapter);

        // 이벤트 처리를 위한 부분
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), (++position)+"번째 이미지 선택", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
