package com.example.sba_project.GameRoomPkg;

import com.example.sba_project.Adapter.PlayerItem;
import com.example.sba_project.R;
import com.example.sba_project.Userdata.ExtendedMyUserData;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameRoomActivity extends AppCompatActivity {
    private Spinner gameselc;
    private TextView jicwi;
    private TextView teamone;
    private ImageView master_pro_Image;
    private Button add_team;
    private ScrollView teamone_scoll;

    private ListView PlayerListView;
    PlayerItem PlayersList = null;

    ArrayAdapter<String> arrayAdapter;

    // Request Code
    static final int INVITE = 100;

    // Result Code
    static final int INVITE_RESULT_OK = 200;
    static final int INVITE_RESULT_FAIL = 201;
    // Tag
    static final String USER_DATA = "UserData";

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    //private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case INVITE:
                switch (resultCode) {
                    case INVITE_RESULT_OK:
                        ExtendedMyUserData InvitedUser = (ExtendedMyUserData) data.getSerializableExtra(USER_DATA);
                        // 만약 같은 유저가 방 안에 있으면 예외 처리 추가해야 함.
                        PlayersList.addItem(InvitedUser);
                        PlayerListView.setAdapter(PlayersList);
                        Toast.makeText(GameRoomActivity.this, "초대 성공!", Toast.LENGTH_SHORT).show();
                        break;
                    case INVITE_RESULT_FAIL:
                        Toast.makeText(GameRoomActivity.this, "초대 실패!", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_room);

        SetViews();

        findViewById(R.id.addteam).setOnClickListener(new Button.OnClickListener() {
                                                          @Override
                                                          public void onClick(View view) {
                                                              Intent intent = new Intent(GameRoomActivity.this, GameRoomPopup.class);
                                                              startActivityForResult(intent, INVITE);
                                                          }
                                                      }
        );
    }

    private void SetViews() {
        final ArrayList arrayList = new ArrayList<>(); // 파이널 달린거 주의
        arrayList.add("철수");
        arrayList.add("영희");
        arrayList.add("람휘");
        arrayList.add("녹지");
        arrayList.add("치치");
        arrayList.add("양가");
        arrayList.add("용병");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        gameselc = (Spinner) findViewById(R.id.gameselect);
        jicwi = (TextView) findViewById(R.id.textView2);
        teamone = (TextView) findViewById(R.id.textView4);
        master_pro_Image = (ImageView) findViewById(R.id.imageView2);
        add_team = (Button) findViewById(R.id.addteam);
        teamone_scoll = (ScrollView) findViewById(R.id.scrollView);
        gameselc.setAdapter(arrayAdapter);
        jicwi.setText("바꾸는 예");
        gameselc.setPrompt("hi");

        gameselc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), arrayList.get(i) + "가 선택되었습니다.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        PlayerListView = findViewById(R.id.listview);

        PlayersList = new PlayerItem();
        PlayersList.SetActivity(this);
    }
}
