package com.antovski.antonio.reminder;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayFragment extends android.support.v4.app.Fragment{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private WeekView mWeekView;

    private ArrayList<Note> notes = new ArrayList<Note>();
    private List<WeekViewEvent> events = new ArrayList<>();

    public static DayFragment newInstance(String param1, String param2) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.setupWeekview();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_day, container, false);
        mWeekView = (WeekView) mView.findViewById(R.id.weekView);
        setupWeekview();

        DBHandler db = new DBHandler(this.getContext());
        notes = (ArrayList<Note>) db.getAllNotes();
        Collections.sort(notes);
        db.close();

        return mView;
    }

    private void setupWeekview(){
        if (mWeekView != null) {
            //set listener for month change
            mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
                @Override
                public List<? extends WeekViewEvent> onMonthChange(int i, int i1) {
                    int j = 0;
                    events.clear();
                    for (Note n : notes) {
                        Date date = n.getDate1();
                        Calendar startTime = Calendar.getInstance();
                        startTime.setTime(date);
                        startTime.set(Calendar.MONTH, startTime.get(Calendar.MONTH));

                        Calendar endTime = Calendar.getInstance();
                        endTime.setTime(date);
                        endTime.set(Calendar.MONTH, endTime.get(Calendar.MONTH));
                        endTime.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY)+1);

                        WeekViewEvent event = new WeekViewEvent(1, n.getName(), startTime, endTime);
                        events.add(event);
                        ++j;
                    }

                    mWeekView.invalidate();
                    return events;
                }
            });

            //set listener for event click
            mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
                @Override
                public void onEventClick(final WeekViewEvent event, RectF eventRect) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("Do you want to delete event?");

                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            notes.remove(event);
                            DBHandler db = new DBHandler(getThisContext());

                            Note del = null;
                            for(Note n : db.getAllNotes())
                                if(n.getName().equals(event.getName()) &&
                                        (event.getStartTime().getTime().getTime() > n.getTime()-10000 ||
                                                event.getStartTime().getTime().getTime() < n.getTime()+10000)){
                                    del = n;
                                    break;
                                }

                            if(del != null)
                                db.deleteNote(del);

                            db.close();
                            mWeekView.invalidate();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            mWeekView.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {
                @Override
                public void onEmptyViewClicked(final Calendar time) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("Do you want to add new event?");

                    builder.setPositiveButton("Add note", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Date date = new Date();
                            time.set(Calendar.MONTH, time.get(Calendar.MONTH)+1);
                            date.setTime(time.getTime().getTime());

                            Intent intent = new Intent(getActivity(), NewNoteActivity.class);
                            intent.putExtra("dayClicked", date);
                            startActivity(intent);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            //set event long press listener
            mWeekView.setEventLongPressListener(new WeekView.EventLongPressListener() {
                @Override
                public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
                    //TODO: Handle event long press
                }
            });
        }}

    public Context getThisContext(){ return this.getContext(); }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
