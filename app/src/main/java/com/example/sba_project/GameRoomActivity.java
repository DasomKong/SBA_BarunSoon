package com.example.sba_project;
import com.example.sba_project.GameRoomPkg.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GameRoomActivity extends AppCompatActivity {
    private Button makeroombt;
    private Button invitebt;
    private EditText usersr;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_room);

        makeroombt = (Button) findViewById(R.id.makeroom);
        invitebt = (Button) findViewById(R.id.invitegamer);
        usersr = (EditText) findViewById(R.id.editText);

//        GameRoom GR = new GameRoom();

        invitebt.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                databaseReference.child("").push().setValue("");
            }
        });

        makeroombt.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                databaseReference.child("").push().setValue("");
            }
        });
    }
}
