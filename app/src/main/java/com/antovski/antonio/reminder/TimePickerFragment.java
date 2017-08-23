package com.antovski.antonio.reminder;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Antonio on 13-Feb-17.
 */

@SuppressLint("ValidFragment")
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    Button button;

    public TimePickerFragment(Button button){
        this.button = button;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        try {
            String time = getArguments().getString("time");
            if (!time.equals("TIME")) {
                hour = Integer.parseInt(time.substring(0, 2));
                minute = Integer.parseInt(time.substring(3, 5));
            }
        }catch (Exception e){

        }

        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        button.setText(String.format("%02d:%02d", hourOfDay, minute));
    }
}
