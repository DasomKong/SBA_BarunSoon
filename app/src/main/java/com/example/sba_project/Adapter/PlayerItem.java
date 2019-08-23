package com.example.sba_project.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sba_project.R;
import com.example.sba_project.Userdata.ExtendedMyUserData;

import java.util.ArrayList;

public class PlayerItem extends BaseAdapter implements View.OnClickListener {
    private ArrayList<ExtendedMyUserData> UserList = new ArrayList<>();
    private Activity activity;

    @Override
    public int getCount() {
        return UserList.size();
    }

    @Override
    public ExtendedMyUserData getItem(int i) {
        return UserList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.playeritem, viewGroup, false);
        }

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        ExtendedMyUserData newUser = getItem(i);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        ((TextView)view.findViewById(R.id.NickName)).setText(newUser.NickName);

        if(!newUser.PhotoUrl.isEmpty())
            Glide.with(activity).load(newUser.PhotoUrl).into((ImageView)view.findViewById(R.id.profile));

        view.findViewById(R.id.button_chu).setOnClickListener(this);
        view.findViewById(R.id.button_exit).setOnClickListener(this);

        return view;
    }

    public void SetActivity(Activity _activity){
        activity = _activity;
    }

    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(ExtendedMyUserData newUser) {
        UserList.add(newUser);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_chu:
                break;
            case R.id.button_exit:
                break;
        }
    }
}
