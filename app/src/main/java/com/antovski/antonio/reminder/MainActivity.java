package com.antovski.antonio.reminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        IntentFilter mTime = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(myBroadcastReceiver, mTime);

//        DBHandler db = new DBHandler(this);
//        db.deleteAll();
//        db.close();

        Button btnNewNote = (Button) findViewById(R.id.btnNewNote);
        btnNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                startActivity(intent);
            }
        });

        Button btnMyNotes = (Button) findViewById(R.id.btnMyNotes);
        btnMyNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyNotesActivity.class);
                startActivity(intent);
            }
        });
    }

    private final BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
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

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(noteDate);
                int noteDay = calendar.get(Calendar.DAY_OF_MONTH);
                int noteMonth = calendar.get(Calendar.MONTH) + 1;
                int noteYear = calendar.get(Calendar.YEAR);
                int noteHours = calendar.get(Calendar.HOUR_OF_DAY) + 1;
                int noteMinutes = calendar.get(Calendar.MINUTE);

                calendar.setTime(now);
                int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
                int nowMonth = calendar.get(Calendar.MONTH) + 1;
                int nowYear = calendar.get(Calendar.YEAR);
                int nowHours = calendar.get(Calendar.HOUR_OF_DAY) + 1;
                int nowMinutes = calendar.get(Calendar.MINUTE);

                if(noteDay == nowDay && noteMonth == nowMonth && noteYear == nowYear && (noteHours == nowHours || noteHours > nowHours)){
                    if(noteMinutes - nowMinutes == 30 || nowMinutes - noteMinutes == 30){
                        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                .setContentTitle(n.getName())
                                .setContentText(n.getDescription())
                                .setSmallIcon(R.drawable.ic_stat_)
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .setSound(sound);
                        Intent resultIntent = new Intent(context, MainActivity.class);
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                        stackBuilder.addParentStack(MainActivity.class);
                        stackBuilder.addNextIntent(resultIntent);
                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(resultPendingIntent);
                        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(n.getID(), mBuilder.build());
                    }
                }

                if(noteDay == nowDay && noteMonth == nowMonth && noteYear == nowYear && noteHours == nowHours && noteMinutes == nowMinutes){
                    Intent i = new Intent(MainActivity.this, NotifyActivity.class);
                    i.putExtra("Note", n);
                    startActivity(i);
                }
            }
        }
    };
}
