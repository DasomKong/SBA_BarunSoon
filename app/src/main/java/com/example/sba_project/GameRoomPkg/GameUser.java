package com.example.sba_project.GameRoomPkg;

// 실제로 게임을 플레이하는 유저의 클래스이다.

public class GameUser {
    public String nickName;	// 닉네임
    public float amount_of_activity = 0.f;
    public String Date;
    public int score = 0;
    // 게임에 관련된 변수 설정
    // ...
    //

    public GameUser() { // 아무런 정보가 없는 깡통 유저를 만들 때
    }

    public GameUser(String nickName) {
        this.nickName = nickName;
    }

    public GameUser(String nickName, float amount_of_activity, String date, int score) {
        this.nickName = nickName;
        this.amount_of_activity = amount_of_activity;
        Date = date;
        this.score = score;
    }
}
