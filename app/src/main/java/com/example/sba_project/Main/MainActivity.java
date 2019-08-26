package com.example.sba_project.Main;

import android.content.Intent;
import android.os.Bundle;

import com.example.sba_project.GameRoomPkg.GameRoomActivity;
import com.example.sba_project.Game_Description.Gameintroduction;
import com.example.sba_project.Game_Description.Gameplus;
import com.example.sba_project.LoginActivity;
import com.example.sba_project.Navi_fragment.Fragment1;
import com.example.sba_project.Navi_fragment.Fragment2;
import com.example.sba_project.Navi_fragment.Fragment3;
import com.example.sba_project.Navi_fragment.Fragment4;
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
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private BackPressCloseHandler backPressCloseHandler;
    private AccessTokenTracker accessTokenTracker;

    private FirebaseAuth.AuthStateListener authListener;

    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;
    private Fragment4 fragment4;
    private Gameplus gameplus;
    private Home home;



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
                    Intent addregi_intent = new Intent(MainActivity.this, Additional_data.class);
                    addregi_intent.putExtra(Additional_data.FROM_KEY, Additional_data.KEY_WHERE.FROM_MAIN.getValue());
                    startActivity(addregi_intent);
                }
            }
        });

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();
        gameplus = new Gameplus();
        home = new Home();

        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();

        tr.replace(R.id.content_layout, home);
        tr.addToBackStack(null);
        tr.commit();


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
            case R.id.nav_history:
                tr.replace(R.id.content_layout, fragment1);
                setTitle("내 이용기록");
                break;
            case R.id.nav_gallery:
                tr.replace(R.id.content_layout, gameplus);
                setTitle("전체게임");
                break;
            case R.id.nav_slideshow:
                tr.replace(R.id.content_layout, fragment3);
                setTitle("방만들기");
                break;
        }

        tr.addToBackStack(null);
        tr.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }
}
