package me.bennhart.hreherch_habittracker;

import junit.framework.TestCase;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Created by Ben on 2016-09-22.
 * For test-driven development
 */

public class HabitTest extends TestCase {
    private final String DATE_FORMAT = "yyyy-MM-dd";

    public void testHabitName() {
        String habitName = "my habit";
        Habit aHabit = new Habit( habitName );
        assertEquals( "testHabitName: new Habit object lacks invoked name!",
                habitName, aHabit.getName() );

        habitName = "new Name";
        assertNotEquals( "testHabitName: new habit name matches!",
                habitName, aHabit.getName() );

        aHabit.setName( habitName );
        assertEquals( "testHabitName: reassigned habit name not equal!",
                habitName, aHabit.getName());
    }

    public void testHabitDate() {
        String habitName = "dateHabit";
        Habit aHabit = new Habit( habitName );
        GregorianCalendar calendar = new GregorianCalendar();

        DateFormat formatter = new SimpleDateFormat( DATE_FORMAT, Locale.getDefault() );


        assertEquals( "testHabitDate: Habit date format does not match expected format!",
                formatter.format( calendar.getTime() ) , aHabit.getStartDate() );
    }

    public void testHabitDateConstructor() {
        String habitName = "myHabit";
        GregorianCalendar calendar = new GregorianCalendar( 0, 2, 14 );
        Habit aHabit = new Habit( habitName, calendar );

        DateFormat formatter = new SimpleDateFormat( DATE_FORMAT, Locale.getDefault() );

        assertEquals( "testHabitDateConstructor: init calendar date is not the same as habit's!",
                formatter.format( calendar.getTime() ), aHabit.getStartDate() );
    }
}
