package com.example.sba_project.GameRoomPkg;

import com.example.sba_project.Adapter.PlayerItem;
import com.example.sba_project.Main.MainActivity;
import com.example.sba_project.R;
import com.example.sba_project.Userdata.ExtendedMyUserData;
import com.example.sba_project.Userdata.UserDataManager;
import com.example.sba_project.Util.BackPressCloseHandler;
import com.example.sba_project.Util.UtilValues;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.example.sba_project.Userdata.InviteData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
    final ArrayList arrayList = new ArrayList<>(); // 파이널 달린거 주의
    ArrayAdapter<String> arrayAdapter;

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

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case INVITE:
                switch (resultCode) {
                    case INVITE_RESULT_OK:
                        ExtendedMyUserData InvitedUser = (ExtendedMyUserData) data.getSerializableExtra(USER_DATA);
                        InviteMessageToUser(InvitedUser);
                        Toast.makeText(GameRoomActivity.this, "초대 성공!", Toast.LENGTH_SHORT).show();
                        break;
                    case INVITE_RESULT_FAIL:
                        Toast.makeText(GameRoomActivity.this, "초대 실패!", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }

    private void InviteMessageToUser(final ExtendedMyUserData InvitedUser){
        final InviteData inviteData = new InviteData(UserDataManager.getInstance().getGameRoom().getRoom_id(), FirebaseAuth.getInstance().getCurrentUser().getUid(), InvitedUser.uID, InviteData.EInviteFlag.INVITE);
        UtilValues.getInviteRef().push().setValue(inviteData);
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
        // 이미 게임 룸에 참여했었고, gameroom이 아직 존재하다면..
        final int _roomNum = UserDataManager.getInstance().getRoomNumber();
        if(_roomNum != -1){
            // 초기화 후 방 살아있는지 확인.
            UserDataManager.getInstance().setRoomNumber(-1);
            FirebaseDatabase.getInstance().getReference().child("GameRoom").equalTo(_roomNum).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserDataManager.getInstance().setGameRoom(new GameRoom(UserDataManager.getInstance().getCurUserData().NickName, _roomNum, PlayersList));
                        UserDataManager.getInstance().setInGameRoom(true);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    int a = 0;
                }
            });
        }else{
            SetGameRoomResult();
        }
    }

    private void SetGameRoomResult(){
        // 권한 확인
        Intent permit = getIntent();

        if((User_Permission)permit.getSerializableExtra(GameRoomActivity.ROOM_PERMITION) != null)
            userPermission = (User_Permission)permit.getSerializableExtra(GameRoomActivity.ROOM_PERMITION);

        int RoomNumber = permit.getIntExtra(ROOM_NUMBER, -1);
        PlayersList.setPermission(userPermission);

        // 직접 생성했을 경우와 초대받아서 들어온 경우.
        switch (userPermission)
        {
            case HOST:
                UserDataManager.getInstance().setGameRoom(new GameRoom(UserDataManager.getInstance().getCurUserData().NickName, PlayersList));
                break;
            case CLIENT:
                if(RoomNumber == -1)
                    Toast.makeText(GameRoomActivity.this, "잘못된 방 번호 " + RoomNumber + "입니다.", Toast.LENGTH_SHORT).show();
                else
                    UserDataManager.getInstance().setGameRoom(new GameRoom(UserDataManager.getInstance().getCurUserData().NickName, RoomNumber, PlayersList));
                break;
        }
        UserDataManager.getInstance().setInGameRoom(true);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserDataManager.getInstance().setInGameRoom(false);
    }

    private void SetViews() {
        backPressCloseHandler = new BackPressCloseHandler(this);

        gameselc = (Spinner) findViewById(R.id.gameselect);
        jicwi = (TextView) findViewById(R.id.textView2);
        teamone = (TextView) findViewById(R.id.textView4);
        master_pro_Image = (ImageView) findViewById(R.id.imageView2);
        add_team = (Button) findViewById(R.id.addteam);
        teamone_scoll = (ScrollView) findViewById(R.id.scrollView);

        // start button
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDataManager.getInstance().getCurGameRoomRef().child("CategoryName").setValue(true);
            }
        });

        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList){
            @SuppressLint("WrongViewCast")
            public View getView(int positon, View convertView, ViewGroup parent){
                View v = super.getView(positon,convertView,parent);
                if(positon==getCount()){
//                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
//                    ((TextView)v.findViewById(android.R.id.text2)).setHint(getItem(getCount()));
                    ((TextView)v.findViewById(R.id.gameselect)).setText("");
                    ((TextView)v.findViewById(R.id.gameselect)).setHint(getItem(getCount()));

                }
                return  v;
            }
            public int getCount(int getcount){
                return super.getCount() -1;
            }
        };
        final DatabaseReference games_ref = FirebaseDatabase.getInstance().getReference("Game").child("title");
        games_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.add("게임선택");
                for(DataSnapshot iter : dataSnapshot.getChildren()){
                    /*NickName, Address, Age, eMail, PhotoUrl */
                    //ExtendedMyUserData tmpUser = UtilValues.GetUserDataFromDatabase(iter);
                    arrayList.add(iter.getKey());
                }
//                arrayList.add("게임선택");
                arrayAdapter.notifyDataSetChanged();
//                gameselc.setSelection(arrayAdapter.getCount()-1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        gameselc.setPrompt("게임선택");
        gameselc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final DatabaseReference tmpRef = UserDataManager.getInstance().getCurGameRoomRef();

                if(tmpRef != null){
                    tmpRef.child("CategoryName").setValue(arrayList.get(i).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        gameselc.setAdapter(arrayAdapter);
        gameselc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(arrayList.get(i).equals("게임선택")==true){
                    //Toast.makeText(getApplicationContext(),"게임선택해주세요!",Toast.LENGTH_SHORT).show();
                    arrayList.remove(0);
                }
                else
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
