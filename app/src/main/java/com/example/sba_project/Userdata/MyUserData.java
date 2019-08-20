package com.example.sba_project.Userdata;

/*
    파베 데이터베이스 읽고 쓰기용으로 사용될 데이터 클래스
 */

public class MyUserData {
    public String NickName;
    public String Address;
    public String PhotoUrl = "";
    public int Age;

    public MyUserData (){}

    public MyUserData (String nickName, String address, int age) {
        NickName = nickName;
        Address = address;
        Age = age;
    }
}
