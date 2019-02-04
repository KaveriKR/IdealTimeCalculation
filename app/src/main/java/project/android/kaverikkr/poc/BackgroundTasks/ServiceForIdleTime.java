package project.android.kaverikkr.poc.BackgroundTasks;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;


import android.text.format.Time;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Service finds out the largest idle time between screen locks and stores
 * in storage.
 */

public class ServiceForIdleTime extends Service {

    public static final String UNLOCKED = "unclocked";
    public static final String LOCKED = "locked";
    public static final String TAG = "ServiceIn";
    public static final String DURATION_HOUR = "duationHours";
    public static final String DURATON_MINUTES = "durationMinutes";
    public static final String MAX_START_TIME = "max_start_time";
    public static final String MAX_END_TIME = "max_end_time";

    boolean islocked = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences preferences = this.getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Log.e(TAG, "onHandleIntent: " );
        KeyguardManager myKM = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        if (myKM != null && myKM.isKeyguardLocked()) {
            //locked
            Log.e(TAG, "onHandleIntent: "+"locked" );

            if(islocked){
               // not unlocked yet
            }else {
                editor.putString(LOCKED, today.format("%k:%M:%S"));
                editor.apply();
            }
            islocked = true;
        }else{

            if(islocked){
                editor.putString(UNLOCKED, today.format("%k:%M:%S"));
                editor.apply();
                if(preferences.contains(UNLOCKED) && preferences.contains(LOCKED))
                {
                    String locked_time = preferences.getString(LOCKED, null);
                    String un_locked_time = preferences.getString(UNLOCKED, null);
                    Log.e(TAG, "" + locked_time + ":" + un_locked_time);
                        java.sql.Time tt = java.sql.Time.valueOf(locked_time);
                        java.sql.Time tt2 = java.sql.Time.valueOf(un_locked_time);

                        Time dur = Duration(tt, tt2);
                        Log.e(TAG, "" + dur.hour + ":" + dur.minute);

                        if (preferences.contains(DURATION_HOUR)) {

                            long dur_hr = preferences.getLong(DURATION_HOUR, 0);
                            long dur_mn = preferences.getLong(DURATON_MINUTES, 0);


                            if (dur.hour > dur_hr) {
                                if (dur.minute > dur_mn) {
                                    editor.putLong(DURATION_HOUR, dur.hour);
                                    editor.putLong(DURATON_MINUTES, dur.minute);
                                    editor.putString(MAX_START_TIME, locked_time);
                                    editor.putString(MAX_END_TIME, un_locked_time);
                                    editor.apply();
                                }
                            }

                        } else {
                            editor.putLong(DURATION_HOUR, dur.hour);
                            editor.putLong(DURATON_MINUTES, dur.minute);
                            editor.putString(MAX_START_TIME, locked_time);
                            editor.putString(MAX_END_TIME, un_locked_time);
                            editor.apply();
                            Log.e(TAG, "" + dur.hour + ":" + dur.minute);
                        }


                }
               // editor.apply();
            }
            islocked = false;
            Log.e(TAG, "onHandleIntent: "+"un_locked" );
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    private boolean getDuration(java.sql.Time t1, java.sql.Time t2){

        if(t1.getHours()>=t2.getHours()){
            if(t1.getMinutes() >t2.getMinutes()){
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param t1
     * @param t2
     * @return
     *
     * The function returns time that is equal to the duration between t1 and t2
     * where t2 occurs after t1
     */
    private Time Duration(java.sql.Time t1, java.sql.Time t2){
        Time time = new Time();
        Log.e(TAG, "Duration: "+t1+","+t2 );
        if(t1.before(t2)){
            Log.e(TAG, "Duration: "+t1+","+t2 );

            time.hour = t2.getHours()-t1.getHours();
            if(t2.getMinutes()>t1.getMinutes())
            time.minute = t2.getMinutes()-t1.getMinutes();
            else
                time.minute = t2.getMinutes()+60-t1.getMinutes();
        }

        return time ;
    }

}
