package com.example.sba_project.Game_Description;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sba_project.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


//YouTubeBaseActivity로 상속 받는것에 유의
public class Gameintroduction extends YouTubeBaseActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        final String title = intent.getExtras().getString("title");

        databaseReference.child("Game").child("title").child(title).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final String touchdownVideo = (String) dataSnapshot.child("URL").getValue();


                if(touchdownVideo.equals("NULL")){
                    setContentView(R.layout.activity_gameintroduction_image);
                    ImageView gameimage = (ImageView)findViewById(R.id.gameimage);

                    switch (title) {
                        case "TWINS":
                            gameimage.setImageResource(R.drawable.twins1);
                            break;
                        case "DOJO":
                            gameimage.setImageResource(R.drawable.dojo);
                            break;
                        case "PILA":
                            gameimage.setImageResource(R.drawable.pila1);
                            break;
                        case "SCOREBOARD":
                            gameimage.setImageResource(R.drawable.scboard1);
                            break;
                        case "CHRONO":
                            gameimage.setImageResource(R.drawable.chrono1);
                            break;
                        case "RELE":
                            gameimage.setImageResource(R.drawable.rele1);
                            break;
                        case "WAK":
                            gameimage.setImageResource(R.drawable.wak1);
                            break;
                    }


                }
                else {
                    setContentView(R.layout.activity_sub);

                    YouTubePlayerView youTubeView;
                    YouTubePlayer.OnInitializedListener listener;
                    youTubeView = (YouTubePlayerView) findViewById(R.id.youtubeView);


                    //리스너 등록부분
                    listener = new YouTubePlayer.OnInitializedListener() {

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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
}
