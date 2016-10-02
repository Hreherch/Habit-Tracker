package me.bennhart.hreherch_habittracker;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

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
}
