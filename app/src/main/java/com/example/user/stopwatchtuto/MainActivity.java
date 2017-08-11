package com.example.user.stopwatchtuto;

import android.database.Cursor;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView TV_timer;
    Button BTN_start;
    Button BTN_stop;
    long difference = 0L;
    long secondDifference, minuteDifference, hourDifference=0L;
    Handler handler = new Handler();
    Integer hour, min, sec;
    String startTm = null;
    String startTime=null;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TV_timer = (TextView) findViewById(R.id.UI_TV_timer);
        BTN_start = (Button) findViewById(R.id.UI_BTN_start);
        BTN_stop = (Button) findViewById(R.id.UI_BTN_stop);

        if (getLastEvent().equals("CheckIn")) {

            startTm = getStartTime();

        handler.postDelayed(runnable,0);}

        final DBHelper dbHelper = new DBHelper(getApplicationContext());
        BTN_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDateFormat = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
                Calendar calendar = Calendar.getInstance();
                startTime = simpleDateFormat.format(calendar.getTime());
                String currentTime = simpleDateFormat.format(calendar.getTime());
                String currentEvent = "CheckIn";



           /*     if (getLastEvent().equals("CheckIn")) {

                    startTm = getStartTime();

                } else {*/
                    startTm = currentTime;
                    dbHelper.insertTimer(startTm, currentEvent);
                    //Log.d("LastEvent",getLastEvent());
              //  }
                handler.postDelayed(runnable,0);

            }
        });
        BTN_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
            }
        });
    }

    public String getStartTime() {

        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.getTimer();
        cursor.moveToLast();
        return cursor.getString(cursor.getColumnIndex("startTime"));

    }

    public String getLastEvent() {
        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.getTimer();

        if(cursor.getCount()>0){
            cursor.moveToLast();
        return cursor.getString(cursor.getColumnIndex("event"));}
        else return "null";


    }


    Runnable runnable = new Runnable() {
        public long getDifferenceInMilli(String startTime, String currentTime) throws ParseException {
            long difference = 0;
            simpleDateFormat = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
            Date dateStart = simpleDateFormat.parse(startTime);
            Date dateCurrent = simpleDateFormat.parse(currentTime);
            difference = dateCurrent.getTime() - dateStart.getTime();
            return difference;
        }

        public long getSecDifference(long differenceinMilli) {
            return differenceinMilli / 1000 % 60;
        }

        public long getMinDifference(long differenceInMilli) {
            return differenceInMilli / (1000 * 60) % 60;
        }

        public long getHourDifference(long differenceInMilli) {
            return differenceInMilli / (1000 * 60 * 60) % 24;
        }


        @Override
        public void run() {
            simpleDateFormat = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String currentTime = simpleDateFormat.format(calendar.getTime());
            try {
                difference = getDifferenceInMilli(startTm, currentTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int sec = (int) getSecDifference(difference);
            int min = (int) getMinDifference(difference);
            int hour = (int) getHourDifference(difference);

            TV_timer.setText(" "+hour
                    +":"+min
                    +":"+sec);
            handler.postDelayed(this,0);
        }

    };
}

