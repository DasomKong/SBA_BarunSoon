package com.example.sba_project.GameRoomPkg;

import com.example.sba_project.Adapter.PlayerItem;
import com.example.sba_project.R;
import com.example.sba_project.Userdata.ExtendedMyUserData;
import com.example.sba_project.Userdata.InviteData;
import com.example.sba_project.Util.UtilValues;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
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
    public enum User_Permission{
        HOST(0), CLIENT(1);

        private final int value;

        private User_Permission(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private Spinner gameselc;
    private TextView jicwi;
    private TextView teamone;
    private ImageView master_pro_Image;
    private Button add_team;
    private ScrollView teamone_scoll;

    private ListView PlayerListView;
    PlayerItem PlayersList = null;

    ArrayAdapter<String> arrayAdapter;

    private GameRoom gameRoom = null;

    // Request Code
    static final int INVITE = 100;

    // Result Code
    static final int INVITE_RESULT_OK = 200;
    static final int INVITE_RESULT_FAIL = 201;
    // Tag
    public static final String USER_DATA = "UserData";
    public static final String ROOM_PERMITION = "Room_Permition";
    public static final String ROOM_NUMBER = "Room_Number";

    // 디폴트는 Client
    User_Permission userPermission = User_Permission.CLIENT;

    // Invite Listen
    ChildEventListener _childeListener = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case INVITE:
                switch (resultCode) {
                    case INVITE_RESULT_OK:
                        ExtendedMyUserData InvitedUser = (ExtendedMyUserData) data.getSerializableExtra(USER_DATA);
                        WaitUserToAccept(InvitedUser);
                        Toast.makeText(GameRoomActivity.this, "초대 성공!", Toast.LENGTH_SHORT).show();
                        break;
                    case INVITE_RESULT_FAIL:
                        Toast.makeText(GameRoomActivity.this, "초대 실패!", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }

    private void WaitUserToAccept(final ExtendedMyUserData InvitedUser){
        // 만약 같은 유저가 방 안에 있으면 예외 처리 추가해야 함.
        // db 로 초대장. uid 와 room_number
        // db 에서 수락 확인.
        // db 방에 방에 입장.
        final InviteData inviteData = new InviteData(gameRoom.getRoom_id(), FirebaseAuth.getInstance().getCurrentUser().getUid(), InvitedUser.uID, InviteData.EInviteFlag.INVITE);

        UtilValues.getInviteRef().push().setValue(inviteData);

        _childeListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // 대기 끝나면 add
                // boolean 확인하고 타깃 불린 바뀌면 ok
                InviteData tmp_data = dataSnapshot.getValue(InviteData.class);

                if(tmp_data.Flag != InviteData.EInviteFlag.RESULT
                && tmp_data.ClientuID != inviteData.ClientuID
                && tmp_data.HostuID != inviteData.HostuID
                && tmp_data.RoomNumber != inviteData.RoomNumber)
                    return;

                PlayersList.addItem(InvitedUser);
                PlayersList.notifyDataSetChanged();

//                UtilValues.getAcceptRef().removeEventListener(_childeListener);
//                _childeListener = null;
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        UtilValues.getAcceptRef().addChildEventListener(_childeListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_room);

        SetViews();

        // 게임 룸 컨트롤.
        SetGameRoom();
    }

    private void SetGameRoom(){
        // 직접 생성했을 경우와 초대받아서 들어온 경우.
        // 권한 확인
        Intent permit = getIntent();

        if((User_Permission)permit.getSerializableExtra(GameRoomActivity.ROOM_PERMITION) != null)
            userPermission = (User_Permission)permit.getSerializableExtra(GameRoomActivity.ROOM_PERMITION);

        int RoomNumber = permit.getIntExtra(ROOM_NUMBER, -1);

        switch (userPermission)
        {
            case HOST:
                gameRoom = new GameRoom();
                break;
            case CLIENT:
                if(RoomNumber == -1)
                    Toast.makeText(GameRoomActivity.this, "잘못된 방 번호 " + RoomNumber + "입니다.", Toast.LENGTH_SHORT).show();
                else
                    gameRoom = new GameRoom(RoomNumber);
                break;
        }
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
        PlayerListView.setAdapter(PlayersList);
        findViewById(R.id.addteam).setOnClickListener(new Button.OnClickListener() {
                                                          @Override
                                                          public void onClick(View view) {
                                                              Intent intent = new Intent(GameRoomActivity.this, GameRoomPopup.class);
                                                              startActivityForResult(intent, INVITE);
                                                          }
                                                      }
        );
    }
}
