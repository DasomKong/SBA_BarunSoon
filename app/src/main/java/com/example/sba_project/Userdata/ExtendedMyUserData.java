package com.example.sba_project.Userdata;

import java.io.Serializable;
import java.util.Objects;
   /*
    파베 데이터베이스 읽고 쓰기용으로 사용될 데이터 클래스
    디비에 저장할 때, 충돌나면 Serializable 의심해볼 것.
 */

/*
클라이언트에서 사용할 사용자 데이터.
 */
@SuppressWarnings("serial")
public class ExtendedMyUserData implements Serializable {
    public String NickName = "";
    public String Address = "";
    public int Age = 0;
    public String eMail = "";
    public String PhotoUrl = "";
    public String uID = "";

    public ExtendedMyUserData(){
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExtendedMyUserData)) return false;
        ExtendedMyUserData that = (ExtendedMyUserData) o;
        return Age == that.Age &&
                Objects.equals(NickName, that.NickName) &&
                Objects.equals(Address, that.Address) &&
                Objects.equals(eMail, that.eMail) &&
                Objects.equals(PhotoUrl, that.PhotoUrl) &&
                Objects.equals(uID, that.uID);
    }
}