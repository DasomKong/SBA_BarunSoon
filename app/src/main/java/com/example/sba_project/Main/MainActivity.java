package com.example.sba_project.Main;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.sba_project.GameRoomPkg.GameRoomActivity;
import com.example.sba_project.Game_Description.Gameplus;
import com.example.sba_project.LoginActivity;
import com.example.sba_project.Register.Additional_data;
import com.example.sba_project.Register.Mypage;
import com.example.sba_project.Register.RegisterActivity;
import com.example.sba_project.Userdata.ExtendedMyUserData;
import com.example.sba_project.Userdata.UserDataManager;
import com.example.sba_project.Util.BackPressCloseHandler;
import com.example.sba_project.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private BackPressCloseHandler backPressCloseHandler;
    private AccessTokenTracker accessTokenTracker;

    private FirebaseAuth.AuthStateListener authListener;

    private TextView textView;
    private String a;

    private Gameplus gameplus;
    private Home home;

    private ExtendedMyUserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserDataManager.getInstance().Init(this);

        displaySelectScreen(R.id.content_layout);
        SetListenerTracker();
        SetViews();
    }

    private void SetListenerTracker() {
        backPressCloseHandler = new BackPressCloseHandler(this);

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null) {
                    FirebaseAuth.getInstance().signOut();
                }
            }
        };

        // 사용자 정보 변경 시.
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
//                    emailText.setText(user.getEmail());
//                    statusText.setText("Signed In");
//
//                    if (user.getPhotoUrl() != null) {
//                        displayImage(user.getPhotoUrl());
//                    }
                } else {
//                    emailText.setText("");
//                    statusText.setText("Signed Out");
//                    imageView.setImageResource(
//                            R.drawable.com_facebook_profile_picture_blank_square);
                }
            }
        };
    }

    private void SetViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // signout 버튼
        navigationView.getMenu().findItem(R.id.nav_signOut).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                final FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
                if(curUser != null)
                {
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                    Toast.makeText(MainActivity.this,"로그아웃!",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this,"로그아웃 실패!",Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        // additional register
        navigationView.getHeaderView(0).findViewById(R.id.goMyPage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
                if(curUser != null)
                {
                    Intent addregi_intent = new Intent(MainActivity.this, Mypage.class);
                    addregi_intent.putExtra(Additional_data.FROM_KEY, Additional_data.KEY_WHERE.FROM_MAIN.getValue());
                    startActivity(addregi_intent);
                }
            }
        });

        gameplus = new Gameplus();
        home = new Home();

        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();

        tr.replace(R.id.content_layout, home);
        tr.addToBackStack(null);
        tr.commit();
        setTitle("Home");

    }

    public void setUserData(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        TextView nav_header_text = (TextView) navigationView.getHeaderView(0).findViewById(R.id.header_nickname);
        nav_header_text.setText(UserDataManager.getInstance().getCurUserData().NickName);

        nav_header_text = (TextView) navigationView.getHeaderView(0).findViewById(R.id.header_email);
        nav_header_text.setText(UserDataManager.getInstance().getCurUserData().eMail);


        if(!UserDataManager.getInstance().getCurUserData().PhotoUrl.isEmpty()){
            System.out.println("abcd"+UserDataManager.getInstance().getCurUserData().PhotoUrl);
            Glide.with(MainActivity.this).load(UserDataManager.getInstance().getCurUserData().PhotoUrl).into((de.hdodenhof.circleimageview.CircleImageView)navigationView.getHeaderView(0).findViewById(R.id.header_profile_image));
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(authListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(authListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserDataManager.getInstance().Destroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            backPressCloseHandler.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectScreen(item.getItemId());
        return true;
    }

    public void displaySelectScreen(int itemId){

        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();

        switch (itemId) {
            case R.id.nav_home:
                tr.replace(R.id.content_layout, home);
                setTitle("Home");
                break;
//            case R.id.nav_history:
//                tr.replace(R.id.content_layout, );
//                setTitle("내 이용기록");
//                break;
            case R.id.nav_game:
                tr.replace(R.id.content_layout, gameplus);
                setTitle("전체게임");
                break;
            case R.id.nav_Room:
                Intent intent = new Intent(MainActivity.this, GameRoomActivity.class);
                intent.putExtra(GameRoomActivity.ROOM_PERMITION, GameRoomActivity.User_Permission.HOST);
                startActivity(intent);
                break;
        }

        tr.addToBackStack(null);
        tr.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }
}
