package com.example.sba_project.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sba_project.LoginActivity;
import com.example.sba_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    TextView inputEmail;
    TextView inputPassword;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    Task<AuthResult> Task = createUser(email, password);
                    // 등록 성공일 경우.
                    if(Task.isSuccessful()) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        finish();
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No blanks!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // 회원가입
    private Task<AuthResult> createUser(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            Toast.makeText(RegisterActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, Additional_data.class);
                            finish();
                            startActivity(intent);
                        } else {
                            // 회원가입 실패
                            Toast.makeText(RegisterActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
