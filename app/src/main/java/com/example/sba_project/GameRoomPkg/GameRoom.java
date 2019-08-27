package com.example.sba_project.GameRoomPkg;

/*
 * Author : Gompang
 * Desc : 네트워크 게임에서 사용되는(채팅도 포함) 방 개념 클래스
 * Blog : http://gompangs.tistory.com/
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sba_project.Adapter.PlayerItem;
import com.example.sba_project.Userdata.ExtendedMyUserData;
import com.example.sba_project.Userdata.UserDataManager;
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

        GameRoomState newRoomState = new GameRoomState();

        getcurGameStateRef().setValue(newRoomState);
        room_listener = getcurGameStateRef().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // start button
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

        player_listener = getcurPlayersRef().addChildEventListener(new ChildEventListener() {
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
                                break;
                            }
                        }
                        if(userList.size() <= 0){
                            // 방을 제거
                            DestroyGameRoom();
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
    }

    private GameUser CreateUser(String _nickname) {
        GameUser user = new GameUser(_nickname);
        getcurPlayersRef().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
        return user;
    }

    private DatabaseReference getcurRoomRef(){
        return mDatabase.child(Integer.toString(getRoom_id()));
    }

    private DatabaseReference getcurPlayersRef(){
        return getcurRoomRef().child(("Players"));
    }

    private DatabaseReference getcurGameStateRef(){
        return getcurRoomRef().child(("GameState"));
    }

    public int getRoom_id() {
        return room_id;
    }


    // DB 업데이트 함수
    public void UpdateUserScore(final int score){
        getcurPlayersRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot iter : dataSnapshot.getChildren()){
                    if(iter.child("nickName").getValue().toString().equals(UserDataManager.getInstance().getCurUserData().NickName))
                    {
                        iter.child("score").getRef().setValue(score);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void ExitRoom(){
        UserDataManager.getInstance().setInGameRoom(false);
        getcurPlayersRef().orderByChild(UserDataManager.getInstance().getCurUserData().uID).equalTo(UserDataManager.getInstance().getCurUserData().uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getcurPlayersRef().orderByKey().equalTo(UserDataManager.getInstance().getCurUserData().uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void DestroyGameRoom() {
        getcurRoomRef().setValue(null);
//        getcurGameStateRef().removeEventListener(room_listener);
//        getcurPlayersRef().removeEventListener(player_listener);
//        room_listener = null;
//        player_listener = null;
    }
}