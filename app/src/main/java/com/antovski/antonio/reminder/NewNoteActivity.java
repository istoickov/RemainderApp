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

        final Note updateNote = (Note) getIntent().getSerializableExtra("Note");

        if(updateNote != null){
            update = true;
        }

        final Button btnDate = (Button) findViewById(R.id.buttonDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment df = new DatePickerFragment(btnDate);
                df.show(getSupportFragmentManager(), "datePicker");
            }
        });

        final Button btnTime = (Button) findViewById(R.id.buttonTime);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment df = new TimePickerFragment(btnTime);
                df.show(getSupportFragmentManager(), "timePicker");
            }
        });

        final Button btnPlace = (Button) findViewById(R.id.btn_pick);
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewNoteActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        Button btnAddNote = (Button) findViewById(R.id.btnAdd);
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

                if(!date.contains("DATE") && !date.contains("TIME") && name != ""  && desc != ""){ //VALIDATION!!!
                    try{
                        if(!update){
                            NOTES_COUNT = db.getNotesCount() + 1;
                            note = new Note(NOTES_COUNT, name, desc, date, 0, 0);
                        }else{
                            updateNote.setName(name);
                            updateNote.setDescription(desc);
                            updateNote.setDate(date);
                            updateNote.setLng(0);
                            updateNote.setLat(0);
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
                    btnDate.setText("");
                    btnTime.setText("");
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
