package org.ghost;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

//    private static final String dateSeparator = "-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
    }

    private void findViews() {
//                dateChooser = (DatePicker) findViewById(R.id.dateChooser);
//                timeChooser = (TimePicker) findViewById(R.id.timeChooser);
    }
}

/*
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }
    }


*/

// sqlite 自動搜尋器



// ======================= 這裡以下的代表作業已經完成 =======================

    //    private DatePicker dateChooser;
    //    private TimePicker timeChooser;

//    private EditText date;
//    private EditText time;
//    private DatePickerDialog datePickerDialog;
//    private TimePickerDialog timePickerDialog;

//    private CalendarView calendarChooser;
//    private TextView changedDate;



//    private void findViews() {
//
//        date = (EditText) findViewById(R.id.date);
//        date.setOnClickListener(this::choiceDate);
//
//        time = (EditText) findViewById(R.id.time);
//        time.setOnClickListener(this::choiceTime);
//
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int date = calendar.get(Calendar.DATE);
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//
//        datePickerDialog = new DatePickerDialog(this,
//                android.R.style.Theme_Material_Dialog_Alert,
//                this::onDateChoice,
//                year, month, date);
//
//        timePickerDialog = new TimePickerDialog(this,
//                android.R.style.Theme_Material_Dialog_Alert,
//                this::onTimeChoice,
//                hour, minute, true);
//
//        calendarChooser = (CalendarView) findViewById(R.id.calendarChooser);
//        calendarChooser.setOnDateChangeListener(this::onDateChange);
//
//        changedDate = (TextView) findViewById(R.id.changedDate);
//    }

//
//    private void choiceDate(View view) {
//        datePickerDialog.show();
//    }
//
//    private void choiceTime(View view) {
//        timePickerDialog.show();
//    }

//    private void onDateChoice(DatePicker datePicker, int year, int month, int date) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year, month, date);
//        Date dateToString = calendar.getTime();
//
//        String dateString = simpleDateFormat.format(dateToString);
//        this.date.setText(dateString);
//    }
//
//    private void onTimeChoice(TimePicker timePicker, int hourOfDate, int minute) {
//        this.time.setText(hourOfDate + dateSeparator + minute);
//    }

//    private void onDateChange(CalendarView calendarView, int year, int month, int date) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year, month, date);
//
//        Date dateToString = calendar.getTime();
//        changedDate.setText(simpleDateFormat.format(dateToString));
//    }