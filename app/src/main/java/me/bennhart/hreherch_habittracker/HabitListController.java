package me.bennhart.hreherch_habittracker;

import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Ben on 2016-09-26.
 */
public class HabitListController {
    private static HabitList habitList = null;
    private static Integer viewHabit = null;

    public static HabitList getHabitList() {
        if ( habitList == null ) {
            habitList = new HabitList();
        }
        return habitList;
    }

    public String addHabit( String habitName, String date, boolean[] listOfActiveDays ) {
        try {
            Habit newHabit = new Habit(habitName, date);
            newHabit.setActive(listOfActiveDays);
            getHabitList().addHabit(newHabit);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        return null;
    }

    public void addCompletionToday( int position ) {
        addCompletionToday( getHabitList().getHabits().get( position ).getName() );
    }

    public void addCompletionToday( String habitName ) {
        getHabitList().addHabitCompletion( habitName );
    }

    public void removeHabit( String habitName ) {
        getHabitList().removeHabit( habitName );
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
    }

    public void setHabitActives( String habitName, boolean[] newActiveList ) {
        getHabitList().setHabitActives( habitName, newActiveList );
    }

    public static void save() {

    }
}
