package com.example.sba_project.GameRoomPkg;

public class GameRoomState {
    public String CategoryName = "";
    public boolean isRunning = false;

    public GameRoomState() {
    }

    public GameRoomState(String categoryName, boolean isRunning) {
        CategoryName = categoryName;
        this.isRunning = isRunning;
    }
}
