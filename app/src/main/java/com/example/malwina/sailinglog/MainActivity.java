package com.example.malwina.sailinglog;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    TextView tv1, tv2, tv3, tv5;
    ImageButton ib1, ib2, ib3;
    Button b5;
    long sailTime, engineTime, anchorTime;
    SharedPreferences app_preferences;

    Date currentTime;
    final Handler handler = new Handler();
    SharedPreferences.Editor editor;
    Boolean timeChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.textView1);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        tv5 = (TextView) findViewById(R.id.textView5);

        ib1 = (ImageButton) findViewById(R.id.button1);
        ib2 = (ImageButton) findViewById(R.id.button2);
        ib3 = (ImageButton) findViewById(R.id.button3);
        b5 = (Button) findViewById(R.id.button5);
        b5.setEnabled(false);

    }

    @Override
    protected void onResume() {
        super.onResume();


        app_preferences = getPreferences(this.MODE_PRIVATE);
        int state = app_preferences.getInt("state", 9);

        if (state == 0) {
            tv1.setTextAppearance(this,R.style.highlighted);
        }
        else if(state == 1) {
            tv2.setTextAppearance(this,R.style.highlighted);
        }
        else if(state == 2) {
            tv3.setTextAppearance(this,R.style.highlighted);
        }

        handler.removeCallbacksAndMessages(null);
        onChange();
    }

    private class GetTime extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onChange();
        }
    }

    public void sails(View view) {
        app_preferences = getPreferences(this.MODE_PRIVATE);
        editor = app_preferences.edit();

        handler.removeCallbacksAndMessages(null);
        editor.putLong("oldSailTime", app_preferences.getLong("sails", 0));
        editor.commit();
        onChange();

        tv1.setTextAppearance(this,R.style.highlighted);
        tv2.setTextAppearance(this,R.style.myStyle);
        tv3.setTextAppearance(this,R.style.myStyle);

        ib1.setEnabled(false);
        ib2.setEnabled(true);
        ib3.setEnabled(true);
        b5.setEnabled(false);

        currentTime = Calendar.getInstance().getTime();
        String currentDateandTime = sdf.format(currentTime);


        String newState = currentDateandTime;



        editor.putInt("state", 0);
        editor.putString("time", newState);
        editor.commit();
        timeChanged = false;

    }

    public void engine (View view) {

        app_preferences = getPreferences(this.MODE_PRIVATE);
        editor = app_preferences.edit();

        handler.removeCallbacksAndMessages(null);
        editor.putLong("oldEngineTime", app_preferences.getLong("engine", 0));
        editor.commit();
        onChange();

        tv1.setTextAppearance(this,R.style.myStyle);
        tv2.setTextAppearance(this,R.style.highlighted);
        tv3.setTextAppearance(this,R.style.myStyle);

        ib1.setEnabled(true);
        ib2.setEnabled(false);
        ib3.setEnabled(true);
        b5.setEnabled(false);

        currentTime = Calendar.getInstance().getTime();
        String currentDateandTime = sdf.format(currentTime);


        String newState = currentDateandTime;


        editor.putInt("state", 1);
        editor.putString("time", newState);
        editor.commit();
    }

    public void anchor (View view) {
        app_preferences = getPreferences(this.MODE_PRIVATE);
        editor = app_preferences.edit();

        handler.removeCallbacksAndMessages(null);
        editor.putLong("oldAnchorTime", app_preferences.getLong("anchor", 0));
        editor.commit();
        onChange();

        tv1.setTextAppearance(this,R.style.myStyle);
        tv2.setTextAppearance(this,R.style.myStyle);
        tv3.setTextAppearance(this,R.style.highlighted);

        ib1.setEnabled(true);
        ib2.setEnabled(true);
        ib3.setEnabled(false);
        b5.setEnabled(false);

        currentTime = Calendar.getInstance().getTime();
        String currentDateandTime = sdf.format(currentTime);


        String newState = currentDateandTime;


        editor.putInt("state", 2);
        editor.putString("time", newState);
        editor.commit();
    }


    public String convertTime (long diff) {
        String time = "00:00:00";

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        long elapsedHours = diff / hoursInMilli;
        diff = diff % hoursInMilli;

        long elapsedMinutes = diff / minutesInMilli;
        diff = diff % minutesInMilli;

        long elapsedSeconds = diff / secondsInMilli;

        String hrs = Long.toString(elapsedHours);
        String mins = Long.toString(elapsedMinutes);
        if (elapsedMinutes < 10) {
            mins = "0" + mins;
        }
        String sec = Long.toString(elapsedSeconds);
        if (elapsedSeconds < 10) {
            sec = "0" + sec;
        }
        time = hrs + ":" + mins + ":" + sec;
        return time;
    }

    public void onChange () {
        app_preferences = getPreferences(this.MODE_PRIVATE);
        int state = app_preferences.getInt("state", 9);

        String time = app_preferences.getString("time", "");

        currentTime = Calendar.getInstance().getTime();
        Date oldTime = null;
        try {
            oldTime = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            long diff = currentTime.getTime() - oldTime.getTime();
            long temp;

            if (state == 0) {
                sailTime = app_preferences.getLong("sails", 0);
                if (sailTime > 0)
                {
                    temp = app_preferences.getLong("oldSailTime", 0) + diff;
                    editor = app_preferences.edit();
                    editor.putLong("sails", temp);
                    editor.commit();
                }
                else {
                    sailTime = diff;
                    editor = app_preferences.edit();
                    editor.putLong("sails", sailTime);
                    editor.commit();
                }
            }
            else if(state == 1) {
                engineTime = app_preferences.getLong("engine", 0);
                if (engineTime > 0)
                {
                    temp = app_preferences.getLong("oldEngineTime", 0) + diff;
                    editor = app_preferences.edit();
                    editor.putLong("engine", temp);
                    editor.commit();
                }
                else {
                    engineTime = diff;
                    editor = app_preferences.edit();
                    editor.putLong("engine", engineTime);
                    editor.commit();
                }
            }
            else if(state == 2) {
                anchorTime = app_preferences.getLong("anchor", 0);
                if (anchorTime > 0)
                {
                    temp = app_preferences.getLong("oldAnchorTime", 0) + diff;
                    editor = app_preferences.edit();
                    editor.putLong("anchor", temp);
                    editor.commit();
                }
                else {
                    anchorTime = diff;
                    editor = app_preferences.edit();
                    editor.putLong("anchor", anchorTime);
                    editor.commit();
                }
            }



            tv1.setText(convertTime(app_preferences.getLong("sails", 0)));
            tv2.setText(convertTime(app_preferences.getLong("engine", 0)));
            tv3.setText(convertTime(app_preferences.getLong("anchor", 0)));


            long totalTime = app_preferences.getLong("sails", 0) + app_preferences.getLong("engine", 0) + app_preferences.getLong("anchor", 0);

            tv5.setText(convertTime(totalTime));
            //new GetTime().execute();


            handler.postDelayed(new Runnable() {

                public void run() {
                    new GetTime().execute();
                }
            }, 1000);

        } catch (NullPointerException e) {
            app_preferences = getPreferences(this.MODE_PRIVATE);
            editor = app_preferences.edit();
            editor.putInt("state", 3);
            editor.putString("time", sdf.format(currentTime.getTime()));
            editor.putLong("sails", 0);
            editor.putLong("engine", 0);
            editor.putLong("anchor", 0);
            editor.commit();
            onChange();
        }
    }

    public void sumUp (View view) {
        onChange();
        handler.removeCallbacksAndMessages(null);
        tv1.setTextAppearance(this,R.style.myStyle);
        tv2.setTextAppearance(this,R.style.myStyle);
        tv3.setTextAppearance(this,R.style.myStyle);

        ib1.setEnabled(true);
        ib2.setEnabled(true);
        ib3.setEnabled(true);
        b5.setEnabled(true);

        app_preferences = getPreferences(this.MODE_PRIVATE);

        editor = app_preferences.edit();
        editor.putInt("state", 3);
        editor.commit();
        onResume();
    }

    public void reset (View view) {
        editor = app_preferences.edit();
        editor.clear();
        editor.commit();
        onChange();
    }
}
