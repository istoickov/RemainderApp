package com.antovski.antonio.reminder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewNoteActivity extends AppCompatActivity {

    private static int NOTES_COUNT = 0;
    private boolean update = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note_activitiy);

        final Context context = this;

        final EditText txtName = (EditText) findViewById(R.id.txtNoteName);
        final EditText txtDesc = (EditText) findViewById(R.id.txtNoteDescription);
        final Button btnDate = (Button) findViewById(R.id.buttonDate);
        final Button btnTime = (Button) findViewById(R.id.buttonTime);

        final Note updateNote = (Note) getIntent().getSerializableExtra("Note");

        if(updateNote != null){
            update = true;
            txtName.setText(updateNote.getName());
            txtDesc.setText(updateNote.getDescription());
            String oldDate = updateNote.getDate().split(" ")[0];
            String oldTime = updateNote.getDate().split(" ")[1];
            btnDate.setText(oldDate);
            btnTime.setText(oldTime);
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

        final Button btnPlace = (Button) findViewById(R.id.btn_pick);

        final String latlng = getIntent().getStringExtra("Place");

        double lat = 0;
        double lng = 0;

        if(latlng != null){
            //lat/lng: (double,double)
            String pos = latlng.split(" ")[1];
            String[] parts = pos.substring(1, pos.length() - 1).split(",");
            lat = Double.parseDouble(parts[0]);
            lng = Double.parseDouble(parts[1]);

        }

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewNoteActivity.this, MapsActivity.class);
                Note tempNote = null;
                if(updateNote != null){
                    tempNote = updateNote;
                    intent.putExtra("Note", tempNote);
                }
                startActivity(intent);
            }
        });

        Button btnAddNote = (Button) findViewById(R.id.btnAdd);
        final double finalLat = lat;
        final double finalLng = lng;
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtName = (EditText) findViewById(R.id.txtNoteName);
                String name = txtName.getText().toString().trim();
                EditText txtDesc = (EditText) findViewById(R.id.txtNoteDescription);
                String desc = txtDesc.getText().toString().trim();
                String date = btnDate.getText().toString() + " " + btnTime.getText().toString();

                Note note = null;
                DBHandler db = new DBHandler(context);

                if(!date.contains("DATE") && !date.contains("TIME") && name.length() != 0  && desc.length() != 0){
                    try{
                        if(!update){
                            NOTES_COUNT = db.getNotesCount() + 1;
                            note = new Note(NOTES_COUNT, name, desc, date, finalLat, finalLng);
                        }else{
                            updateNote.setName(name);
                            updateNote.setDescription(desc);
                            updateNote.setDate(date);
                            updateNote.setLng(finalLng);
                            updateNote.setLat(finalLat);
                        }
                    }
                    catch (Exception err){
                        err.printStackTrace();
                    }
                }

                if(note != null && !update){
                    db.addNote(note);
                    db.close();
                    txtName.setText("");
                    txtDesc.setText("");
                    btnDate.setText("DATE");
                    btnTime.setText("TIME");
                }else if(update){
                    db.updateNote(updateNote);
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
