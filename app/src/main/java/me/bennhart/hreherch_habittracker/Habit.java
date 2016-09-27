package me.bennhart.hreherch_habittracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Ben on 2016-09-22.
 */
public class Habit {
    private String name;
    private CompletionTracker completionTracker;
    private boolean[] daysActiveList = { false, false, false, false, false, false, false };
    public final int SUN = 0;
    public final int MON = 1;
    public final int TUE = 2;
    public final int WED = 3;
    public final int THU = 4;
    public final int FRI = 5;
    public final int SAT = 6;

    public Habit( String habitName ) {
        GetToday today = new GetToday();
        checkName( habitName );
        this.name = habitName;
        this.completionTracker = new CompletionTracker( today.getString() );
    }

    public Habit( String habitName, String date ) {
        checkName( habitName );
        this.name = habitName;
        try {
            this.completionTracker = new CompletionTracker(date);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException( "Invalid date (must be today or before today)." );
        }
    }

    private void checkName( String name ) {
        if ( name.length() <= 0 ) {
            throw new IllegalArgumentException( "A habit name may not be blank." );
        }
    }

    public String getName() {
        return name;
    }

    public void setName( String newName ) {
        checkName( newName );
        this.name = newName;
    }

    public String getStartDate() {
        return completionTracker.getStartDate();
    }

    public String checkNull( String date ) {
        if ( date == null ) {
            GetToday today = new GetToday();
            return today.getString();
        }
        return date;
    }

    public void addHabitCompletion( String date ) {
        date = checkNull( date );
        completionTracker.addCompletion( date );
    }

    public int getHabitCompletions( String date ) {
        date = checkNull( date );
        return completionTracker.getCompletions( date );
    }

    public void setHabitCompletions( String date, int numCompletions ) {
        date = checkNull( date );
        completionTracker.setCompletions( date, numCompletions );
    }

    public void setActive( boolean[] dotwList ) {
        if ( dotwList.length != 7 ) {
            String message = "This boolean[] must have 7 elements only";
            throw new IllegalArgumentException( message );
        }
        daysActiveList = dotwList;
    }

    private void checkDOTWIndex( int index ) {
        if ( index < 0 || 7 < index ) {
            String message = index + "Is not a valid day number. Use habit.XXX for ease of use.";
            throw new IllegalArgumentException( message );
        }
    }

    public void setActive( int day, boolean active ) {
        checkDOTWIndex( day );
        daysActiveList[ day ] = active;
    }

    public boolean isActiveOn( int day ) {
        checkDOTWIndex( day );
        return daysActiveList[ day ];
    }

    public boolean isActiveToday() {
        GregorianCalendar calendar = new GregorianCalendar();
        int dotw = calendar.get( Calendar.DAY_OF_WEEK ) - 1;
        return isActiveOn( dotw );
    }

}
