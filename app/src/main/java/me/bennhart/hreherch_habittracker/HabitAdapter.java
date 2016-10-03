package me.bennhart.hreherch_habittracker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ben on 2016-09-30.
 */
public class HabitAdapter extends ArrayAdapter<Habit> {
    private ArrayList<Habit> habitList;

    public HabitAdapter(Context context, int textViewResourceId, ArrayList<Habit> habitArrayList ) {
        super( context, textViewResourceId, habitArrayList );
        this.habitList = habitArrayList;
    }

    public View getView( int position, View v, ViewGroup parent ) {
        if ( v == null ) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            v = inflater.inflate( R.layout.habitlist_item, null );
        }

        final Habit habit = habitList.get( position );

        if ( habit != null ) {
            TextView habitName = (TextView) v.findViewById( R.id.textView_habitName );
            TextView completions = (TextView) v.findViewById( R.id.textView_numCompletions );
            ImageButton completeButton = (ImageButton) v.findViewById( R.id.imageButton_addCompletionButton );
            LinearLayout dotw_layout = (LinearLayout) v.findViewById( R.id.linearLayout_dotw );
            int numComplete = habit.getHabitCompletions( null );

            if ( habitName != null ) {
                habitName.setText( habit.getName() );
            }
            if ( completions != null ) {
                String text = "x" + numComplete;
                completions.setText( text );
            }
            if ( completeButton != null ) {
                completeButton.setTag( position );
                if ( habit.isActiveToday() ) {
                    if (numComplete <= 0) {
                        completeButton.setImageResource( R.drawable.ic_priority_high_black );
                        completeButton.setBackgroundResource( R.color.colorActive );
                    } else { // numComplete > 0
                        completeButton.setImageResource(R.drawable.ic_done_black );
                        completeButton.setBackgroundResource( R.color.colorActive );
                    }
                } else { // (habit is not active today)
                    if (numComplete <= 0) {
                        completeButton.setImageResource( R.drawable.ic_snooze_black );
                        completeButton.setBackgroundResource( R.color.colorInactive );
                    } else { // numComplete > 0
                        completeButton.setImageResource( R.drawable.ic_done_black );
                        completeButton.setBackgroundResource( R.color.colorInactive );
                    }
                }

                completeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        HabitListController habitListController = new HabitListController();
                        habitListController.addCompletionToday( position );
                        Toast.makeText( v.getContext(), "Completion added to: " + habit.getName(), Toast.LENGTH_SHORT ).show();
                    }
                });

                for ( int i = 0; i < 7; i++ ) {
                    TextView dotw_text = (TextView) dotw_layout.getChildAt( i );
                    boolean active = habit.isActiveOn( i );
                    int inactiveCol = ContextCompat.getColor( v.getContext(), R.color.colorInactive );
                    int activeCol = ContextCompat.getColor( v.getContext(), R.color.colorActive );
                    dotw_text.setTextColor( active ? activeCol : inactiveCol );
                }
            }
        }
        return v;
    }
}
