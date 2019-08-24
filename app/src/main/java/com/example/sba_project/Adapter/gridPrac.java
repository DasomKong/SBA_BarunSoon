package com.example.sba_project.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sba_project.Game_Description.Gameintroduction;
import com.example.sba_project.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class gridPrac extends AppCompatActivity {

    String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_main);


        final GridView gridView;

        gridView = (GridView)findViewById(R.id.grid_game);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        gridView.setAdapter(imageAdapter);

        // 이벤트 처리를 위한 부분
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), (++position)+"번째 이미지 선택", Toast.LENGTH_SHORT).show();

                switch(position){
                    case 0:
                        title = "CHRONO";
                        System.out.println(position);
                        break;
                    case 1:
                        title = "CONSTELLO";
                        System.out.println(position);
                        break;
                    case 2:
                        title = "CORNER";
                        break;
                    case 3:
                        title = "DANZA";
                        break;
                    case 4:
                        title = "DOJO";
                        break;
                    case 5:
                        title = "DUNK";
                        break;
                    case 6:
                        title = "GAIA";
                        break;
                    case 7:
                        title = "GALACTIC";
                        break;
                    case 8:
                        title = "GERM";
                        break;
                    case 9:
                        title = "JAM";
                        break;
                    case 10:
                        title = "MINEWORLD";
                        break;
                    case 11:
                        title = "NEWTON";
                        break;
                    case 12:
                        title = "PILA";
                        break;
                    case 13:
                        title = "PUZZ";
                        break;
                    case 14:
                        title = "RELE";
                        break;
                    case 15:
                        title = "ROAR";
                        break;
                    case 16:
                        title = "SCALA";
                        break;
                    case 17:
                        title = "SCOREBOARD";
                        break;
                    case 18:
                        title = "SWET";
                        break;
                    case 19:
                        title = "TARGET";
                        break;
                    case 20:
                        title = "TOUCHDOWN";
                        break;
                    case 21:
                        title = "TWINS";
                        break;
                    case 22:
                        title = "VIKA";
                        break;
                    case 23:
                        title = "WAK";
                        break;
                    case 24:
                        title = "ZOO";
                        break;
                    default :
                        System.out.println("ERROR");
                }

                Intent intent = new Intent(gridPrac.this, Gameintroduction.class);
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });


    }
}
