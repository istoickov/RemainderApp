package com.antovski.antonio.reminder;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myBroadcastReciever myBroadcastReciever = new myBroadcastReciever();

        IntentFilter mTime = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(myBroadcastReciever, mTime);

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
}
