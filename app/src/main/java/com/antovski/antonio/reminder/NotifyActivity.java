package com.antovski.antonio.reminder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

public class NotifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notify);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Note n = (Note) getIntent().getSerializableExtra("Note");

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(this, sound);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(n.getName())
                .setMessage(n.getDescription() + "\n" + "Lat: " + n.getLat() + ", Lng: " + n.getLng())
                .setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(NotifyActivity.this, MyNotesActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        r.play();
    }
}
