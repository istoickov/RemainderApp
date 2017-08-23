package com.antovski.antonio.reminder;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Antonio on 13-Feb-17.
 */

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Button button;

    public DatePickerFragment(Button button){
        this.button = button;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String time = getArguments().getString("date");
        try {
            if (!time.equals("DATE")) {
                day = Integer.parseInt(time.substring(0, 2));
                month = Integer.parseInt(time.substring(3, 5));
                year = Integer.parseInt(time.substring(6, 10));
            }
        }catch(Exception e){}

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        button.setText(String.format("%02d/%02d/%04d",dayOfMonth, (month + 1), year));
    }
}
