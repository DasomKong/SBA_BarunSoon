package com.example.sba_project.Userdata;

public class InviteData{
    public enum EInviteFlag {
        INVITE(0), RESULT(1);
        private final int value;
        EInviteFlag(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    public int RoomNumber;
    public String HostuID;
    public String ClientuID;
    public EInviteFlag Flag;

    public InviteData(){}
    public InviteData(int roomNumber, String hostuID, String clientuID, EInviteFlag flag) {
        RoomNumber = roomNumber;
        HostuID = hostuID;
        ClientuID = clientuID;
        this.Flag = flag;
    }
}