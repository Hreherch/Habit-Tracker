package me.bennhart.hreherch_habittracker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Ben on 2016-09-24.
 */
public class GetToday {
    private final String DATE_FORMAT = "yyyy-MM-dd";

    public String getString() {
        GregorianCalendar calendar = new GregorianCalendar();
        DateFormat formatter = new SimpleDateFormat( DATE_FORMAT, Locale.getDefault() );
        return formatter.format( calendar.getTime() );
    }

    public String getTodayPlus( int plusDays ) {
        GregorianCalendar calendar = new GregorianCalendar();
        DateFormat formatter = new SimpleDateFormat( DATE_FORMAT, Locale.getDefault() );
        calendar.add( Calendar.DATE, plusDays );
        return formatter.format( calendar.getTime() );
    }

    public String getDatePlus( String date, int plusDays ) {
        DateFormat formatter = new SimpleDateFormat( DATE_FORMAT, Locale.getDefault() );
        GregorianCalendar calendar = new GregorianCalendar();
        try {
            calendar.setTime( formatter.parse( date ) );
        } catch ( ParseException e ) {
            String message = date + " is not a valid date. Ensure format: " + DATE_FORMAT;
            throw new IllegalArgumentException( message );
        }
        calendar.add( Calendar.DATE, plusDays );
        return formatter.format( calendar.getTime() );
    }

    public String getDayName() {
        GregorianCalendar calendar = new GregorianCalendar();
        DateFormat formatter = new SimpleDateFormat( "EEEE", Locale.getDefault() );
        return formatter.format( calendar.getTime() );
    }

    public String getDateFormat() {
        return DATE_FORMAT;
    }
}
