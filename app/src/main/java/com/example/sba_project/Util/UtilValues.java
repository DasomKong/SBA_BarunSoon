package com.example.sba_project.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.example.sba_project.GameRoomPkg.GameRoomActivity;
import com.example.sba_project.Userdata.ExtendedMyUserData;
import com.example.sba_project.Userdata.InviteData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UtilValues {
/*
* Values
* */
    public static String INVITE = "invite";
    public static String ACCEPT = "accept";

/*
* Function Class
* */
    // 추상화 시킨 인터페이스로 함수 포인터처럼 이용하기 위해 선언함.
    public interface FuncInstance{
        public void Execute();
    }

    public class moveSimpleFunc implements FuncInstance{
        public Context _context;
        public Class _class;

        public moveSimpleFunc (Context _context, Class _class) {
            this._context = _context;
            this._class = _class;
        }
        public void Execute(){
            if(_context == null && _class == null){
                Log.d("UtilValues", "Not expected function excute : " + this.getClass());
                return;
            }
            Intent _intent = new Intent(_context, _class);
            _context.startActivity(_intent);
        }
    }



    // GameRoomActivity 진입용
    public class moveWithIntFunc implements FuncInstance{
        public Context _context;
        public Class _class;
        public String _tag;
        public int _value;
        public InviteData _acceptData;

        public moveWithIntFunc (Context _context, Class _class, String _tag, int _value, InviteData _acceptData) {
            this._context = _context;
            this._class = _class;
            this._tag = _tag;
            this._value = _value;
            this._acceptData = _acceptData;
        }
        public void Execute(){
            if(_context == null && _class == null){
                Log.d("UtilValues", "Not expected function excute : " + this.getClass());
                return;
            }
            // 확인하면 확인한 데이터 다시 돌려줌
            UtilValues.getAcceptRef().push().setValue(_acceptData);

            Intent _intent = new Intent(_context, _class);
            _intent.putExtra(_tag, _value);
            _context.startActivity(_intent);
        }
    }

/*
* DB 함수
* */
    public static ExtendedMyUserData GetUserDataFromDatabase(DataSnapshot _iter){
        ExtendedMyUserData newUser = new ExtendedMyUserData(
                _iter.child("NickName").getValue().toString(),
                _iter.child("Address").getValue().toString(),
                Integer.parseInt(_iter.child("Age").getValue().toString()),
                _iter.child("eMail").getValue().toString(),
                _iter.child("PhotoUrl").getValue().toString(),
                _iter.getKey());

        return newUser;
    }

    public static DatabaseReference getInviteRef(){
        return FirebaseDatabase.getInstance().getReference().child(UtilValues.INVITE);
    }

    public static DatabaseReference getAcceptRef(){
        return FirebaseDatabase.getInstance().getReference().child(UtilValues.ACCEPT);
    }
/*
* 유틸 함수
* */
    public static void CreateSimpleDialogue(Context _context, String _message, String _positive, String _negative, String _title, final FuncInstance _movefunc){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(_context);
        alt_bld.setMessage(_message).setCancelable(
                false).setPositiveButton(_positive,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button
                        _movefunc.Execute();
                    }
                }).setNegativeButton(_negative,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle(_title);
        alert.show();
    }
}
