package me.bennhart.hreherch_habittracker;

//        HabitTracker: a simple android to-do list/habit tracker application.
//        Copyright (C) 2016 Bennett Hreherchuk hreherch@ualberta.ca
//
//        This program is free software: you can redistribute it and/or modify
//        it under the terms of the GNU General Public License as published by
//        the Free Software Foundation, either version 3 of the License, or
//        (at your option) any later version.
//
//        This program is distributed in the hope that it will be useful,
//        but WITHOUT ANY WARRANTY; without even the implied warranty of
//        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//        GNU General Public License for more details.
//
//        You should have received a copy of the GNU General Public License
//        along with this program.  If not, see <http://www.gnu.org/licenses/>.

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Ben on 2016-09-22.
 */
public class Habit implements Comparable<Habit> {
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

    public void addHabitCompletion( @Nullable String date ) {
        date = checkNull( date );
        completionTracker.addCompletion( date );
    }

    public int getHabitCompletions( @Nullable String date ) {
        date = checkNull( date );
        return completionTracker.getCompletions( date );
    }

    public void setHabitCompletions( @Nullable String date, int numCompletions ) {
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

    @Override
    public int compareTo( @NonNull Habit habit ) {
        // return positive if this > that
        // return negative if this < that
        int completions = this.getHabitCompletions( null ) - habit.getHabitCompletions( null );
        if ( this.isActiveToday() && habit.isActiveToday() ) {
            return (completions < 0) ? -1 : 1;
        }
        else if ( !this.isActiveToday() && !habit.isActiveToday() ) {
            return (completions < 0) ? -1 : 1;
        }
        else if ( !this.isActiveToday() && habit.isActiveToday() ) {
            return 1;
        } else {
            return -1;
        }
    }

    public String getCompletionRate() {
        return completionTracker.getCompletionRate();
    }

    public int getNumTotalCompletions() {
        return completionTracker.getNumTotalCompletions();
    }

    public int getNumDaysFulfilled() {
        return completionTracker.getNumDaysFulfilled();
    }

    public int getNumDaysMissed() {
        return completionTracker.getNumDaysMissed();
    }

    public void addListener( Listener newListener ) {
        completionTracker.addListener( newListener );
    }

    public void notifyListeners() {
        completionTracker.notifyListeners();
    }

    public int getNumListeners() {
        return completionTracker.getNumListeners();
    }

    public void clearListeners() {
        completionTracker.clearListeners();
    }

    public ArrayList<String> getAdaptableCompletionArray() {
        return completionTracker.getAdaptableCompletionArray();
    }
}
