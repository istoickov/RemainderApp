package com.antovski.antonio.reminder;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Antonio on 13-Feb-17.
 */

public class Note implements Serializable, Comparable<Note> {
    private int ID;
    private String name;
    private String description;
    private Date date;
    private double lat;
    private double lng;

    public Note(){
        this.ID = -1;
        this.name = this.description  = null;
        this.date = null;
        this.lat = this.lng = -1;
    }

    public Note(int ID, String name, String description, String date, double lat, double lng) throws ParseException {
        this.ID = ID;
        this.name = name;
        this.description = description;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.date = sdf.parse(date);
        this.lat = lat;
        this.lng = lng;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            this.date = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDate() {
        String parseDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        parseDate = sdf.format(date);
        return parseDate;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return String.format("%d. %s - %s", this.ID, this.name, sdf.format(date));
    }

    @Override
    public int compareTo(Note o) {
        if(this.getDate().equals(o.date)){
            return this.getName().compareTo(o.getName());
        }
        return this.getDate().compareTo(o.getDate());
    }
}