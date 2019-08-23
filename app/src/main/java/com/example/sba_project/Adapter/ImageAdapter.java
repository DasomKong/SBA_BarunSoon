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

    private Integer[] gameThumb ={
            R.drawable.chrono1,R.drawable.dunk1,R.drawable.gaia1,
            R.drawable.jam1, R.drawable.constello1, R.drawable.corner1,
            R.drawable.gaia1, R.drawable.gaia1, R.drawable.gaia1,
            R.drawable.gaia1, R.drawable.gaia1, R.drawable.gaia1,
            R.drawable.gaia1, R.drawable.gaia1, R.drawable.gaia1,
            R.drawable.gaia1, R.drawable.gaia1, R.drawable.gaia1
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
        }else{
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(gameThumb[position]);
        return imageView;

    }
}

