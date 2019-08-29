package com.example.sba_project.GameRoomPkg;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sba_project.Adapter.PlayerItem;
import com.example.sba_project.R;
import com.example.sba_project.Userdata.ExtendedMyUserData;
import com.example.sba_project.Userdata.InviteData;
import com.example.sba_project.Userdata.UserDataManager;
import com.example.sba_project.Util.UtilValues;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Game_Room_Frag extends Fragment {
    Button startbutton;

    public Game_Room_Frag() {
        // Required empty public constructor
    }

    public enum User_Permission {
        HOST(0), CLIENT(1);

        private final int value;

        User_Permission(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private Spinner gameselc;
    private TextView host;
    private TextView crew;
    private ImageView master_pro_Image;

    //    private ScrollView teamone_scroll;
    private ListView crewList;
    private Button exit_home;

    private ListView PlayerListView;
    PlayerItem PlayersList = null;


    final ArrayList arrayList = new ArrayList<>(); // 파이널 달린거 주의
    ArrayAdapter<String> arrayAdapter;

    // Request Code
    static final int INVITE = 1000;

    // Result Code
    static final int INVITE_RESULT_OK = 2000;
    static final int INVITE_RESULT_FAIL = 2001;

    // Tag
    public static final String USER_DATA = "UserData";
    public static final String ROOM_PERMITION = "Room_Permition";
    public static final String ROOM_NUMBER = "Room_Number";


    // Host 용 버튼
    private boolean isClicked = false;

    // 디폴트는 Client
    User_Permission userPermission = User_Permission.CLIENT;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_game__room_, container, false);
//        return inflater.inflate(R.layout.fragment_game__room_, container, false);

        host = rootView.findViewById(R.id.nickname2);
        master_pro_Image = rootView.findViewById(R.id.profile_image2);


        //start button
        startbutton = rootView.findViewById(R.id.btn_start);
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClicked)
                    isClicked = false;
                else
                    isClicked = true;

                //view.setClickable(false);
                UserDataManager.getInstance().getCurGameRoomRef().child("GameState").child("isRunning").setValue(isClicked);
            }
        });

        //crew list
        ListView PlayerListView = rootView.findViewById(R.id.crewList);

        PlayersList = new PlayerItem();
        PlayersList.SetActivity(getActivity());
        PlayerListView.setAdapter(PlayersList);

        Button add_team = rootView.findViewById(R.id.addTeam);
        add_team.setOnClickListener(new Button.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(getActivity(), GameRoomPopup.class);
                                            startActivityForResult(intent, INVITE);
                                        }
                                    }
        );

        ///////////////////////////////////////////////////// 수정해야할거
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList) {
            @SuppressLint("WrongViewCast")
            public View getView(int positon, View convertView, ViewGroup parent) {
                View v = super.getView(positon, convertView, parent);
                if (positon == getCount()) {
//                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
//                    ((TextView)v.findViewById(android.R.id.text2)).setHint(getItem(getCount()));
                    ((TextView) v.findViewById(R.id.gameSelect)).setText("");
                    ((TextView) v.findViewById(R.id.gameSelect)).setHint(getItem(getCount()));

                }
                return v;
            }

            public int getCount(int getcount) {
                return super.getCount() - 1;
            }
        };
        final DatabaseReference games_ref = FirebaseDatabase.getInstance().getReference("Game").child("title");
        games_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.add("게임선택");
                for (DataSnapshot iter : dataSnapshot.getChildren()) {
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

        gameselc = rootView.findViewById(R.id.gameSelect);
        gameselc.setPrompt("게임선택");

        gameselc.setAdapter(arrayAdapter);
        gameselc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (arrayList.get(i).equals("게임선택") == true) {
                    //Toast.makeText(getApplicationContext(),"게임선택해주세요!",Toast.LENGTH_SHORT).show();
                    arrayList.remove(0);
                } else{
                    Toast.makeText(getApplicationContext(), arrayList.get(i) + "가 선택되었습니다.",
                            Toast.LENGTH_SHORT).show();

                    if(UserDataManager.getInstance().getGameRoom() != null){
                        UserDataManager.getInstance().getGameRoom().newRoomState.CategoryName = arrayList.get(i).toString();
                    }

                    final DatabaseReference tmpRef = UserDataManager.getInstance().getCurGameRoomRef().child("GameState").child("CategoryName");

                    if (tmpRef != null) {
                        tmpRef.setValue(arrayList.get(i).toString());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        SetGameRoom();


        ////////////////////////////////////////////////////////////////수정해야할거


        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INVITE:
                switch (resultCode) {
                    case INVITE_RESULT_OK:
                        ExtendedMyUserData InvitedUser = (ExtendedMyUserData) data.getSerializableExtra(USER_DATA);
                        InviteMessageToUser(InvitedUser);
                        Toast.makeText(getActivity(), "초대 성공!", Toast.LENGTH_SHORT).show();
                        break;
                    case INVITE_RESULT_FAIL:
                        Toast.makeText(getActivity(), "초대 실패!", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }



    private void InviteMessageToUser(final ExtendedMyUserData InvitedUser) {
        final InviteData inviteData = new InviteData(UserDataManager.getInstance().getGameRoom().getRoom_id(), FirebaseAuth.getInstance().getCurrentUser().getUid(), InvitedUser.uID, InviteData.EInviteFlag.INVITE);
        UtilValues.getInviteRef().push().setValue(inviteData);
    }

    private void SetGameRoom() {
        // 이미 게임 룸에 참여했었고, gameroom이 아직 존재하다면..
        final int _roomNum = UserDataManager.getInstance().getRoomNumber();
        if (_roomNum != -1) {
            // 초기화 후 방 살아있는지 확인.
            FirebaseDatabase.getInstance().getReference().child("GameRoom").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot iter : dataSnapshot.getChildren()){
                        if(iter.getKey().equals(Integer.toString(_roomNum))){
                            UserDataManager.getInstance().setGameRoom(new GameRoom(host, master_pro_Image, getContext(),UserDataManager.getInstance().getCurUserData().NickName, _roomNum, PlayersList));
                            return;
                        }
                    }
                    UserDataManager.getInstance().setRoomNumber(-1);
                    Toast.makeText(getContext(), "이전 방이 없습니다. 새로 만듭니다.", Toast.LENGTH_SHORT).show();
                    SetGameRoomResult();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    int a = 0;
                }
            });
        } else {
            SetGameRoomResult();
        }
    }

    private void SetGameRoomResult() {
        // 권한 확인
        Intent permit = getActivity().getIntent();
        String Image_check = null;

        if (permit.getSerializableExtra(Game_Room_Frag.ROOM_PERMITION) != null)
            userPermission = (User_Permission) permit.getSerializableExtra(Game_Room_Frag.ROOM_PERMITION);

        int RoomNumber = permit.getIntExtra(ROOM_NUMBER, -1);
        PlayersList.setPermission(userPermission);

        // 직접 생성했을 경우와 초대받아서 들어온 경우.

        switch (userPermission) {
            case HOST:
                // 호스트 이미지와 닉네임 받아오기
                Image_check = UserDataManager.getInstance().getCurUserData().PhotoUrl;

                UserDataManager.getInstance().setGameRoom(new GameRoom(host, master_pro_Image, getContext(), UserDataManager.getInstance().getCurUserData().NickName, PlayersList));

//                if (Image_check.equals(null) == true || Image_check.isEmpty()) {
//
//                } else {
//                }
                break;
            case CLIENT:
                if (RoomNumber == -1)
                    Toast.makeText(getActivity(), "잘못된 방 번호 " + RoomNumber + "입니다.", Toast.LENGTH_SHORT).show();
                else{
                    startbutton.setVisibility(View.GONE);
                    UserDataManager.getInstance().setGameRoom(new GameRoom(host, master_pro_Image, getContext(),UserDataManager.getInstance().getCurUserData().NickName, RoomNumber, PlayersList));
                }
                break;
        }
        UserDataManager.getInstance().setInGameRoom(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}


