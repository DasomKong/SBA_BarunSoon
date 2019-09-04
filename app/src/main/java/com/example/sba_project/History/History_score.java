package com.example.sba_project.History;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sba_project.R;
import com.example.sba_project.Userdata.UserDataManager;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;


public class History_score extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private DatabaseReference mDatabase;
    private Context context;
    private String syear;
    private String sday;
    private String smonth;
    private TextView textView ;
    private LineChart lineChart;
    private Button search;
    private Button date;
    private String today;
    private String gname = null;
    private int ryear;
    private int rmonth;
    private int rday;
    final ArrayList arrayList = new ArrayList<>(); // 파이널 달린거 주의
    ArrayAdapter<String> arrayAdapter;
    private Spinner gameselc;
    private String selectday = null;






    public static History_score newInstance() {
        Bundle args = new Bundle();

        History_score frament = new History_score();
        frament.setArguments(args);
        return frament;
    }


    public History_score() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        today = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
        ryear = Integer.valueOf(today.substring(0,4));
        rmonth = Integer.valueOf(today.substring(4,6));
        rday =  Integer.valueOf(today.substring(6,8));

        mDatabase = FirebaseDatabase.getInstance().getReference();

        context = container.getContext();
        final View rootView = inflater.inflate(R.layout.fragment_history_score, container, false);
        final DatePickerDialog dialog = new DatePickerDialog(getActivity(), listener, ryear, rmonth-1, rday);


        search = rootView.findViewById(R.id.btns);
        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(gname == null || selectday == null){
                    Toast.makeText(getContext(),"값을 입력해주세요!",Toast.LENGTH_SHORT).show();
                    return;
                }

                mDatabase.child("Game").child("uid").child(UserDataManager.getInstance().getCurUserData().uID)
                        .child(gname).child(selectday).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> x = new ArrayList<String>();
                        ArrayList y = new ArrayList<Float>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            System.out.println("abcd"+snapshot.getKey());
//                                System.out.println("abcd"+snapshot.child("kcal").getValue());
//                                System.out.println("abcd"+dataSnapshot.getValue());
                            System.out.println("abcd"+snapshot.child("score").getValue());

                            x.add(snapshot.getKey());
                            y.add(snapshot.child("score").getValue());

                        }
                        System.out.println("abcd" + x);
                        System.out.println("abcd" + y);
                        lineChart = (LineChart)rootView.findViewById(R.id.chart);


                        List<Entry> entries = new ArrayList<>();

                        String[] xp=new String[x.size()];


                        for(int i=0;i<y.size();i++){
                            int waxy = Integer.parseInt(y.get(i).toString());
                            float exam = (float) waxy;
                            entries.add(new Entry((float)i, exam));
                            xp[i]=x.get(i);
                        }





//        entries.add(new Entry(1, 1));
//        entries.add(new Entry(2, 2));
//        entries.add(new Entry(3, 0));
//        entries.add(new Entry(4, 4));
//        entries.add(new Entry(5, 3));

                        LineDataSet lineDataSet = new LineDataSet(entries, "Score");
                        lineDataSet.setLineWidth(2);
                        lineDataSet.setCircleRadius(6);
                        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
//        lineDataSet.setCircleColorHole(Color.BLUE);
                        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
                        lineDataSet.setDrawCircleHole(true);
                        lineDataSet.setDrawCircles(true);
                        lineDataSet.setDrawHorizontalHighlightIndicator(false);
                        lineDataSet.setDrawHighlightIndicators(false);
                        lineDataSet.setDrawValues(false);

                        LineData lineData = new LineData(lineDataSet);
                        lineChart.setData(lineData);

                        XAxis xAxis = lineChart.getXAxis();

                        xAxis.setValueFormatter(new IndexAxisValueFormatter(xp));

                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawGridLines(false);


                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setTextColor(Color.BLACK);
                        xAxis.enableGridDashedLine(8, 24, 0);

                        YAxis yLAxis = lineChart.getAxisLeft();
                        yLAxis.setTextColor(Color.BLACK);

                        YAxis yRAxis = lineChart.getAxisRight();
                        yRAxis.setDrawLabels(false);
                        yRAxis.setDrawAxisLine(false);
                        yRAxis.setDrawGridLines(false);

                        Description description = new Description();
                        description.setText("");

                        lineChart.setDoubleTapToZoomEnabled(false);
                        lineChart.setDrawGridBackground(false);
                        lineChart.setDescription(description);
                        lineChart.animateY(2000, Easing.EaseInCubic);
                        lineChart.invalidate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });


        textView = rootView.findViewById(R.id.textView);
        date = rootView.findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });


        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList) {
            @SuppressLint("WrongViewCast")
            public View getView(int positon, View convertView, ViewGroup parent) {
                View v = super.getView(positon, convertView, parent);
                if (positon == getCount()) {
//                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
//                    ((TextView)v.findViewById(android.R.id.text2)).setHint(getItem(getCount()));
                    ((TextView) v.findViewById(R.id.gameSelect)).setText("");
                    ((TextView) v.findViewById(R.id.gameSelect)).setHint(getItem(getCount()));

                }
                return v;
            }

            public int getCount(int getcount) {
                return super.getCount() - 1;
            }
        };

        final DatabaseReference games_ref = FirebaseDatabase.getInstance().getReference("Game").child("title");
        games_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.add("게임선택");
                for (DataSnapshot iter : dataSnapshot.getChildren()) {
                    /*NickName, Address, Age, eMail, PhotoUrl */
                    //ExtendedMyUserData tmpUser = UtilValues.GetUserDataFromDatabase(iter);
                    arrayList.add(iter.getKey());
                }
//                arrayList.add("게임선택");
                arrayAdapter.notifyDataSetChanged();
//                gameselc.setSelection(arrayAdapter.getCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        gameselc = rootView.findViewById(R.id.gameName);
        gameselc.setPrompt("게임선택");
        gameselc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final DatabaseReference tmpRef = UserDataManager.getInstance().getCurGameRoomRef();

                if (tmpRef != null) {
                    tmpRef.child("CategoryName").setValue(arrayList.get(i).toString());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        gameselc.setAdapter(arrayAdapter);
        gameselc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (arrayList.get(i).equals("게임선택") == true) {
                    //Toast.makeText(getApplicationContext(),"게임선택해주세요!",Toast.LENGTH_SHORT).show();
                    arrayList.remove(0);
                } else{
                    Toast.makeText(getApplicationContext(), arrayList.get(i) + "가 선택되었습니다.",
                            Toast.LENGTH_SHORT).show();
                    gname = (String) arrayList.get(i);
                    ((TextView)adapterView.getChildAt(0)).setTextColor(Color.WHITE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return rootView;
    }

    public void onClick(View v) {

    }


    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            syear = String.valueOf(year);
            sday = String.valueOf(dayOfMonth);


            smonth = String.valueOf(monthOfYear + 1);
            if(monthOfYear<10) {
                smonth = "0"+smonth;
            }

            selectday = syear+"-"+smonth+"-"+sday;
            textView.setText(syear +"/"+ smonth +"/" + sday);
            Toast.makeText(getApplicationContext(), year + "년" + (monthOfYear+1) + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();

        }
    };


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }




}
