package com.example.sba_project;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameRoomActivity extends AppCompatActivity {
    private Spinner gameselc;
    private TextView jicwi;
    private TextView teamone;
    private ImageView pro_Image;
    private Button add_team;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_room);


        final ArrayList arrayList = new ArrayList<>(); // 파이널 달린거 주의
        arrayList.add("철수");
        arrayList.add("영희");
        arrayList.add("람휘");
        arrayList.add("녹지");
        arrayList.add("치치");
        arrayList.add("양가");
        arrayList.add("용병");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList);
        gameselc = (Spinner)findViewById(R.id.gameselect);
        gameselc.setAdapter(arrayAdapter);
        gameselc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),arrayList.get(i)+"가 선택되었습니다.",
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        
    }
}
