//        Copyright (C) 2016 Bennett Hreherchuk hreherch@ualberta.ca
//
//        This program is free software: you can redistribute it and/or modify
//        it under the terms of the GNU General Public License as published by
//        the Free Software Foundation, either version 3 of the License, or
//        (at your option) any later version.
//
//        This program is distributed in the hope that it will be useful,
//        but WITHOUT ANY WARRANTY; without even the implied warranty of
//        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//        GNU General Public License for more details.
//
//        You should have received a copy of the GNU General Public License
//        along with this program.  If not, see <http://www.gnu.org/licenses/>.

package me.bennhart.hreherch_habittracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AddHabitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Habit");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        GetToday today = new GetToday();
        final TextView textView_date = (TextView) findViewById(R.id.editText_date);
        textView_date.setText(today.getString());
    }

    public void calendarDialogue(View v) {
        AlertDialog.Builder adb = new AlertDialog.Builder(AddHabitActivity.this);
        adb.setTitle("Pick the Start Date");

        final DatePicker calendarPicker = new DatePicker(AddHabitActivity.this);
        calendarPicker.setMaxDate(System.currentTimeMillis());

        FrameLayout frameParent = new FrameLayout(AddHabitActivity.this);
        frameParent.addView(calendarPicker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));
        adb.setView(frameParent);

        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GetToday today = new GetToday();
                TextView textView_date = (TextView) findViewById(R.id.editText_date);
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.set(calendarPicker.getYear(), calendarPicker.getMonth(), calendarPicker.getDayOfMonth());
                DateFormat formatter = new SimpleDateFormat(today.getDateFormat(), Locale.getDefault());
                textView_date.setText(formatter.format(calendar.getTime()));
            }
        });

        adb.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public boolean[] getCheckedDays() {
        int[] buttonIds = {R.id.button_sunday, R.id.button_monday, R.id.button_tuesday,
                R.id.button_wednesday, R.id.button_thursday, R.id.button_friday,
                R.id.button_saturday};
        boolean[] dotw = {false, false, false, false, false, false, false};

        for (int i = 0; i < buttonIds.length; i++) {
            ToggleButton button = (ToggleButton) findViewById(buttonIds[i]);
            dotw[i] = button.isChecked();
        }

        return dotw;
    }

    public void addHabit(View v) {
        HabitListController controller = new HabitListController();

        EditText editText_habitName = (EditText) findViewById(R.id.editText_habitName);
        String habitName = editText_habitName.getText().toString();

        TextView textView_date = (TextView) findViewById(R.id.editText_date);

        String error = "";
        error = controller.addHabit(habitName,
                textView_date.getText().toString(),
                getCheckedDays());

        if (error != null) {
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Habit: " + habitName + " added.", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    public void toastDateChange(View view) {
        Toast.makeText(AddHabitActivity.this, "Click the calender button to set the date", Toast.LENGTH_SHORT).show();
    }
}
