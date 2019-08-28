package com.example.sba_project.Userdata;

/*
 *  전역에서 다뤄야 할 데이터를 들고 있을 것.
 *  init 은 main.
 * */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sba_project.GameRoomPkg.GameRoom;
import com.example.sba_project.GameRoomPkg.GameRoomActivity;
import com.example.sba_project.LoginActivity;
import com.example.sba_project.Main.MainActivity;
import com.example.sba_project.Register.Additional_data;
import com.example.sba_project.Util.UtilValues;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDataManager {
    private static UserDataManager instance;
    private GameRoom gameRoom = null;
    private ChildEventListener inviteListener = null;
    private ChildEventListener myUserDataListener = null;
    private ExtendedMyUserData curUserData = null;
    private boolean isInGameRoom = false;

    private int RoomNumber = -1;

    public static UserDataManager getInstance() {
        if (instance == null) {
            instance = new UserDataManager();
        }
        return instance;
    }

    public void Init(final Context _context) {
        // 디비에서 자신의 정보 읽어오기
        // 더 좋은 방법이 있으면 수정
        UtilValues.setProgressDialogue(_context);

        UtilValues.getUsers().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String curUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                boolean isExist = false;
                for (DataSnapshot iter : dataSnapshot.getChildren()) {
                    final String find_uID = iter.getKey();

                    if (curUID.equals(find_uID)){
                        curUserData = UtilValues.GetUserDataFromDatabase(iter);
                        isExist = true;
                    }
                }

                if(!isExist){
                    Additional_data.ExternUploadDefaulUserData();
                    Intent intent = new Intent(_context, Additional_data.class);
                    intent.putExtra(Additional_data.FROM_KEY, Additional_data.KEY_WHERE.FROM_LOGIN.getValue());
                    ((MainActivity)_context).startActivity(intent);
                    ((MainActivity)_context).finish();
                }else{
                    ((MainActivity)_context).setUserData();

                    myUserDataListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        }
                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            switch (dataSnapshot.getKey()){
                                case "NickName":
                                    curUserData.NickName = dataSnapshot.getValue(String.class);
                                    break;
                                case "Address":
                                    curUserData.Address = dataSnapshot.getValue(String.class);
                                    break;
                                case "Age":
                                    curUserData.Age = dataSnapshot.getValue(Integer.class);
                                    break;
                                case "PhotoUrl":
                                    curUserData.PhotoUrl = dataSnapshot.getValue(String.class);
                                    break;
                            }
                            ((MainActivity)_context).setUserData();
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
                    UtilValues.getUsers().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addChildEventListener(myUserDataListener);
                }

                UtilValues.dismissProgressDialogue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        inviteListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (isInGameRoom) {
                    dataSnapshot.getRef().setValue(null);
                    return;
                }

                // 다이얼로그 팝업 띄워서 확인.
                final String uid = dataSnapshot.child("ClientuID").getValue().toString();
                final String myid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if (!uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    return;

                InviteData inviteData = dataSnapshot.getValue(InviteData.class);
                inviteData.Flag = InviteData.EInviteFlag.RESULT;

                final int RoomNumber = dataSnapshot.child("RoomNumber").getValue(Integer.class);
                UtilValues.CreateSimpleDialogue(_context,
                        "초대를 수락하시겠습니까?", "네", "아니오", "게임 초대",
                        new UtilValues().new moveWithIntFunc(_context, GameRoomActivity.class, GameRoomActivity.ROOM_NUMBER, RoomNumber));

                // 확인했으니 지움.
                dataSnapshot.getRef().setValue(null);
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
        UtilValues.getInviteRef().addChildEventListener(inviteListener);
    }

    public DatabaseReference getCurGameRoomRef() {
        return FirebaseDatabase.getInstance().getReference().child("GameRoom").child(Integer.toString(RoomNumber));
    }

    public void Destroy() {
        UtilValues.getInviteRef().removeEventListener(inviteListener);
        inviteListener = null;
        instance = null;
    }

    public ExtendedMyUserData getCurUserData() {
        return curUserData;
    }

    public GameRoom getGameRoom() {
        return gameRoom;
    }

    public void setInGameRoom(boolean inGameRoom) {
        isInGameRoom = inGameRoom;
    }

    public void setGameRoom(GameRoom _gameRoom) {
        gameRoom = _gameRoom;
        setRoomNumber(gameRoom.getRoom_id());
    }

    public int getRoomNumber() {
        return RoomNumber;
    }

    public void setRoomNumber(int _num) {
        RoomNumber = _num;
    }
}
