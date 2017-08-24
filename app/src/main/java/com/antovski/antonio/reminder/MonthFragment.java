package com.antovski.antonio.reminder;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MonthFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MonthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthFragment extends android.support.v4.app.Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayList<Note> notes = new ArrayList<Note>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MonthFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MonthFragment newInstance(String param1, String param2) {
        MonthFragment fragment = new MonthFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        DBHandler db = new DBHandler(this.getContext());
        notes = (ArrayList<Note>) db.getAllNotes();
        Collections.sort(notes);
        db.close();

        final CompactCalendarView compactCalendarView = (CompactCalendarView) view.findViewById(R.id.month);
        final ListView listMonth = (ListView) view.findViewById(R.id.listMonth);

        for (Note n : notes) {
            compactCalendarView.addEvent(new Event(Color.GRAY, n.getTime(), n.getName()));
        }

        listMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage(String.format("%s\n%.5f : %.5f", notes.get(position).getDescription(), notes.get(position).getLat(), notes.get(position).getLng()));
                builder.setTitle(notes.get(position).getName());

                builder.setPositiveButton("Edit Note", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), NewNoteActivity.class);
                        intent.putExtra("Note", notes.get(position));
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Delete Note", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHandler db = new DBHandler(getContext());
                        db.deleteNote(notes.get(position));
                        notes = (ArrayList<Note>) db.getAllNotes();
                        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(getContext(), android.R.layout.simple_list_item_1, notes);
                        ListView listMonth = (ListView) getView().findViewById(R.id.listMonth);
                        listMonth.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        db.close();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(final Date date) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Do you want to add new event?");

                builder.setPositiveButton("Add note", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), NewNoteActivity.class);
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        c.set(Calendar.MONTH, c.get(Calendar.MONTH)+1);

                        Calendar cal = Calendar.getInstance();

                        c.set(Calendar.HOUR, cal.get(Calendar.HOUR));
                        c.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));

                        Date d = c.getTime();
                        intent.putExtra("monthClicked", d);
                        startActivity(intent);
                    }
                });

                builder.setNeutralButton("View notes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHandler db = new DBHandler(getContext());
                        notes = (ArrayList<Note>) db.getAllNotes();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String str_date = sdf.format(date);

                        Iterator<Note> iterator = notes.iterator();
                        while(iterator.hasNext()) {
                            if (!iterator.next().getDate().toString().split(" ")[0].trim().equals(str_date))
                                iterator.remove();
                        }

                        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(getContext(), android.R.layout.simple_list_item_1, notes);
                        ListView listMonth = (ListView) getView().findViewById(R.id.listMonth);
                        listMonth.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        db.close();
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



            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
