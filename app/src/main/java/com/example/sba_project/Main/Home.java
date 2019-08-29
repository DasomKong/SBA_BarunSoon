package com.example.sba_project.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.sba_project.Game_Description.Gameintroduction;
import com.example.sba_project.R;
import com.example.sba_project.Util.AutoScrollAdapter;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class Home extends Fragment {

    private ImageView gameimage;
    private TextView Moregames;
    AutoScrollViewPager autoViewPager;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home, container, false);

        ArrayList<String> data = new ArrayList<>(); //이미지 url를 저장하는 arraylist
        data.add("https://user-images.strikinglycdn.com/res/hrscywv4p/image/upload/c_limit,fl_lossy,h_9000,w_1200,f_auto,q_auto/457571/twins_uho36t.png");
        data.add("https://user-images.strikinglycdn.com/res/hrscywv4p/image/upload/c_limit,fl_lossy,h_9000,w_1200,f_auto,q_auto/457571/printscreen-dojo_yzijtn.png");
        data.add("https://user-images.strikinglycdn.com/res/hrscywv4p/image/upload/c_limit,fl_lossy,h_9000,w_1200,f_auto,q_auto/457571/printscreen-pila_xtgrld.png");
        data.add("https://user-images.strikinglycdn.com/res/hrscywv4p/image/upload/c_limit,fl_lossy,h_9000,w_1200,f_auto,q_auto/457571/printscreen-scoreboard_j8wkmo.png");

        autoViewPager = rootView.findViewById(R.id.autoViewPager);
        AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(getActivity(), data);
        autoViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
        autoViewPager.setInterval(5000); // 페이지 넘어갈 시간 간격 설정
        autoViewPager.startAutoScroll(); //Auto Scroll 시작

        Moregames = rootView.findViewById(R.id.Moregames);
        Moregames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).displaySelectScreen(R.id.nav_game);

            }
        });

        gameimage = rootView.findViewById(R.id.aimageview1);
        gameimage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Gameintroduction.class);
                intent.putExtra("title","TOUCHDOWN");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.aimageview2);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Gameintroduction.class);
                intent.putExtra("title","CORNER");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.aimageview3);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Gameintroduction.class);
                intent.putExtra("title","TOUCHDOWN");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.aimageview4);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Gameintroduction.class);
                intent.putExtra("title","RELE");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.bimageview1);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Gameintroduction.class);
                intent.putExtra("title","SWET");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.bimageview2);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Gameintroduction.class);
                intent.putExtra("title","GERM");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.bimageview3);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Gameintroduction.class);
                intent.putExtra("title","TARGET");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.bimageview4);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Gameintroduction.class);
                intent.putExtra("title","SCALA");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.cimageview1);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Gameintroduction.class);
                intent.putExtra("title","VIKA");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.cimageview2);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Gameintroduction.class);
                intent.putExtra("title","MINEWORD");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.cimageview3);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Gameintroduction.class);
                intent.putExtra("title","NEWTON");
                startActivity(intent);
            }
        });


        gameimage = rootView.findViewById(R.id.cimageview4);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Gameintroduction.class);
                intent.putExtra("title","CONSTELLO");
                startActivity(intent);
            }
        });

        return rootView;

    }
}