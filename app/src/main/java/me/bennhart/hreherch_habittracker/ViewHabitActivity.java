package me.bennhart.hreherch_habittracker;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

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

        updateStats();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateStats();

        HabitListController hlc = new HabitListController();
        final Habit habit = hlc.getViewHabit();

        ListView completionListView = (ListView) findViewById( R.id.listView_ofCompletions );
        final ArrayList<String> habitCompletionArray = habit.getAdaptableCompletionArray();
        final ArrayAdapter<String> completionAdapter = new ArrayAdapter<String>( this,
                                                                            android.R.layout.simple_list_item_1,
                                                                            habitCompletionArray );
        completionListView.setAdapter( completionAdapter );

        habit.clearListeners();

        habit.addListener(new Listener() {
            @Override
            public void update() {
                habitCompletionArray.clear();
                habitCompletionArray.addAll( habit.getAdaptableCompletionArray() );
                completionAdapter.notifyDataSetChanged();
            }
        });

        completionListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, int position, long id) {
                // TODO http://stackoverflow.com/questions/27263008/alertdialog-with-numberpicker-rendered-incorrectly/27263520#27263520
                AlertDialog.Builder adb = new AlertDialog.Builder( ViewHabitActivity.this );
                String text = habitCompletionArray.get( position );
                String[] textSplit = text.split( " " );
                final String date = textSplit[0];
                Integer completions = Integer.parseInt( textSplit[2] );
                adb.setTitle( "Completions for: " + date );
                final NumberPicker picker = new NumberPicker( ViewHabitActivity.this );
                picker.setMinValue( 0 );
                picker.setMaxValue( 50 + completions );
                picker.setValue( completions );
                picker.setWrapSelectorWheel( false );
                FrameLayout frameParent = new FrameLayout( ViewHabitActivity.this );
                frameParent.addView( picker, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER ));
                adb.setView( frameParent );
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        habit.setHabitCompletions( date, picker.getValue() );
                        HabitListController.save();
                        updateStats();
                    }
                });
                adb.show();
                return false;
            }
        });
    }

    public void updateStats() {
        TextView completionRateView = (TextView) findViewById( R.id.textView_completionRate );
        TextView numCompletionsView = (TextView) findViewById( R.id.textView_completionNum );
        TextView numFulfilledView = (TextView) findViewById( R.id.textView_fulfilledNum );
        TextView numMissedView = (TextView) findViewById( R.id.textView_missedNum );

        HabitListController hlc = new HabitListController();
        Habit habit = hlc.getViewHabit();

        String compRate = habit.getCompletionRate() + "%";
        String numComplete = String.format(Locale.getDefault(), "%d", habit.getNumTotalCompletions() );
        String numFilled = String.format(Locale.getDefault(), "%d", habit.getNumDaysFulfilled() );
        String numMiss = String.format(Locale.getDefault(), "%d", habit.getNumDaysMissed() );

        // TODO daysMissed and fulfilled does not currently sync with daysActive, maybe intentional
        completionRateView.setText( compRate );
        numCompletionsView.setText( numComplete );
        numFulfilledView.setText( numFilled );
        numMissedView.setText( numMiss );
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
            // TODO reset habit functionality
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
                    // TODO need to error check inputs here (blank, same names)
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
