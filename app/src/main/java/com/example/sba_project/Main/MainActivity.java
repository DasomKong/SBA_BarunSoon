package com.example.sba_project.Main;

import android.content.Intent;
import android.os.Bundle;

import com.example.sba_project.GameRoomPkg.GameRoomActivity;
import com.example.sba_project.LoginActivity;
import com.example.sba_project.Register.Additional_data;
import com.example.sba_project.Userdata.UserDataManager;
import com.example.sba_project.Util.AutoScrollAdapter;
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

import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private BackPressCloseHandler backPressCloseHandler;
    AutoScrollViewPager autoViewPager;
    private AccessTokenTracker accessTokenTracker;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserDataManager.getInstance().Init(this);

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
        navigationView.getHeaderView(0).findViewById(R.id.signout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
                if(curUser != null)
                {
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                    Toast.makeText(MainActivity.this,"signout!",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this,"signout failed!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // additional register
        navigationView.getHeaderView(0).findViewById(R.id.AddRegi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
                if(curUser != null)
                {
                    Intent addregi_intent = new Intent(MainActivity.this, Additional_data.class);
                    addregi_intent.putExtra(Additional_data.FROM_KEY, Additional_data.KEY_WHERE.FROM_MAIN.getValue());
                    startActivity(addregi_intent);
                }
            }
        });

        ArrayList<String> data = new ArrayList<>(); //이미지 url를 저장하는 arraylist
        data.add("https://user-images.strikinglycdn.com/res/hrscywv4p/image/upload/c_limit,fl_lossy,h_9000,w_1200,f_auto,q_auto/457571/twins_uho36t.png");
        data.add("https://user-images.strikinglycdn.com/res/hrscywv4p/image/upload/c_limit,fl_lossy,h_9000,w_1200,f_auto,q_auto/457571/printscreen-dojo_yzijtn.png");
        data.add("https://user-images.strikinglycdn.com/res/hrscywv4p/image/upload/c_limit,fl_lossy,h_9000,w_1200,f_auto,q_auto/457571/printscreen-pila_xtgrld.png");
        data.add("https://user-images.strikinglycdn.com/res/hrscywv4p/image/upload/c_limit,fl_lossy,h_9000,w_1200,f_auto,q_auto/457571/printscreen-scoreboard_j8wkmo.png");

        autoViewPager = (AutoScrollViewPager)findViewById(R.id.autoViewPager);
        AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(this, data);
        autoViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
        autoViewPager.setInterval(5000); // 페이지 넘어갈 시간 간격 설정
        autoViewPager.startAutoScroll(); //Auto Scroll 시작
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
        // automatically handle clicks on the Home/Up button, so long
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
        // Handle navigation view item clicks here.
        switch (item.getItemId())
        {
            case R.id.nav_home:
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_tools:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
            case R.id.nav_createroom:
                Intent intent = new Intent(MainActivity.this, GameRoomActivity.class);
                intent.putExtra(GameRoomActivity.ROOM_PERMITION, GameRoomActivity.User_Permission.HOST);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
