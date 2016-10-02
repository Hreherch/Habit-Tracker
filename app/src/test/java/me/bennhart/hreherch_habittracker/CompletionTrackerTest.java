package me.bennhart.hreherch_habittracker;

/**
 * Created by Ben on 2016-09-23.
 */

import static org.junit.Assert.*;
import junit.framework.TestCase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

public class CompletionTrackerTest extends TestCase {
    private final String DATE_FORMAT = "yyyy-MM-dd";

    public void testCompletionAddition() {
        CompletionTracker tracker = new CompletionTracker( "2016-09-22" );
        tracker.addCompletion( "2016-09-23" );
        assertEquals( "testCompletionAddition: Failed to add/get a completion",
                      1, tracker.getCompletions( "2016-09-23" ) );
    } // testCompletionAddition

    public void testHandleInvalidDate() {
        CompletionTracker tracker = new CompletionTracker( "2016-09-21" );
        GregorianCalendar calendar = new GregorianCalendar();
        DateFormat formatter = new SimpleDateFormat( DATE_FORMAT, Locale.getDefault() );
        String date = "";

        calendar.add( Calendar.DATE, 1 );
        date = formatter.format( calendar.getTime() );
        try {
            tracker.addCompletion(date);
            assertFalse( "testHandleInvalidDate: did not handle invalid date", true );
        } catch (IllegalArgumentException e) {
            assertTrue( true );
        }
    } // testHandleInvalidDate

    public void testRandomAdditions() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add( Calendar.DATE, -7 );
        DateFormat formatter = new SimpleDateFormat( DATE_FORMAT, Locale.getDefault() );
        String lastWeek = formatter.format( calendar.getTime() );
        Random random = new Random();
        CompletionTracker tracker = new CompletionTracker( lastWeek );
        int completions[] = { 0, 0, 0, 0, 0, 0, 0, 0 };

        for ( int i = 0; i < 20; i++ ) {
            int randomNum = Math.abs( random.nextInt() % 7 );

            calendar.add( Calendar.DATE, randomNum );
            String date = formatter.format( calendar.getTime() );
            tracker.addCompletion( date );
            completions[randomNum]++;
            calendar.add( Calendar.DATE, -randomNum );
        }

