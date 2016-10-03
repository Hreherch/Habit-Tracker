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

import android.support.annotation.Nullable;

/**
 * Singleton for a HabitList, viewHabit, and saveContext
 */
public class HabitListController {
    private static HabitList habitList = null;
    private static Integer viewHabit = null;
    private static MainActivity saveContext = null;

    public static HabitList getHabitList() {
        if (habitList == null) {
            habitList = new HabitList();
        }
        return habitList;
    }

    // used after/during a load to return to a saved state of the HabitList
    public static void setHabitList(HabitList newHabitList) {
        habitList = newHabitList;
        save();
    }

    // holds the context that can save to file, so that the controller can save after modifying
    public static void setSaveContext(MainActivity context) {
        saveContext = context;
    }

    /**
     * Adds a habit to the habitList, with the given habitName, and start date,
     *
     * @param habitName the name of the new habit
     * @param date the start date of a new habit (format yyyy-MM-dd)
     * @param listOfActiveDays corresponds to what days the habit is active on
     * @return An error message if a problem occurred, null otherwise
     */
    public String addHabit(String habitName, String date, boolean[] listOfActiveDays) {
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

    /**
     * Adds a completion to a habit by calling addCompletionToday with the habit's name, finds
     * the habit's name using the given position in the habitList array
     *
     * @param position the index that the habit is at in the habitList
     */
    public void addCompletionToday(int position) {
        addCompletionToday(getHabitList().getHabits().get(position).getName());
        save();
    }

    public void addCompletionToday(String habitName) {
        getHabitList().addHabitCompletion(habitName);
        save();
    }

    public void removeHabit(String habitName) {
        getHabitList().removeHabit(habitName);
        save();
    }

    // Sets the current "viewing" habit to be used with ViewHabitActivity
    public void setViewHabit(@Nullable Integer index) {
        viewHabit = index;
    }

    public Habit getViewHabit() {
        if (viewHabit == null) {
            throw new RuntimeException("viewHabit was not set properly.");
        }
        return HabitListController.getHabitList().getHabits().get(viewHabit);
    }

    // for changing the currently view'd habit's name
    public void setViewHabitName(String newHabitName) {
        getHabitList().setHabitName(getViewHabit().getName(), newHabitName);
        save();
    }

    public void setHabitActives(String habitName, boolean[] newActiveList) {
        getHabitList().setHabitActives(habitName, newActiveList);
        save();
    }

    public static void save() {
        saveContext.save();
    }

    /**
     * Clears all data from the saved file and habitList
     */
    public static void reset() {
        habitList = null;
        viewHabit = null;
        save();
    }
}
