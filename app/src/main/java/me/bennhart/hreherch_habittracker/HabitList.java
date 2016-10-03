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

package me.bennhart.hreherch_habittracker;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Holds a list of habits for manipulation
 *
 * Uses an ArrayList of names to aid in preventing duplicate habit names in the HabitList
 * Allows Listeners to see if data has changed and notifies
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

    /**
     * Ensures no other habit with the same name exists then adds it to the habitList
     *
     * @param habit a new habit to add to the habit list
     */
    public void addHabit(Habit habit) {
        if (habitNames.contains(habit.getName())) {
            throw new IllegalArgumentException("You may not have two habits with the same name.");
        }
        habitList.add(habit);
        habitNames.add(habit.getName());
        Collections.sort(habitList);
        notifyListeners();
    }

    public void removeHabit(String habitName) {
        if (!(habitNames.contains(habitName))) {
            throw new IllegalArgumentException("You may not remove a habit not in the HabitList");
        }

        Habit removeHabit = null;

        habitNames.remove(habitName);
        for (Habit habit : habitList) {
            if (habit.getName().equals(habitName)) {
                removeHabit = habit;
            }
        }
        habitList.remove(removeHabit);
        Collections.sort(habitList);
        notifyListeners();
    }

    /**
     * Returns a habit object based on the habitName passed
     *
     * @param habitName the name of the habit you with to get from the habitList
     * @return null, if no habit exists by the given name, otherwise returns a specified habit
     */
    public Habit getHabit(String habitName) {
        if (!(habitNames.contains(habitName))) {
            return null;
        }

        for (Habit habit : habitList) {
            if (habit.getName().equals(habitName)) {
                return habit;
            }
        }
        return null;
    }

    /**
     * Adds a completion to today for a habit
     *
     * @param habitName the name of the habit that will get a completion for today
     */
    public void addHabitCompletion(String habitName) {
        Habit habit = getHabit(habitName);
        if (habitName == null) {
            throw new RuntimeException("Habit name not in HabitList");
        }
        habit.addHabitCompletion(null);
        Collections.sort(habitList);
        notifyListeners();
    }

    public void setHabitName(String oldName, String newName) {
        if (habitNames.contains(newName)) {
            throw new IllegalArgumentException("You may not change a habit name to one that already exists.");
        }
        if (newName.equals("")) {
            throw new IllegalArgumentException("You may not set a habit name to be blank.");
        }
        habitNames.remove(oldName);
        habitNames.add(newName);

        for (Habit habit : habitList) {
            if (habit.getName().equals(oldName)) {
                habit.setName(newName);
            }
        }

        notifyListeners();
    }

    public int getSize() {
        return habitList.size();
    }

    public void addListener(Listener newListener) {
        listeners.add(newListener);
    }

    public void notifyListeners() {
        for (Listener listen : listeners) {
            listen.update();
        }
    }

    // Sets a habit's days active
    public void setHabitActives(String habitName, boolean[] newActiveList) {
        for (Habit habit : habitList) {
            if (habit.getName().equals(habitName)) {
                habit.setActive(newActiveList);
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
