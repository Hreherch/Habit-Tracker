package me.bennhart.hreherch_habittracker;

import android.net.sip.SipAudioCall;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ben on 2016-09-23.
 */
public class HabitList {
    protected ArrayList<Habit> habitList;
    protected ArrayList<String> habitNames;
    protected ArrayList<Listener> listeners;

    public HabitList() {
        habitList = new ArrayList<Habit>();
        habitNames = new ArrayList<String>();
        listeners = new ArrayList<Listener>();
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
        Collections.sort( habitList );
        notifyListeners();
    }

    public void removeHabit( String habitName ) {
        if ( !( habitNames.contains( habitName ) ) ) {
            throw new IllegalArgumentException( "You may not remove a habit not in the HabitList" );
        }

        Habit removeHabit = null;

        habitNames.remove( habitName );
        for ( Habit habit : habitList ) {
            if ( habit.getName().equals( habitName ) ) {
                removeHabit = habit;
            }
        }
        habitList.remove( removeHabit );
        Collections.sort( habitList );
        notifyListeners();
    }

    public Habit getHabit( String habitName ) {
        if ( !( habitNames.contains( habitName ) ) ) {
            return null;
        }

        for ( Habit habit : habitList ) {
            if ( habit.getName().equals( habitName ) ) {
                return habit;
            }
        }
        return null;
    }

    public void addHabitCompletion( String habitName ) {
        Habit habit = getHabit( habitName );
        if ( habitName == null ) {
            throw new RuntimeException( "Habit name not in HabitList" );
        }
        habit.addHabitCompletion( null );
        Collections.sort( habitList );
        notifyListeners();
    }

    public void setHabitName( String oldName, String newName ) {
        habitNames.remove( oldName );
        habitNames.add( newName );

        for ( Habit habit : habitList ) {
            if ( habit.getName().equals( oldName ) ) {
                habit.setName( newName );
            }
        }

        notifyListeners();
    }

    public int getSize() {
        return habitList.size();
    }

    public void addListener( Listener newListener ) {
        listeners.add( newListener );
    }

    public void notifyListeners() {
        for ( Listener listen : listeners ) {
            listen.update();
        }
    }

    public void setHabitActives(String habitName, boolean[] newActiveList) {
        for ( Habit habit : habitList ) {
            if ( habit.getName().equals( habitName ) ) {
                habit.setActive( newActiveList );
            }
        }
        notifyListeners();
    }

    public int getNumListeners() {
        return listeners.size();
    }

    public void clearListeners() {
        listeners.clear();
    }


}
