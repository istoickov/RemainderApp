package com.antovski.antonio.reminder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

public class MyNotesActivity extends AppCompatActivity implements ListFragment.OnFragmentInteractionListener{

    CompactCalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
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
        switch (item.getItemId()) {
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

    public void listFragment() {
        ListFragment lf = (ListFragment) getSupportFragmentManager().findFragmentByTag("ListFragment");
        if (lf == null) {
            lf = new ListFragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentLayout, lf, "ListFragment");
        ft.commit();
    }

    public void monthView() {
        Fragment f = getSupportFragmentManager().findFragmentByTag("MaterialCalendarView");
        if(f == null){
            f = new Fragment();
        }
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.fragmentLayout, f, "calendarView");
        t.commit();
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(MyNotesActivity.this, MainActivity.class);
        startActivity(intent);
    }
}