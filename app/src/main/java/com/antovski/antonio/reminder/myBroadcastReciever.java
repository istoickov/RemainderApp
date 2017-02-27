package com.antovski.antonio.reminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Antonio on 27-Feb-17.
 */

public class myBroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DBHandler db = new DBHandler(context);
        List<Note> notes = db.getAllNotes();
        Date now = new Date();

        for(Note n : notes){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date noteDate = null;
            try {
                noteDate = sdf.parse(n.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(now.getTime() - noteDate.getTime() <= 30*60*1000){
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                            .setContentTitle(n.getName())
                                            .setContentText(n.getDescription())
                                            .setSmallIcon(R.drawable.ic_stat_)
                                            .setAutoCancel(true)
                                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                Intent resultIntent = new Intent(context, MainActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());
            }
        }
    }
}
