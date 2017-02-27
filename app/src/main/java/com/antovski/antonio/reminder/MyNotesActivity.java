package com.antovski.antonio.reminder;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MyNotesActivity extends AppCompatActivity implements ListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes);
        listFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuDayView:

                return true;
            case R.id.menuListView:
                listFragment();
                return true;
            case R.id.menuMonthView:
                monthView();
                return true;
            case R.id.menuWeekView:

                return true;
            case R.id.menuMapView:

                return true;
        }
        return false;
    }

    public void listFragment(){
        ListFragment lf = (ListFragment) getSupportFragmentManager().findFragmentByTag("ListFragment");
        if(lf == null){
            lf = new ListFragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentLayout, lf, "ListFragment");
        ft.commit();
    }

    public void monthView(){
        MonthView mv = (MonthView) getSupportFragmentManager().findFragmentByTag("MonthView");
        if(mv == null){
            mv = new MonthView();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentLayout, mv, "MonthView");
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
