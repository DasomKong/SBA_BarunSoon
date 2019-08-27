package com.example.sba_project.GameRoomPkg;

// 실제로 게임을 플레이하는 유저의 클래스이다.

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class GameUser {
    public String nickName = "";	// 닉네임
    public float amount_of_activity = 0.f;
    public String Date = "";
    public int score = 0;
    // 게임에 관련된 변수 설정
    // ...
    //

    public GameUser() { // 아무런 정보가 없는 깡통 유저를 만들 때
    }

    public GameUser(String nickName) {
        this.nickName = nickName;

        long now = System.currentTimeMillis();
        java.util.Date date = new Date(now);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.Date = sdf.format(date);
    }

    public GameUser(String nickName, float amount_of_activity, String date, int score) {
        this.nickName = nickName;
        this.amount_of_activity = amount_of_activity;
        Date = date;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameUser)) return false;
        GameUser gameUser = (GameUser) o;
        return Float.compare(gameUser.amount_of_activity, amount_of_activity) == 0 &&
                score == gameUser.score &&
                Objects.equals(nickName, gameUser.nickName) &&
                Objects.equals(Date, gameUser.Date);
    }
}
