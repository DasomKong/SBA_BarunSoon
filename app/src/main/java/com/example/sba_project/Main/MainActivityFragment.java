package com.example.sba_project.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sba_project.Adapter.gridPrac;
import com.example.sba_project.Game_Description.Gameintroduction;
import com.example.sba_project.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ImageView gameimage;
    TextView Moregames;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.content_main,container,false);

        Moregames = rootView.findViewById(R.id.Moregames);
        Moregames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), gridPrac.class);
                startActivity(intent);
            }
        });


        gameimage = rootView.findViewById(R.id.aimageview1);
        gameimage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","TOUCHDOWN");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.aimageview2);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","CORNER");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.aimageview3);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","TOUCHDOWN");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.aimageview4);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","RELE");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.bimageview1);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","SWET");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.bimageview2);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","GERM");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.bimageview3);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","TARGET");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.bimageview4);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","SCALA");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.cimageview1);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","VIKA");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.cimageview2);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","MINEWORD");
                startActivity(intent);
            }
        });

        gameimage = rootView.findViewById(R.id.cimageview3);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","NEWTON");
                startActivity(intent);
            }
        });


        gameimage = rootView.findViewById(R.id.cimageview4);
        gameimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","CONSTELLO");
                startActivity(intent);
            }
        });



        return rootView;
    }

}