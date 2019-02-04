package project.android.kaverikkr.poc.BackgroundTasks;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import project.android.kaverikkr.poc.MainActivity;


/*
  Service retrieves the max idle time for the last day and stores it in the list
  that represents the sleep periods for each day.

  It also resets the idle time parameters for next calculations.
 */
public class EveryDayScheduler extends Service {

public static final String TAG = "EveryDayScheduler";
public static final String START_TIME = "start_time";
public static final String END_TIME = "end_time";

private HashMap<String , String> startTimes ;
private HashMap<String , String> endTimes ;
    HashMap<String, String> testHashMap1,testHashMap2;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startTimes = new HashMap<>();
        endTimes = new HashMap<>();
        Log.e(TAG, "onStartCommand: " );

        SharedPreferences preferences = this.getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences preferences_main = this.getSharedPreferences("main_data",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences_main.edit();

        if(preferences.contains(ServiceForIdleTime.DURATION_HOUR)){
            Log.e(TAG, "onStartCommand: "+"1" );
            String start_tm = preferences.getString(ServiceForIdleTime.MAX_START_TIME," ");
            String end_tm  = preferences.getString(ServiceForIdleTime.MAX_END_TIME," ");

            if(preferences_main.contains(START_TIME)){
                Log.e(TAG, "onStartCommand: "+"in" );
                Gson gson = new Gson();
                String strt = preferences_main.getString(EveryDayScheduler.START_TIME,null);
                String end = preferences_main.getString(EveryDayScheduler.END_TIME,null);

                java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
                testHashMap1 = gson.fromJson(strt, type);
                testHashMap2 = gson.fromJson(end, type);
                startTimes.putAll(testHashMap1);
                endTimes.putAll(testHashMap2);

            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            startTimes.put(currentDateandTime,start_tm);
            endTimes.put(currentDateandTime,end_tm);

            Log.e(TAG, "onStartCommand: "+startTimes.size()+","+endTimes.size() );
            Gson gson = new Gson();
            String start_time = gson.toJson(startTimes);
            String end_time = gson.toJson(endTimes);
            editor.putString(START_TIME,start_time);

            editor.putString(END_TIME,end_time);
            editor.apply();

            SharedPreferences.Editor editor1 = preferences.edit();
            editor1.clear();
            editor1.apply();

        }




        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
