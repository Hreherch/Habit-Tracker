package me.bennhart.hreherch_habittracker;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

public class ViewHabitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        HabitListController habitListController = new HabitListController();
        Habit habit = habitListController.getViewHabit();

        if ( getSupportActionBar() != null ) {
            getSupportActionBar().setTitle( habit.getName() );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        }

        RelativeLayout dotw_layout = (RelativeLayout) findViewById( R.id.relativeLayout_dotwLayout );
        for ( int i = 0; i < 7; i++ ) {
            ToggleButton b = (ToggleButton) dotw_layout.getChildAt( i );
            b.setChecked( habit.isActiveOn( i ) );
        }
    }

    @Override
    public void onBackPressed() {
        HabitListController habitListController = new HabitListController();
        Habit habit = habitListController.getViewHabit();
        final String habitName = habit.getName();
        final Activity activity = this;

        boolean[] newActiveList = { false, false, false, false, false, false, false };
        RelativeLayout dotw_layout = (RelativeLayout) findViewById( R.id.relativeLayout_dotwLayout );
        for ( int i = 0; i < 7; i++ ) {
            ToggleButton b = (ToggleButton) dotw_layout.getChildAt( i );
            newActiveList[ i ] = b.isChecked();
        }

        habitListController.setHabitActives( habitName, newActiveList );
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_view_habit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        HabitListController habitListController = new HabitListController();
        Habit habit = habitListController.getViewHabit();
        final String habitName = habit.getName();
        final Activity activity = this;

        if ( id == android.R.id.home ) {
            onBackPressed();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_resetHabit) {
            return true;
        }

        if (id == R.id.action_editName) {
            AlertDialog.Builder adb = new AlertDialog.Builder( ViewHabitActivity.this );
            adb.setTitle( "Set Habit's Name:" );
            final EditText editText_habitName = new EditText( ViewHabitActivity.this );
            final ActionBar actBar = this.getSupportActionBar();
            editText_habitName.setText( habitName );
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter( 20 );
            editText_habitName.setFilters( filterArray );
            editText_habitName.setSingleLine( true );
            adb.setView( editText_habitName );
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HabitListController hbc = new HabitListController();
                    String newName = editText_habitName.getText().toString();
                    hbc.setViewHabitName( newName );
                    if (actBar != null) {
                        actBar.setTitle(newName);
                    }
                }
            });
            adb.show();
            return true;
        }

        if (id == R.id.action_deleteHabit) {
            AlertDialog.Builder adb = new AlertDialog.Builder( ViewHabitActivity.this );
            adb.setMessage( "Delete " + habit.getName() + "?" );
            adb.setCancelable( true );
            adb.setPositiveButton( "Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HabitListController hbc = new HabitListController();
                    hbc.removeHabit( habitName );
                    activity.finish();
                }
            });
            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //pass
                }
            });
            adb.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
