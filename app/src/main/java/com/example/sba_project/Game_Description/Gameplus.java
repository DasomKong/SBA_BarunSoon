package com.example.sba_project.Game_Description;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.sba_project.R;

public class Gameplus extends Fragment {

    String title;


    public Gameplus() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game_main, container,false);

        final GridView gridView;

        gridView = rootView.findViewById(R.id.grid_game);
        ImageAdapter imageAdapter = new ImageAdapter(getActivity());
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), (++position)+"번째 이미지 선택", Toast.LENGTH_SHORT).show();

                switch(position){
                    case 0:
                        title = "CHRONO";
                        break;
                    case 1:
                        title = "CONSTELLO";
                        break;
                    case 2:
                        title = "CORNER";
                        break;
                    case 3:
                        title = "DANZA";
                        break;
                    case 4:
                        title = "DOJO";
                        break;
                    case 5:
                        title = "DUNK";
                        break;
                    case 6:
                        title = "GAIA";
                        break;
                    case 7:
                        title = "GALACTIC";
                        break;
                    case 8:
                        title = "GERM";
                        break;
                    case 9:
                        title = "JAM";
                        break;
                    case 10:
                        title = "MINEWORD";
                        break;
                    case 11:
                        title = "NEWTON";
                        break;
                    case 12:
                        title = "PILA";
                        break;
                    case 13:
                        title = "PUZZ";
                        break;
                    case 14:
                        title = "RELE";
                        break;
                    case 15:
                        title = "ROAR";
                        break;
                    case 16:
                        title = "SCALA";
                        break;
                    case 17:
                        title = "SCOREBOARD";
                        break;
                    case 18:
                        title = "SWET";
                        break;
                    case 19:
                        title = "TARGET";
                        break;
                    case 20:
                        title = "TOUCHDOWN";
                        break;
                    case 21:
                        title = "TWINS";
                        break;
                    case 22:
                        title = "VIKA";
                        break;
                    case 23:
                        title = "WAK";
                        break;
                    case 24:
                        title = "ZOO";
                        break;
                    default :
                        System.out.println("ERROR");
                }

                Intent intent = new Intent(getActivity(), Gameintroduction.class);
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });

        return rootView;
    }



}