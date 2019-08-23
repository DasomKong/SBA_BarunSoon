package com.example.sba_project.Game_Description;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaSession2;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sba_project.R;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.Duration;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


//YouTubeBaseActivity로 상속 받는것에 유의
public class GameplusActivity extends YouTubeBaseActivity {



    YouTubePlayerView youTubeView;
    YouTubePlayer.OnInitializedListener listener;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);


        Intent intent = getIntent();
        String title = intent.getExtras().getString("title");
        System.out.println("abcd" + title);



        databaseReference.child("Game").child("title").child("TOUCHDOWN").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String touchdownName = (String) dataSnapshot.getKey();
                TextView nameView = (TextView)findViewById(R.id.nameView);
                nameView.setText(touchdownName);

                String touchdownDuration = (String) dataSnapshot.child("Game_Duration").getValue();
                TextView durationView = (TextView)findViewById(R.id.durationView);
                durationView.setText(touchdownDuration);

                String touchdownDescription= (String) dataSnapshot.child("Game_Description").getValue();
                TextView descriptionView = (TextView)findViewById(R.id.descriptionView);
                descriptionView.setText(touchdownDescription);

                String touchdownNumber = (String) dataSnapshot.child("Number_of_Players").getValue();
                TextView numberView = (TextView)findViewById(R.id.numberView);
                numberView.setText(touchdownNumber);

                String touchdownTarget = (String) dataSnapshot.child("Target_Age").getValue();
                TextView targetView = (TextView)findViewById(R.id.targetView);
                targetView.setText(touchdownTarget);

                final String touchdownVideo = (String) dataSnapshot.child("URL").getValue();




//                mWebView.loadUrl(gaiaVideo);

                //System.out.println("aaa"+dataSnapshot.getValue());

                youTubeView = (YouTubePlayerView)findViewById(R.id.youtubeView);


                //리스너 등록부분
                listener = new YouTubePlayer.OnInitializedListener(){

                    //초기화 성공시
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.loadVideo(touchdownVideo);//url의 맨 뒷부분 ID값만 넣으면 됨

                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }

                };

                youTubeView.initialize("AIzaSyDOmjYJs_Uly-x-lG9NNUvNSiwp8Fr3M_8", listener);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
}

