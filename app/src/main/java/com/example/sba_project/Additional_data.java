package com.example.sba_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Additional_data extends AppCompatActivity implements View.OnClickListener {
    TextView NickName;
    TextView Age;
    TextView Address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_data);

        SetViews();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_additionalRegister:
                AdditionalRegister();
                break;
            case R.id.btn_nexttime:
                finish();
                break;
        }
    }

    void SetViews()
    {
        findViewById(R.id.btn_additionalRegister).setOnClickListener(this);
        findViewById(R.id.btn_nexttime).setOnClickListener(this);

        NickName = findViewById(R.id.NickName);
        Age = findViewById(R.id.Age);
        Address = findViewById(R.id.Address);
    }

    void AdditionalRegister()
    {
        String _nickname = NickName.getText().toString().trim();
        String _age = Age.getText().toString().trim();
        String _address = Address.getText().toString().trim();

        if(!_nickname.isEmpty() && !_age.isEmpty() && !_address.isEmpty())
        {
            // 파이어베이스 유저 아이디 루트로 등록.
        }
        else{
            Toast.makeText(this,"Fill all texts", Toast.LENGTH_SHORT);
        }
    }
}