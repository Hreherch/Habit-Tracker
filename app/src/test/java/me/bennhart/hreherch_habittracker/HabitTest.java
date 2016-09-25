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
    //private final String DATE_FORMAT = "yyyy-MM-dd";

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
        DateFormat formatter = new SimpleDateFormat( GetToday.DATE_FORMAT, Locale.getDefault() );

        assertEquals( "testHabitDate: Habit date format does not match expected format!",
                formatter.format( calendar.getTime() ) , aHabit.getStartDate() );
    }

    public void testHabitDateConstructor() {
        String habitName = "myHabit";
        GregorianCalendar calendar = new GregorianCalendar( 0, 2, 14 );
        DateFormat formatter = new SimpleDateFormat( GetToday.DATE_FORMAT, Locale.getDefault() );

        Habit aHabit = new Habit( habitName, formatter.format( calendar.getTime() ) );

        assertEquals( "testHabitDateConstructor: init calendar date is not the same as habit's!",
                formatter.format( calendar.getTime() ), aHabit.getStartDate() );
    }

    // testing implementation of CompletionTracker in Habit
    public void testInitHabitWithCompletions() {
        String habitName = "completion_habit";
        String today = GetToday.getString();
        Habit myHabit = new Habit( habitName, today );
        assertEquals( "testInitHabitWithCompletions: must initialize with zero completions",
                      0, myHabit.getHabitCompletions( today ) );

        assertEquals( "testInitHabitWithCompletions: must be initialized with given date",
                      GetToday.getString(), myHabit.getStartDate() );

        assertEquals( "testInitHabitWithCompletions: must return -1 on future day completions",
                      -1, myHabit.getHabitCompletions( GetToday.getStringPlus( 2 ) ) );

        assertEquals( "testInitHabitWithCompletions: must return -1 on days before start date",
                      -1, myHabit.getHabitCompletions( GetToday.getStringPlus( -2 ) ) );

    }

    public void testAddHabitCompletions() {
        String date = GetToday.getStringPlus( -7 );
        String theDate = date;
        Habit myHabit = new Habit( "aName", date );
        int completions[] = { 1, 0, 1, 4, 2, 3 };
        for ( int numCompletions : completions ) {
            for ( int i = 0; i < numCompletions; i++ ) {
                myHabit.addHabitCompletion( theDate );
            }
            assertEquals( "testAddHabitCompletions: number of completions does not equal input",
                          numCompletions, myHabit.getHabitCompletions( theDate ) );
            theDate = GetToday.getDatePlus( theDate, 1 );
        }
        assertEquals( "testAddHabitCompletions: A day with no completions returns 0",
                      0, myHabit.getHabitCompletions( GetToday.getDatePlus( date, 1 ) ) );
    }
}
