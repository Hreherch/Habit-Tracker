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
    private boolean[] dotw = { false, true, true, true, true, false, false };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        if ( getSupportActionBar() != null ) {
            getSupportActionBar().setTitle( "Add Habit" );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        }

        EditText editText_date = (EditText) findViewById( R.id.editText_date );
        editText_date.setText( GetToday.getString() );
    }

//    @SuppressWarnings( "deprecation" )
//    public void changeActive( View v ) {
//        Button b = (Button) v;
//        int id = b.getId();
//        int inactiveColor = getResources().getColor( R.color.colorInactive );
//        int activeColor = getResources().getColor( R.color.colorActive );
//        int index = -1;
//
//        if ( id == R.id.button_sunday ) {
//            index = 0;
//        } else if ( id == R.id.button_monday ) {
//            index = 1;
//        } else if ( id == R.id.button_tuesday ) {
//            index = 2;
//        } else if ( id == R.id.button_wednesday ) {
//            index = 3;
//        } else if ( id == R.id.button_thursday ) {
//            index = 4;
//        } else if ( id == R.id.button_friday ) {
//            index = 5;
//        } else if ( id == R.id.button_saturday ) {
//            index = 6;
//        } else if ( index == -1 ) {
//            throw new RuntimeException( "Unexpected call to AddHabitActivity.changeActive()" );
//        }
//
//        dotw[ index ] = !dotw[ index ];
//        b.setTextColor( dotw[ index ] ? activeColor : inactiveColor );
//    }

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
