package com.example.sba_project.Util;

import com.example.sba_project.Userdata.ExtendedMyUserData;
import com.google.firebase.database.DataSnapshot;

public class UtilValues {

    enum User_Permission{
        HOST(0), CLIENT(1);

        private final int value;

        private User_Permission(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static ExtendedMyUserData GetUserDataFromDatabase(DataSnapshot _iter){
        ExtendedMyUserData newUser = new ExtendedMyUserData(
                _iter.child("NickName").getValue().toString(),
                _iter.child("Address").getValue().toString(),
                Integer.parseInt(_iter.child("Age").getValue().toString()),
                _iter.child("eMail").getValue().toString(),
                _iter.child("PhotoUrl").getValue().toString(),
                _iter.getValue().toString());

        return newUser;
    }
}
