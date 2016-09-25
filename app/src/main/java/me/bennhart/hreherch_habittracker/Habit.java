package me.bennhart.hreherch_habittracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    public Habit( String habitName ) {
        this.name = habitName;
        this.completionTracker = new CompletionTracker( GetToday.getString() );
    }

    public Habit( String habitName, String date ) {
        this.name = habitName;
        this.completionTracker = new CompletionTracker( date );
    }

    public String getName() {
        return name;
    }

    public void setName( String newName ) {
        this.name = newName;
    }

    public String getStartDate() {
        return completionTracker.getStartDate();
    }

    public String checkNull( String date ) {
        if ( date == null ) {
            return GetToday.getString();
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

}
