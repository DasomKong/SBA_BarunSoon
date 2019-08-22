package com.example.sba_project;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GameRoomActivity extends AppCompatActivity {
    private Spinner gameselc;
    private TextView jicwi;
    private TextView teamone;
    private ImageView master_pro_Image;
    private Button add_team;
    private ScrollView teamone_scoll;
    ArrayList<String> spinnerAdapter;
    ArrayList<String> arrayList;


    ArrayAdapter<String> arrayAdapter;
    static boolean calledAlready = false;
    private ListView listView;
    List fileList = new ArrayList<>();
    ArrayAdapter adapter;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_room);

        if(!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }
        gameselc.setPrompt("게임선택");
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
        jicwi = (TextView)findViewById(R.id.textView2);
        teamone = (TextView)findViewById(R.id.textView4);
        master_pro_Image = (ImageView)findViewById(R.id.imageView2);
        add_team = (Button)findViewById(R.id.addteam);
        teamone_scoll = (ScrollView)findViewById(R.id.scrollView3);
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

        jicwi.setText("바꾸는 예");
        findViewById(R.id.addteam).setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),GameRoomPopup.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
