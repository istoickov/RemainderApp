package com.antovski.antonio.reminder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewNoteActivity extends AppCompatActivity {

    private static int NOTES_COUNT = 0;
    private boolean update = false;

    Note note;
    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note_activitiy);

        final Context context = this;

        final EditText txtName = (EditText) findViewById(R.id.txtNoteName);
        final EditText txtDesc = (EditText) findViewById(R.id.txtNoteDescription);
        final Button btnDate = (Button) findViewById(R.id.buttonDate);
        final Button btnTime = (Button) findViewById(R.id.buttonTime);

        note = (Note) getIntent().getSerializableExtra("Note");

        if(note != null){
            if(note.getID() != -1){
                update = true;
            }
            txtName.setText(note.getName());
            txtDesc.setText(note.getDescription());
            String oldDate = note.getDate().split(" ")[0];
            String oldTime = note.getDate().split(" ")[1];
            btnDate.setText(oldDate);
            btnTime.setText(oldTime);
            lat = note.getLat();
            lng = note.getLng();
        } else {
            note = new Note();
        }

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment df = new DatePickerFragment(btnDate);
                df.show(getSupportFragmentManager(), "datePicker");
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment df = new TimePickerFragment(btnTime);
                df.show(getSupportFragmentManager(), "timePicker");
            }
        });

        final String latlng = getIntent().getStringExtra("Place");

        if(latlng != null){
            //lat/lng: (double,double)
            String pos = latlng.split(" ")[1];
            String[] parts = pos.substring(1, pos.length() - 1).split(",");
            lat = Double.parseDouble(parts[0]);
            lng = Double.parseDouble(parts[1]);
            note.setLat(lat);
            note.setLng(lng);
        }

        final Button btnPlace = (Button) findViewById(R.id.btn_pick);
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setName(txtName.getText().toString().trim());
                note.setDescription(txtDesc.getText().toString().trim());

                String date = btnDate.getText().toString() + " " + btnTime.getText().toString();
                if(!date.contains("DATE") || !date.contains("TIME")){
                    note.setDate(date);
                }

                Intent intent = new Intent(NewNoteActivity.this, MapsActivity.class);
                intent.putExtra("Note", note);
                startActivity(intent);
            }
        });

        Button btnAddNote = (Button) findViewById(R.id.btnAdd);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtName.getText().toString().trim();
                String desc = txtDesc.getText().toString().trim();
                String date = btnDate.getText().toString() + " " + btnTime.getText().toString();

                DBHandler db = new DBHandler(context);

                if(!date.contains("DATE") && !date.contains("TIME") && name.length() != 0  && desc.length() != 0){
                    try{
                        note.setName(name);
                        note.setDate(date);
                        note.setLat(lat);
                        note.setLng(lng);

                        if(!update){
                            NOTES_COUNT = db.getNotesCount() + 1;
                            note.setID(NOTES_COUNT);
                            db.addNote(note);
                            db.close();
                            txtName.setText("");
                            txtDesc.setText("");
                            btnDate.setText("DATE");
                            btnTime.setText("TIME");
                        } else {
                            db.updateNote(note);
                            db.close();
                            txtName.setText("");
                            txtDesc.setText("");
                            btnDate.setText("DATE");
                            btnTime.setText("TIME");
                        }

                    }
                    catch (Exception err){
                        err.printStackTrace();
                    }
                }

                Intent intent = new Intent(NewNoteActivity.this, MyNotesActivity.class);
                startActivity(intent);
            }
        });

        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewNoteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}