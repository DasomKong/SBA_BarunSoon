package com.example.sba_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sba_project.Main.MainActivity;
import com.example.sba_project.Register.Additional_data;
import com.example.sba_project.Register.RegisterActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private EditText inputEmail;
    private EditText inputPassword;

    ProgressBar login_progress;
    String TAG = "LoginActivity";

    // 페이스북
    private CallbackManager callbackManager;

    // 파이어베이스 인증 객체 생성
    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        FirebaseUser CurUser = FirebaseAuth.getInstance().getCurrentUser();

        // 파이어베이스에 로그인된 유저가 있을 경우 바로 메인으로..
        if(CurUser != null)
        {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            // 최초 로그인일 경우 계정 연동 세팅.
            SetViews();
            SetGoogleLogin();
            SetSignInAndUp();
            SetFaceBookLogin();
        }
    }

    // class methods
    private void SetViews() {
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        login_progress = findViewById(R.id.login_progress);
    }

    private void SetFaceBookLogin() {
        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton)findViewById(R.id.buttonFacebookLogin);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Facebook Cancle", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Facebook Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SetGoogleLogin(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // 버튼 바인딩
        findViewById(R.id.Google_Login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent,RC_SIGN_IN);
            }
        });
    }
    private void SetSignInAndUp(){
        findViewById(R.id.btn_signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin(inputEmail.getText().toString().trim(), inputPassword.getText().toString().trim());
            }
        });


        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else{
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        // 어센티케이션에서 메일 있는지 확인할 것.
//        if(CheckSameID(acct.getEmail()))
//            return;

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "인증 실패", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "구글 로그인 인증 성공", Toast.LENGTH_SHORT).show();
                            ToAdditionalRegister();
                        }
                    }
                });
    }

    private boolean CheckSameID(String ID){
        // db 에 e-mail 도 등록시킬 것.
        String name = FirebaseAuth.getInstance().getCurrentUser().toString();

        DatabaseReference curData_ref = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().toString());
        DatabaseReference eMail_ref = curData_ref.child("eMail");

        if(eMail_ref.toString().equals(ID))
        {
            Toast.makeText(LoginActivity.this, "같은 이메일이 존재합니다.", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
            return false;
    }

    private void userLogin(String email, String password){
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"값을 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

//        if(CheckSameID(email))
//            return;

        login_progress.setVisibility(View.VISIBLE);

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if(task.isSuccessful()) {
                            login_progress.setVisibility(View.GONE);
                            ToMainActivity();
                        }

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            login_progress.setVisibility(View.GONE);
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "login failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void ToMainActivity(){
        Intent menuIntent = new Intent(LoginActivity.this, MainActivity.class);
        finish();
        LoginActivity.this.startActivity(menuIntent);
    }

    private void ToAdditionalRegister()
    {
        Intent intent = new Intent(LoginActivity.this, Additional_data.class);
        intent.putExtra(Additional_data.FROM_KEY, Additional_data.KEY_WHERE.FROM_LOGIN.getValue());
        startActivity(intent);
        finish();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(LoginActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                            ToMainActivity();
                        } else {
                            // 로그인 실패
                            Toast.makeText(LoginActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
