package com.example.sba_project.GameRoomPkg;
import com.example.sba_project.GameRoomPkg.GameRoomPopup;
import com.example.sba_project.R;
import com.example.sba_project.Userdata.MyUserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private ImageView user_image;
    private TextView teamone;
    private ImageView master_user_Image;
    private Button add_team;
    private ScrollView teamone_scoll;
    private TextView nickname;
    private ListView user_list;

    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild; //수신대기를 위한 Child


    private ListView listView;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_room);

        SetViews();

        findViewById(R.id.addteam).setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GameRoomActivity.this , GameRoomPopup.class);
                        startActivity(intent);
                    }
                }
        );
    }

    private void SetViews(){
        initDatabase();

        ArrayList arrayList = new ArrayList<>(); // 파이널 달린거 주의
        arrayList.add("철수");
        arrayList.add("영희");
        arrayList.add("람휘");
        arrayList.add("녹지");
        arrayList.add("치치");
        arrayList.add("양가");
        arrayList.add("용병");
        gameselc = (Spinner)findViewById(R.id.gameselect);
        jicwi = (TextView)findViewById(R.id.textView2);
        teamone = (TextView)findViewById(R.id.textView4);
        master_user_Image = (ImageView)findViewById(R.id.imageView2);
        user_image = (ImageView)findViewById(R.id.imageView4);
        user_list = (ListView)findViewById(R.id.listview);
        add_team = (Button)findViewById(R.id.addteam);
        gameselc.setAdapter(arrayAdapter);
        nickname = (TextView)findViewById(R.id.textView3);
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,arrayList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        //Spinner 드랍다운 아니고 다이얼로그 썻음
        user_list.setAdapter(adapter);
        gameselc.setAdapter(adapter);
        mReference = firebaseDatabase.getReference("user");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear(); //스피너(어댑터)에 값을 넣기 전 초기화
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    MyUserData myUserData = messageData.getValue(MyUserData.class);
                    String msg2 = messageData.getValue().toString();
                    Array.add(msg2);
                    adapter.add(msg2);
                    // child 내에 있는 데이터만큼 반복합니다.
                }
                adapter.notifyDataSetChanged();
                gameselc.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        gameselc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(),arrayList.get(i)+"가 선택되었습니다.",
                        //Toast.LENGTH_SHORT).show();
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

    private void initDatabase() {
        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("log");
        mReference.child("log").setValue("check");

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }
}
