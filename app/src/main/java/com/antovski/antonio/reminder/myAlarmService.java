package com.antovski.antonio.reminder;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Antonio on 27-Feb-17.
 */

public class myAlarmService extends Service {
    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        DBHandler db= new DBHandler(this);
        List<Note> DBnotes = db.getAllNotes();
        String now = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        for(Note n : DBnotes){
            Log.d("NOW", now);
            Log.d("Note", n.getDate());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
