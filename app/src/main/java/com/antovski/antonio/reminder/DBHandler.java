package com.antovski.antonio.reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 16-Feb-17.
 */

public class DBHandler extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;
    //Database Name
    private static final String DATABASE_NAME = "NotesInfo";
    //Table Name
    private static final String TABLE_NOTES = "Notes";

    //Notes Table Columns names
    private static final String KEY_ID = "ID";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESC = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";


    public DBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_DESC + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LAT + " REAL,"
                + KEY_LNG + " REAL)";
        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        //Creating table again
        onCreate(db);
    }

    //Adding new note
    public void addNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, note.getID());   //Note ID
        values.put(KEY_NAME, note.getName());   //Note Name
        values.put(KEY_DESC, note.getDescription());    //Note Description
        values.put(KEY_DATE, note.getDate());   //Note Date
        values.put(KEY_LAT, note.getLat());     //Note LAT
        values.put(KEY_LNG, note.getLng());     //Note LNG

        //Inserting Row
        db.insert(TABLE_NOTES, null, values);
        db.close();     //Closing Database Connection
    }

    //Getting one note
    public Note getNote(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTES, new String[]{KEY_ID, KEY_NAME, KEY_DESC, KEY_DATE, KEY_LAT, KEY_LNG}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor != null)
            cursor.moveToFirst();

        Note note = null;

        try {
            note = new Note(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4), cursor.getDouble(5));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return note;
    }

    //Getting all notes
    public List<Note> getAllNotes(){
        List<Note> notes = new ArrayList<Note>();

        //Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NOTES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if(cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setID(Integer.parseInt(cursor.getString(0)));
                note.setName(cursor.getString(1));
                note.setDescription(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setLat(Double.parseDouble(cursor.getString(4)));
                note.setLng(Double.parseDouble(cursor.getString(5)));
                notes.add(note);
            }while(cursor.moveToNext());
        }

        //return notes list
        return notes;
    }

    //Getting notes count
    public int getNotesCount(){
        String countQuery = "SELECT * FROM " + TABLE_NOTES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        //return count
        return count;
    }

    //Updating a note
    public int updateNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, note.getName());   //Note Name
        values.put(KEY_DESC, note.getDescription());    //Note Description
        values.put(KEY_DATE, note.getDate().toString());   //Note Date
        values.put(KEY_LAT, note.getLat());     //Note LAT
        values.put(KEY_LNG, note.getLng());     //Note LNG

        //updating row
        return db.update(TABLE_NOTES, values, KEY_ID + " = ?", new String[]{String.valueOf(note.getID())});
    }

    //Deleting a note
    public void deleteNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + " = ?", new String[]{String.valueOf(note.getID())});
        db.close();
    }

    //Delete All
    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NOTES);
        db.close();
    }
}
