package com.example.sba_project.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.sba_project.Game_Description.Gameintroduction;
import com.example.sba_project.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ImageView imageView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.content_main,container,false);
        imageView = rootView.findViewById(R.id.aimageview1);
        imageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","TOUCHDOWN");
                startActivity(intent);
            }
        });

        imageView = rootView.findViewById(R.id.aimageview2);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","CORNER");
                startActivity(intent);
            }
        });

        imageView = rootView.findViewById(R.id.aimageview3);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","TOUCHDOWN");
                startActivity(intent);
            }
        });

        imageView = rootView.findViewById(R.id.aimageview4);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","RELE");
                startActivity(intent);
            }
        });

        imageView = rootView.findViewById(R.id.bimageview1);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","SWET");
                startActivity(intent);
            }
        });

        imageView = rootView.findViewById(R.id.bimageview2);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","GERM");
                startActivity(intent);
            }
        });

        imageView = rootView.findViewById(R.id.bimageview3);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","TARGET");
                startActivity(intent);
            }
        });

        imageView = rootView.findViewById(R.id.bimageview4);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","SCALA");
                startActivity(intent);
            }
        });

        imageView = rootView.findViewById(R.id.cimageview1);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","VIKA");
                startActivity(intent);
            }
        });

        imageView = rootView.findViewById(R.id.cimageview2);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","MINEWORD");
                startActivity(intent);
            }
        });

        imageView = rootView.findViewById(R.id.cimageview3);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity)getActivity(), Gameintroduction.class);
                intent.putExtra("title","NEWTON");
                startActivity(intent);
            }
        });


        imageView = rootView.findViewById(R.id.cimageview4);
        imageView.setOnClickListener(new View.OnClickListener(){
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