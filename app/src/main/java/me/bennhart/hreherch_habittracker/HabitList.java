package me.bennhart.hreherch_habittracker;

import java.util.ArrayList;

/**
 * Created by Ben on 2016-09-23.
 */
public class HabitList {
    protected ArrayList<Habit> habitList;
    protected ArrayList<String> habitNames;

    public HabitList() {
        habitList = new ArrayList<Habit>();
        habitNames = new ArrayList<String>();
    }

    public ArrayList<Habit> getHabits() {
        return habitList;
    }

    public void addHabit( Habit habit ) {
        if ( habitNames.contains( habit.getName() ) ) {
            throw new IllegalArgumentException( "You may not have two habits with the same name." );
        }
        habitList.add( habit );
        habitNames.add( habit.getName() );
    }

    public void removeHabit( String habitName ) {
        if ( !( habitNames.contains( habitName ) ) ) {
            throw new IllegalArgumentException( "You may not remove a habit not in the HabitList" );
        }

        int index = habitNames.indexOf( habitName );

        habitNames.remove( index );
        habitList.remove( index );
    }

    public Habit getHabit( String habitName ) {
        if ( !( habitNames.contains( habitName ) ) ) {
            return null;
        }

        return habitList.get( habitNames.indexOf( habitName ) );
    }

    public int getSize() {
        return habitList.size();
    }
}
