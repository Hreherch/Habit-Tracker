package me.bennhart.hreherch_habittracker;

import java.util.ArrayList;

/**
 * Created by Ben on 2016-09-26.
 */
public class HabitListController {
    private static HabitList habitList = null;

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

    public void addCompletion( int position ) {
        Habit habit = getHabitList().getHabits().get( position );
        habit.addHabitCompletion( null );
        getHabitList().notifyListeners();
    }

}
