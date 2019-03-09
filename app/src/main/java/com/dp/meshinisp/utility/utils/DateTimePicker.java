package com.dp.meshinisp.utility.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import com.dp.meshinisp.view.ui.callback.OnDateTimeSelected;

import java.util.Calendar;

public class DateTimePicker  {

    String selectedTime;
    String selectedDate;
    Context context;
    OnDateTimeSelected onDateTimeSelected;
    private static DateTimePicker instance;

    private DateTimePicker() {
    }

    public static DateTimePicker getInstance() {
        if(instance==null){
            instance=new DateTimePicker();
        }
        return instance;
    }

    public void showTimePickerDialog() {
        Calendar mCuurTime = Calendar.getInstance();
        int hour = mCuurTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCuurTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, (view, hourOfDay, minute1) -> {
            selectedTime = ((hourOfDay < 10 ? "0" + hourOfDay : String.valueOf(hourOfDay)) + ":" +
                    (minute1 < 10 ? "0" + minute1 : minute1) + ":00");
            onDateTimeSelected.onDateTimeReady(selectedDate,selectedTime);
        }, hour, minute, true);
        //mTimePicker.setTitle(context.getString(R.string.select_time));
        mTimePicker.show();
    }

    public void showDatePickerDialog(Context context, OnDateTimeSelected onDateTimeSelected) {
        this.context=context;
        this.onDateTimeSelected=onDateTimeSelected;
        Calendar mCuurDate = Calendar.getInstance();
        int year = mCuurDate.get(Calendar.YEAR);
        int month = mCuurDate.get(Calendar.MONTH) + 1;
        int day = mCuurDate.get(Calendar.DAY_OF_WEEK);
        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(context, (view, year1, month1, dayOfMonth) -> {
            selectedDate = String.valueOf(year1) + "-" + (month1 < 10 ? "0" + month1 : month1) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
            System.out.println("Date on utils : " + selectedDate);
            //listener.onDateSet(selectedDate);
            showTimePickerDialog();
        }, year, month, mCuurDate.get(Calendar.DATE));
        //mDatePicker.setTitle(context.getString(R.string.select_date));
        mDatePicker.show();
    }
}
