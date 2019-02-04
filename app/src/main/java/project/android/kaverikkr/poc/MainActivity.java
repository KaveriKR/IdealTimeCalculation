package project.android.kaverikkr.poc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import project.android.kaverikkr.poc.BackgroundTasks.EveryDayScheduler;
import project.android.kaverikkr.poc.BackgroundTasks.ServiceForIdleTime;



/*
  This class displays the stored max_idle_times for each day as the sleep period (timmings)
  in a list, where each item represents each day.

 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        SharedPreferences preferences = getSharedPreferences("main_data",MODE_PRIVATE);

        //Retrieving ist from storage and setting it the recycler view
        if(preferences.contains(EveryDayScheduler.START_TIME)){
            HashMap<String, String> testHashMap1,testHashMap2;
            String strt = preferences.getString(EveryDayScheduler.START_TIME,null);
            String end = preferences.getString(EveryDayScheduler.END_TIME,null);

            if(strt!=null && end!=null){
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
                 testHashMap1 = gson.fromJson(strt, type);
                 testHashMap2 = gson.fromJson(end, type);
                Adapter adapter = new Adapter(this,testHashMap1,testHashMap2);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }
        }

        // Scheduling task to run every 5 mins
        Intent intent1 = new Intent(this,ServiceForIdleTime.class);
        PendingIntent intent2 = PendingIntent.getService(this,0,intent1,0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),60*1000,intent2);

        // Scheduling task to run every 24 hrs
        Intent intent = new Intent(MainActivity.this,EveryDayScheduler.class);
        PendingIntent intent0 = PendingIntent.getService(this,0,intent,0);
       // AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),AlarmManager.INTERVAL_DAY,intent0);


    }
}
