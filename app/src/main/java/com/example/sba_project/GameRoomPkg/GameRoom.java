package com.example.sba_project.GameRoomPkg;

/*
 * Author : Gompang
 * Desc : 네트워크 게임에서 사용되는(채팅도 포함) 방 개념 클래스
 * Blog : http://gompangs.tistory.com/
 */
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    private GameUser roomOwner; // 방장
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("GameRoom");
    private ChildEventListener child_listener = null;

    // 호스트 방 생성
    public GameRoom(){
        CreateRoomID();
    }

    private void CreateRoomID(){
        // db 에서 반환한 값 확인 후 입력.
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot iter : dataSnapshot.getChildren()){
                    if(i != Integer.parseInt(iter.getKey()))
                        break;
                    ++i;
                }
                SetRoomInfo(i);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public GameRoom(int _roomid){
        CreateRoom(_roomid);
    }

    private void CreateRoom(int i){
        child_listener = mDatabase.child(Integer.toString(i)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("GameRoom", "ChildEventListener - onChildAdded : " + dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("GameRoom", "ChildEventListener - onChildChanged : " + s);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d("GameRoom", "ChildEventListener - onChildRemoved : " + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("GameRoom", "ChildEventListener - onChildMoved" + s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("GameRoom", "ChildEventListener - onCancelled" + databaseError.getMessage());
            }
        });
    }

    private void SetRoomInfo(int _roomid){
        room_id = _roomid;
        userList = new ArrayList();

        this.roomOwner = CreateUser(); // 방장을 유저로 만든다.

        CreateRoom(room_id);
    }

    private GameUser CreateUser(){
        GameUser user = new GameUser();
        enterUser(user);
        return user;
    }

    public void enterUser(GameUser user) {
        user.enterRoom(this);
        userList.add(user);

        mDatabase.child(Integer.toString(room_id)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
    }

    public void DestroyGameRoom(){
        mDatabase.child(Integer.toString(room_id)).setValue(null);
        mDatabase.removeEventListener(child_listener);
        child_listener = null;
    }

    // DB 업데이트 함수
    /**
     * 해당 룸의 유저를 다 퇴장시키고 삭제함
     */
    public void close() {
        for (GameUser user : userList) {
            user.exitRoom(this);
        }
        this.userList.clear();
        this.userList = null;
    }

    // 게임 로직
    public void setOwner(GameUser gameUser) {
        this.roomOwner = gameUser; // 특정 사용자를 방장으로 변경한다.
    }

    public GameUser getUserByNickName(String nickName) { // 닉네임을 통해서 방에 속한 유저를 리턴함

        for (GameUser user : userList) {
            if (user.getNickName().equals(nickName)) {
                return user; // 유저를 찾았다면
            }
        }
        return null; // 찾는 유저가 없다면
    }

    public GameUser getUser(GameUser gameUser) { // GameUser 객체로 get

        int idx = userList.indexOf(gameUser);

        // 유저가 존재한다면(gameUser의 equals로 비교)
        if(idx > 0){
            return userList.get(idx);
        }
        else{
            // 유저가 없다면
            return null;
        }
    }

    public int getUserSize() { // 유저의 수를 리턴
        return userList.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameRoom gameRoom = (GameRoom) o;

        return room_id == gameRoom.room_id;
    }

    @Override
    public int hashCode() {
        return room_id;
    }

    public int getRoom_id() {
        return room_id;
    }
}