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
    private GregorianCalendar calendar;

    public Habit( String habitName ) {
        this.name = habitName;
        this.calendar = new GregorianCalendar();
    }

    public Habit( String habitName, GregorianCalendar habitCalendar ) {
        this.name = habitName;
        this.calendar = habitCalendar;
    }

    public String getName() {
        return name;
    }

    public void setName( String newName ) {
        this.name = newName;
    }

    public String getStartDate() {
        DateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd", Locale.CANADA );
        return formatter.format( calendar.getTime() );
    }

}
