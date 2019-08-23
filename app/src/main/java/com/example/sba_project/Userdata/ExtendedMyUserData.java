package com.example.sba_project.Userdata;

import java.io.Serializable;
   /*
    파베 데이터베이스 읽고 쓰기용으로 사용될 데이터 클래스
    디비에 저장할 때, 충돌나면 Serializable 의심해볼 것.
 */

/*
클라이언트에서 사용할 사용자 데이터.
 */
@SuppressWarnings("serial")
public class ExtendedMyUserData implements Serializable {
    public String NickName;
    public String Address;
    public int Age;
    public String eMail;
    public String PhotoUrl = "";
    public String uID;

    public ExtendedMyUserData(final ExtendedMyUserData rhs) {
        this.NickName = rhs.NickName;
        this.Address = rhs.Address;
        this.Age = rhs.Age;
        this.eMail = rhs.eMail;
        this.uID = rhs.uID;
    }

    public ExtendedMyUserData(String nickName, String address, int age, String eMail, String photoUrl, String uID) {
        NickName = nickName;
        Address = address;
        Age = age;
        this.eMail = eMail;
        PhotoUrl = photoUrl;
        this.uID = uID;
    }
}