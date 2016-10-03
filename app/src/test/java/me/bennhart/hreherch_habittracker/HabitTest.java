package me.bennhart.hreherch_habittracker;

import android.app.DatePickerDialog;

import junit.framework.TestCase;
import org.junit.Test;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        Habit aHabit = new Habit(habitName);
        assertEquals("testHabitName: new Habit object lacks invoked name!",
                habitName, aHabit.getName());

        habitName = "new Name";
        assertNotEquals("testHabitName: new habit name matches!",
                habitName, aHabit.getName());

        aHabit.setName(habitName);
        assertEquals("testHabitName: reassigned habit name not equal!",
                habitName, aHabit.getName());
    }

    public void testHabitDate() {
        GetToday getToday = new GetToday();
        String habitName = "dateHabit";
        Habit aHabit = new Habit(habitName);
        GregorianCalendar calendar = new GregorianCalendar();
        DateFormat formatter = new SimpleDateFormat(getToday.getDateFormat(),
                Locale.getDefault());

        assertEquals("testHabitDate: Habit date format does not match expected format!",
                formatter.format(calendar.getTime()), aHabit.getStartDate());
    }

    public void testHabitDateConstructor() {
        GetToday getToday = new GetToday();
        String habitName = "myHabit";
        GregorianCalendar calendar = new GregorianCalendar(0, 2, 14);
        DateFormat formatter = new SimpleDateFormat(getToday.getDateFormat(),
                Locale.getDefault());

        Habit aHabit = new Habit(habitName, formatter.format(calendar.getTime()));

        assertEquals("testHabitDateConstructor: init calendar date is not the same as habit's!",
                formatter.format(calendar.getTime()), aHabit.getStartDate());
    }

    // testing implementation of CompletionTracker in Habit
    public void testInitHabitWithCompletions() {
        GetToday getToday = new GetToday();
        String habitName = "completion_habit";
        String today = getToday.getString();
        Habit myHabit = new Habit(habitName, today);
        assertEquals("testInitHabitWithCompletions: must initialize with zero completions",
                0, myHabit.getHabitCompletions(today));

        assertEquals("testInitHabitWithCompletions: must be initialized with given date",
                getToday.getString(), myHabit.getStartDate());

        assertEquals("testInitHabitWithCompletions: must return -1 on future day completions",
                -1, myHabit.getHabitCompletions(getToday.getTodayPlus(2)));

        assertEquals("testInitHabitWithCompletions: must return -1 on days before start date",
                -1, myHabit.getHabitCompletions(getToday.getTodayPlus(-2)));

    }

    public void testAddHabitCompletions() {
        GetToday getToday = new GetToday();
        String date = getToday.getTodayPlus(-7);
        String theDate = date;
        Habit myHabit = new Habit("aName", date);
        int completions[] = {1, 0, 1, 4, 2, 3};
        for (int numCompletions : completions) {
            for (int i = 0; i < numCompletions; i++) {
                myHabit.addHabitCompletion(theDate);
            }
            assertEquals("testAddHabitCompletions: number of completions does not equal input",
                    numCompletions, myHabit.getHabitCompletions(theDate));
            theDate = getToday.getDatePlus(theDate, 1);
        }
        assertEquals("testAddHabitCompletions: A valid day with no completions returns 0",
                0, myHabit.getHabitCompletions(getToday.getDatePlus(date, 1)));
    }

    public void testSetHabitCompletions() {
        GetToday today = new GetToday();
        String date = today.getTodayPlus(-7);
        Habit habit = new Habit("selectionTest", date);

        String date2 = today.getDatePlus(date, -2);
        try {
            habit.setHabitCompletions(date2, 2);
            String message = "testSetHabitCompletions: You may not track "
                    + "completions before habit start date";
            assertTrue(message, false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        date2 = today.getDatePlus(date, 2);
        try {
            habit.setHabitCompletions(date2, -1);
            String message = "testSetHabitCompletions: You may not assign completions "
                    + "to be less than 0.";
            assertTrue(message, false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        date2 = today.getDatePlus(date, 15);
        try {
            habit.setHabitCompletions(date2, 3);
            String message = "testSetHabitCompletions: You may not assign completions "
                    + "to a date in the future.";
            assertTrue(message, false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        date2 = today.getDatePlus(date, 4);
        habit.setHabitCompletions(date2, 3);

        assertEquals("testSetHabitCompletions: this date's completions was set to 3.",
                3, habit.getHabitCompletions(date2));

    }

    public void testHabitNullValues() {
        GetToday today = new GetToday();
        Habit myHabit = new Habit("nullTest", today.getTodayPlus(-3));

        myHabit.addHabitCompletion(null);
        assertEquals("testHabitNullValue: null addCompletion should add completion to today.",
                1, myHabit.getHabitCompletions(today.getString()));

        assertEquals("testHabitNullValue: null getCompletions should return today's completions.",
                1, myHabit.getHabitCompletions(null));

        myHabit.setHabitCompletions(null, 5);
        assertEquals("testHabitNullValue: null setCompletions should set Today's completions.",
                5, myHabit.getHabitCompletions(null));
        ;
    }

    // test if initial day is false
    public void testHabitStartsInactive() {
        GetToday today = new GetToday();
        Habit habit = new Habit("testMe");
        assertFalse("testMeditateCase: habits should initialize false",
                habit.isActiveToday());
    }

    // test .setActive() can set all to true
    public void testSettingList() {
        GetToday today = new GetToday();
        Habit habit = new Habit("Meditate", today.getString());
        boolean[] dotwList = {true, true, true, true, true, true, true};
        habit.setActive(dotwList);

        assertTrue("testSettingList: habit is active every day of week, this should return true",
                habit.isActiveToday());

        assertTrue("testSettingList: habit is active every day of week, this should return true",
                habit.isActiveOn(habit.SUN));

        assertTrue("testSettingList: habit is active every day of week, this should return true",
                habit.isActiveOn(habit.MON));

        assertTrue("testSettingList: habit is active every day of week, this should return true",
                habit.isActiveOn(habit.TUE));

        assertTrue("testSettingList: habit is active every day of week, this should return true",
                habit.isActiveOn(habit.WED));

        assertTrue("testSettingList: habit is active every day of week, this should return true",
                habit.isActiveOn(habit.THU));

        assertTrue("testSettingList: habit is active every day of week, this should return true",
                habit.isActiveOn(habit.FRI));

        assertTrue("testSettingList: habit is active every day of week, this should return true",
                habit.isActiveOn(habit.SAT));
    }

    // Test that isActiveToday gets the right day of the week
    public void testActiveToday() {
        GregorianCalendar calendar = new GregorianCalendar();
        DateFormat formatter = new SimpleDateFormat( "EEE", Locale.getDefault() );
        Habit myHabit = new Habit( "aHabit" );
        int dotw = -1;

        String day = formatter.format( calendar.getTime() );
        System.out.print( day );
        switch( day )  {
            case "Sun":
                dotw = myHabit.SUN;
                break;
            case "Mon":
                dotw = myHabit.MON;
                break;
            case "Tue":
                dotw = myHabit.TUE;
                break;
            case "Wed":
                dotw = myHabit.WED;
                break;
            case "Thu":
                dotw = myHabit.THU;
                break;
            case "Fri":
                dotw = myHabit.FRI;
                break;
            case "Sat":
                dotw = myHabit.SAT;
                break;
        }

        myHabit.setActive( dotw, true );
        assertTrue( "testActiveToday: habit should be active",
                    myHabit.isActiveToday() );
    }

    // Test that set active( day, bool ) correctly sets that day.
    public void testSetActive() {
        Habit myHabit = new Habit( "helpme" );

        myHabit.setActive( myHabit.MON, true );

        assertTrue( "testSetActive: Mondays were set active.",
                     myHabit.isActiveOn( myHabit.MON ) );
    }

    public void testWrongDates() {
        String date = "2016-09-00";
        try {
            Habit myHabit = new Habit("why?", date );
            assertTrue( "Habit should throw error on dates like 2016-09-00", false );
        } catch ( Exception e ) {
            assertTrue( true );
        }
    }
}