        for ( int i : completions ) {
            String date = formatter.format( calendar.getTime() );
            assertEquals( "testRandomAdditions: the expected number of additions does not match",
                           i, tracker.getCompletions( date ) );
            calendar.add( Calendar.DATE, 1 );
        }
    } // testRandomAdditions

    public void testSettingCompletions() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add( Calendar.DATE, -3 );
        DateFormat formatter = new SimpleDateFormat( DATE_FORMAT, Locale.getDefault() );
        String date = formatter.format( calendar.getTime() );
        CompletionTracker tracker = new CompletionTracker( date );
        int negativeNumber = -1;
        int num2 = 5;
        int num3 = 2;

        try {
            tracker.setCompletions( date, negativeNumber );
            assertFalse( "testSettingCompletions: should not be able to set below zero.", true );
        } catch ( IllegalArgumentException e ) {
            assertTrue( true );
        }

        calendar.add( Calendar.DATE, 2 );
        String date2 = formatter.format( calendar.getTime() );
        tracker.setCompletions( date2, num2 );

        calendar.add( Calendar.DATE, 10 );
        String date3 = formatter.format( calendar.getTime() );
        try {
            tracker.setCompletions( date3, num3 );
            assertFalse( "testSettingCompletions: setter did not check for date in future", true );
        } catch ( IllegalArgumentException e ) {
            assertTrue( true );
        }

        assertEquals( "testSettingCompletions: setter did not set negative completions correctly",
                      0, tracker.getCompletions( date ) );

        assertEquals( "testSettingCompletions: setter did not set regular completions correctly",
                      tracker.getCompletions( date2 ), num2 );

        assertEquals( "testSettingCompletions: setter did not set future completions correctly",
                      -1, tracker.getCompletions( date3 ) );

    } // testSettingCompletions

    public void testGetNumDaysTracked() {
        GetToday today = new GetToday();
        CompletionTracker tracker = new CompletionTracker( today.getString() );

        assertEquals( "testGetNumDaysTracked: Should return 1 on day one.",
                      1, tracker.getNumDaysTracked() );

        CompletionTracker secondTracker = new CompletionTracker( today.getTodayPlus( -1 ) );

        assertEquals( "testGetNumDaysTracked: Should return 2 on day two.",
                      2, secondTracker.getNumDaysTracked() );

    }

    public void testGetNumTotalCompletions() {
        GetToday today = new GetToday();
        CompletionTracker tracker = new CompletionTracker( today.getTodayPlus( -7 ) );

        assertEquals( "CompletionTracker should init with 0 total completions",
                      0, tracker.getNumTotalCompletions() );

        tracker.addCompletion( today.getString() );
        assertEquals( "CompletionTracker did not add a completion to the total.",
                      1, tracker.getNumTotalCompletions() );

        tracker.setCompletions( today.getTodayPlus( -4 ), 5 );
        assertEquals( "CompletionTracker did not add a set completion to the total.",
                6, tracker.getNumTotalCompletions() );

        tracker.setCompletions( today.getString(), 0 );
        assertEquals( "CompletionTracker did allow removal of completion to update the total.",
                5, tracker.getNumTotalCompletions() );
    }

    public void testGetNumDaysFulfilled() {
        GetToday today = new GetToday();
        CompletionTracker tracker = new CompletionTracker( today.getTodayPlus( -7 ) );

        assertEquals( "CompletionTracker should init with no days fulfilled",
                0, tracker.getNumDaysFulfilled() );

        tracker.addCompletion( today.getTodayPlus( -4 ) );
        assertEquals( "CompletionTracker did not update fulfilled days after adding completion",
                1, tracker.getNumDaysFulfilled() );

        tracker.setCompletions( today.getTodayPlus( -5 ), 3 );
        assertEquals( "CompletionTracker did not update fulfilled days after setting completion",
                2, tracker.getNumDaysFulfilled() );

        tracker.setCompletions( today.getTodayPlus( -5 ), 0 );
        assertEquals( "CompletionTracker a fulfillment account for days with 0 completions",
                1, tracker.getNumDaysFulfilled() );
    }

    public void testGetNumDaysMissed() {
        GetToday today = new GetToday();
        CompletionTracker tracker = new CompletionTracker( today.getString() );

        assertEquals( "CompletionTracker should init with no days missed (if started today)",
                      0, tracker.getNumDaysMissed() );

        tracker.addCompletion( today.getString() );
        assertEquals( "CompletionTracker should return 0 missed if today=startDay and completions>0",
                      0, tracker.getNumDaysMissed() );

        CompletionTracker otherTracker = new CompletionTracker( today.getTodayPlus( -1 ) );
        assertEquals( "CompletionTracker should init with 1 days missed (if started yesterday)",
                      1, otherTracker.getNumDaysMissed() );

        otherTracker.addCompletion( today.getString() );
        assertEquals( "CompletionTracker should have missed=1 if start=yesterday and todayCompletions>0",
                1, otherTracker.getNumDaysMissed() );

        otherTracker.addCompletion( today.getTodayPlus( -1 ) );
        assertEquals( "CompletionTracker should have missed=1 if all days have completions",
                0, otherTracker.getNumDaysMissed() );
    }

    public void testGetCompletionRate() {
        GetToday today = new GetToday();
        CompletionTracker tracker = new CompletionTracker( today.getTodayPlus( -1 ) );

        // TODO ensure .00 granularity

        //System.out.println(tracker.getCompletionRate() );
        assertTrue( "CompletionTracker should init with 0.00 completion rate",
                    "0.00".equals( tracker.getCompletionRate() ) );

        tracker.addCompletion( today.getTodayPlus( -1 ) );
        //System.out.println(tracker.getCompletionRate() );
        assertTrue( "CompletionTracker should have 50.00 completion rate with missed=1 fulfilled=1",
                    "50.00".equals( tracker.getCompletionRate() ) );

        tracker.addCompletion( today.getString() );
        //System.out.println(tracker.getCompletionRate() );
        assertTrue( "CompletionTracker should have 100.00 completion rate with missed=0 fulfilled=2",
                "100.00".equals( tracker.getCompletionRate() ) );
    }

} // class CompletionTrackerTest
