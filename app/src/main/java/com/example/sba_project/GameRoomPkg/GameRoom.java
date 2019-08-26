package com.example.sba_project.GameRoomPkg;

/*
 * Author : Gompang
 * Desc : 네트워크 게임에서 사용되는(채팅도 포함) 방 개념 클래스
 * Blog : http://gompangs.tistory.com/
 */

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sba_project.Adapter.PlayerItem;
import com.example.sba_project.Userdata.ExtendedMyUserData;
import com.example.sba_project.Util.UtilValues;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GameRoom {
    private int room_id; // 룸 ID
    private List<GameUser> userList;
    private PlayerItem PlayerAdapter_Ref = null; // adapter 제어용 ref
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("GameRoom");
    private ChildEventListener player_listener = null;
    private ChildEventListener room_listener = null;

    // 호스트 방 생성
    public GameRoom(final String _nickname, final PlayerItem _playerItem) {
        // db 에서 반환한 값 확인 후 입력.
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot iter : dataSnapshot.getChildren()) {
                    if (i != Integer.parseInt(iter.getKey()))
                        break;
                    ++i;
                }

                PlayerAdapter_Ref = _playerItem;
                CreateRoom(i);
                CreateUser(_nickname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // 초대 받은 유저 용
    public GameRoom(final String _nickname, int _roomid, PlayerItem _playerItem) {
        PlayerAdapter_Ref = _playerItem;
        CreateRoom(_roomid);
        CreateUser(_nickname);
    }

    // 최종 세팅
    private void CreateRoom(int i) {
        room_id = i;
        userList = new ArrayList();

        final String roomnumberStr = Integer.toString(i);

        GameRoomState newRoomState = new GameRoomState();
        DatabaseReference state_ref = mDatabase.child(roomnumberStr).child("GameState");
        DatabaseReference player_ref = mDatabase.child(roomnumberStr).child("Players");

        state_ref.setValue(newRoomState);

        room_listener = state_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int a = 0;
                boolean isActive = dataSnapshot.getValue(boolean.class);
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
        });
        //  mDatabase.child(roomnumberStr).child("GameState").addChildEventListener(room_listener);

        player_listener = player_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //  자신을 제외한 유저 리스트 여기서 세팅?
                final GameUser newUser = dataSnapshot.getValue(GameUser.class);

                UtilValues.getUsers().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot iter : dataSnapshot.getChildren()) {
                            if (iter != null && iter.child("NickName").getValue().toString().equals(newUser.nickName)) {
                                ExtendedMyUserData extendedMyUserData = UtilValues.GetUserDataFromDatabase(iter);
                                PlayerAdapter_Ref.addItem(extendedMyUserData);
                                PlayerAdapter_Ref.notifyDataSetChanged();
                                userList.add(newUser);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                final GameUser newUser = dataSnapshot.getValue(GameUser.class);

                UtilValues.getUsers().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot iter : dataSnapshot.getChildren()) {
                            if (iter != null && iter.child("NickName").getValue().toString().equals(newUser.nickName)) {
                                ExtendedMyUserData extendedMyUserData = UtilValues.GetUserDataFromDatabase(iter);
                                // remove
                                PlayerAdapter_Ref.removeItem(extendedMyUserData);
                                PlayerAdapter_Ref.notifyDataSetChanged();
                                userList.remove(newUser);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //mDatabase.child(roomnumberStr).child("Players").addChildEventListener(player_listener);
    }

    private GameUser CreateUser(String _nickname) {
        GameUser user = new GameUser(_nickname);
        mDatabase.child(Integer.toString(room_id)).child("Players").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
        return user;
    }

    public void DestroyGameRoom() {
        mDatabase.child(Integer.toString(room_id)).setValue(null);
        mDatabase.child(Integer.toString(getRoom_id())).child("GameState").removeEventListener(room_listener);
        mDatabase.child(Integer.toString(getRoom_id())).child("Players").removeEventListener(player_listener);
        room_listener = null;
        player_listener = null;
    }

    public int getRoom_id() {
        return room_id;
    }

    // DB 업데이트 함수
}