package me.bennhart.hreherch_habittracker;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class AddHabitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        if ( getSupportActionBar() != null ) {
            getSupportActionBar().setTitle( "Add Habit" );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        }

        GetToday today = new GetToday();

        EditText editText_date = (EditText) findViewById( R.id.editText_date );
        editText_date.setText( today.getString() );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if ( id == android.R.id.home ) {
            this.finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
