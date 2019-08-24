package com.example.sba_project.Adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.sba_project.R;

public class ImageAdapter extends BaseAdapter {

    Context context;

    public Integer[] gameThumb ={
            R.drawable.chrono1,
            R.drawable.constello1, R.drawable.corner1,
            R.drawable.danza1, R.drawable.dojo,
            R.drawable.dunk1, R.drawable.gaia1,
            R.drawable.galactic1, R.drawable.germ1,
            R.drawable.jam1, R.drawable.mineword1,
            R.drawable.newton1, R.drawable.pila1,
            R.drawable.puzz1, R.drawable.rele1,
            R.drawable.roar1, R.drawable.scala1,
            R.drawable.scboard1, R.drawable.swet1,
            R.drawable.target1, R.drawable.touchdown1,
            R.drawable.twins1, R.drawable.vika1,
            R.drawable.wak1, R.drawable.zoo1
    };

    public ImageAdapter(Context context){
        this.context = context;
    }



    @Override
    public int getCount() {
        return gameThumb.length;
    }

    @Override
    public Object getItem(int position) {
        return gameThumb[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        if(convertView == null){
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(300,300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else{
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(gameThumb[position]);
        return imageView;

    }
}
