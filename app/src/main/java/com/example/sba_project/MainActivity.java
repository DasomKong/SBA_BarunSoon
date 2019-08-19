package com.example.sba_project;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressCloseHandler = new BackPressCloseHandler(this);

        SetViews();
        SetUserStat();
    }

    private void SetUserStat() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        ((TextView) findViewById(R.id.name)).setText(currentUser.getDisplayName());
        ((TextView) findViewById(R.id.email)).setText(currentUser.getEmail());
    }

    private void SetViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SetTestButtons();
    }

    private void SetTestButtons()
    {
        // Test SignOut
        findViewById(R.id.signout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 이미 작성된 적 있다면 할 필요없도록 예외 처리 추가되어야함.
        findViewById(R.id.Additional).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addreg_intent = new Intent(MainActivity.this, Additional_data.class);
                startActivity(addreg_intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
}
