package me.bennhart.hreherch_habittracker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ben on 2016-09-23.
 */
public class CompletionTracker {
    private List<String> dates = new ArrayList<>();
    private List<Integer> completions = new ArrayList<>();
    private String startDate = "0000-00-00";
    private final String DATE_FORMAT = "yyyy-MM-dd";

    public CompletionTracker( String date ) {
        date = date.trim();
        validateDate( date );
        startDate = date;
        setCompletions( date, 0 );
    }

    private void validateDate( String date ) throws IllegalArgumentException {
        // TODO create a class that extends string and returns today's date with format?
        GregorianCalendar calendar = new GregorianCalendar();
        DateFormat formatter = new SimpleDateFormat( DATE_FORMAT, Locale.getDefault() );
        String today = formatter.format( calendar.getTime() );

        // Check if date is ahead of today
        if ( today.compareTo( date ) < 0 ) {
            String message = "You may not add a completion to a date in the future";
            throw new IllegalArgumentException( message );
        }

        // Check if date is behind startDate
        if ( startDate.compareTo( date ) > 0 ) {
            String message = "You may not add a completion to a date before the initial date";
            throw new IllegalArgumentException( message );
        }

        // Catch invalid date and improper format
        try {
            formatter.parse( date );
        } catch ( ParseException e ) {
            String message = date + " is not a valid date. Ensure format: " + DATE_FORMAT;
            throw new IllegalArgumentException( message );
        }
    }

    public void addCompletion( String date ) {
        date = date.trim();
        validateDate( date );

        if ( dates.contains( date ) ) {
            int index = dates.indexOf( date );
            completions.set( index, completions.get( index ) + 1  );
        } else {
            dates.add( date );
            int index = dates.indexOf( date );
            completions.add( index, 1 );
        }
    }

    // returns -1 if date is before start range
    public int getCompletions( String date ) {
        date = date.trim();

        if ( 0 < startDate.compareTo( date ) ) {
            return -1;
        }

        if ( dates.contains( date ) ) {
            return completions.get( dates.indexOf( date ) );
        } else {
            return 0;
        }
    }

    public void setCompletions( String date, int numCompletions ) {
        date = date.trim();
        validateDate( date );

        if ( dates.contains( date ) )  {
            completions.set( dates.indexOf( date ), numCompletions );
        } else {
            addCompletion( date );
            completions.set( dates.indexOf( date ), numCompletions );
        }
    }

}
