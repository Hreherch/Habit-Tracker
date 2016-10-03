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

import android.support.annotation.Nullable;

/**
 * Created by Ben on 2016-09-26.
 */
public class HabitListController {
    private static HabitList habitList = null;
    private static Integer viewHabit = null;
    private static String FILENAME = "sav.dat";
    private static MainActivity saveContext = null;

    public static HabitList getHabitList() {
        if ( habitList == null ) {
            habitList = new HabitList();
        }
        return habitList;
    }

    public static void setHabitList( HabitList newHabitList ) {
        habitList = newHabitList;
        save();
    }

    public static void setSaveContext( MainActivity context ) {
        saveContext = context;
    }

    public String addHabit( String habitName, String date, boolean[] listOfActiveDays ) {
        try {
            Habit newHabit = new Habit(habitName, date);
            newHabit.setActive(listOfActiveDays);
            getHabitList().addHabit(newHabit);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        save();
        return null;
    }

    public void addCompletionToday( int position ) {
        addCompletionToday( getHabitList().getHabits().get( position ).getName() );
        save();
    }

    public void addCompletionToday( String habitName ) {
        getHabitList().addHabitCompletion( habitName );
        save();
    }

    public void removeHabit( String habitName ) {
        getHabitList().removeHabit( habitName );
        save();
    }

    public void setViewHabit( @Nullable Integer index ) {
        viewHabit = index;
    }

    public Habit getViewHabit() {
        if (viewHabit == null) {
            throw new RuntimeException( "viewHabit was not set properly." );
        }
        return HabitListController.getHabitList().getHabits().get( viewHabit );
    }

    public void setViewHabitName( String newHabitName ) {
        getHabitList().setHabitName( getViewHabit().getName(), newHabitName );
        save();
    }

    public void setHabitActives( String habitName, boolean[] newActiveList ) {
        getHabitList().setHabitActives( habitName, newActiveList );
        save();
    }

    public static void save() {
        saveContext.save();
    }

    public static void reset() {
        habitList = null;
        viewHabit = null;
        save();
    }
}
