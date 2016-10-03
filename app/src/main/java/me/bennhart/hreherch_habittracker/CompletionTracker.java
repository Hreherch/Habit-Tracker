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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Tracks "completions" or counts per day of some activity
 */
public class CompletionTracker {
    private List<String> dates = new ArrayList<>();
    private List<Integer> completions = new ArrayList<>();
    private String startDate;
    private ArrayList<Listener> listeners = new ArrayList<>();

    public CompletionTracker(String date) {
        date = date.trim();
        startDate = date;
        setCompletions(date, 0);
    }

    public String getStartDate() {
        return startDate;
    }

    /**
     * Validates a given date to ensure the date complies with the assumptions of the tracker.
     * i.e. The date is not after today, the date is not before the start day, and the date is
     * of the proper format (yyyy-MM-dd).
     *
     * @param date a string date to check for validity, has format yyyy-MM-dd
     * @throws IllegalArgumentException
     */
    private void validateDate(String date) throws IllegalArgumentException {
        GetToday getToday = new GetToday();
        DateFormat formatter = new SimpleDateFormat(getToday.getDateFormat(),
                Locale.getDefault());
        String today = getToday.getString();

        // Check if date is ahead of today
        if (today.compareTo(date) < 0) {
            String message = "You may not add a completion to a date in the future";
            throw new IllegalArgumentException(message);
        }

        // Check if date is behind startDate
        if (getStartDate().compareTo(date) > 0) {
            String message = "You may not add a completion to a date before the initial date";
            throw new IllegalArgumentException(message);
        }

        // Catch invalid date and improper format
        try {
            formatter.parse(date);
        } catch (ParseException e) {
            String message = date + " is not a valid date. Ensure format: "
                    + getToday.getDateFormat();
            throw new IllegalArgumentException(message);
        }

    }

    /**
     * Adds a completion to a given date and notifies any listeners
     *
     * @param date a String representing the day you want to add a completion, format: yyyy-MM-dd
     */
    public void addCompletion(String date) {
        date = date.trim();
        validateDate(date);

        if (dates.contains(date)) {
            int index = dates.indexOf(date);
            completions.set(index, completions.get(index) + 1);
        } else {
            dates.add(date);
            int index = dates.indexOf(date);
            completions.add(index, 1);
        }
        notifyListeners();
    }

    /**
     * Returns the number of completions on a given date
     *
     * @param date a String of the date you want to get the completions of, format: yyyy-MM-dd
     * @return the number of completions on date or -1 if given an out of range date
     */
    public int getCompletions(String date) {
        date = date.trim();
        GetToday today = new GetToday();

        if (0 < getStartDate().compareTo(date) || 0 < date.compareTo(today.getString())) {
            return -1;
        }

        if (dates.contains(date)) {
            return completions.get(dates.indexOf(date));
        } else {
            return 0;
        }
    }

    /**
     * Sets the number of completions for a date.
     *
     * @param date a String specifying what day you want to set completions for, format: yyyy-MM-dd
     * @param numCompletions a integer specifying how many completions there should be on date
     * @throws IllegalArgumentException
     */
    public void setCompletions(String date, int numCompletions) throws IllegalArgumentException {
        if (numCompletions < 0) {
            throw new IllegalArgumentException("You may not have less than 0 completions");
        }

        date = date.trim();
        validateDate(date);

        // TODO simplify this if/else block?
        if (dates.contains(date)) {
            completions.set(dates.indexOf(date), numCompletions);
        } else {
            addCompletion(date);
            completions.set(dates.indexOf(date), numCompletions);
        }

        notifyListeners();
    }

    // includes today
    public int getNumDaysTracked() {
        GetToday today = new GetToday();
        String date = startDate;
        int days = 1;
        while (date.compareTo(today.getString()) != 0) {
            date = today.getDatePlus(date, 1);
            days += 1;
        }
        return days;
    }

    public int getNumTotalCompletions() {
        Integer completes = 0;
        for (Integer numComplete : completions) {
            completes += numComplete;
        }
        return completes;
    }

    // AKA the number of days that have >0 completions
    public int getNumDaysFulfilled() {
        Integer fulfilled = 0;
        for (Integer numComplete : completions) {
            if (numComplete > 0) {
                fulfilled += 1;
            }
        }
        return fulfilled;
    }

    public int getNumDaysMissed() {
        GetToday today = new GetToday();
        // return 0 if started today.
        if (startDate.equals(today.getString())) {
            return 0;
        }
        // remove a day if it has completions (it gets counted in days fulfilled)
        int removeToday = (getCompletions(today.getString()) > 0) ? 0 : -1;
        return getNumDaysTracked() - getNumDaysFulfilled() + removeToday;
    }

    public String getCompletionRate() {
        float temp = getNumDaysFulfilled() * 100 / getNumDaysTracked();
        return String.format(Locale.getDefault(), "%.2f", temp);
    }

    public void addListener(Listener newListener) {
        listeners.add(newListener);
    }

    public void notifyListeners() {
        for (Listener listen : listeners) {
            listen.update();
        }
    }

    public int getNumListeners() {
        return listeners.size();
    }

    public void clearListeners() {
        listeners.clear();
    }

    /**
     * Returns an array that can be used with an ArrayAdapter to create a ListView of completions
     * and what days they have been completed on
     *
     * @return a String array that contains date:numCompletions pairs
     */
    public ArrayList<String> getAdaptableCompletionArray() {
        ArrayList<String> adaptableCompletionArray = new ArrayList<>();
        GetToday today = new GetToday();
        String date = today.getString();
        adaptableCompletionArray.add(date + "\t Completions: " + getCompletions(date));
        while (date.compareTo(startDate) != 0) {
            date = today.getDatePlus(date, -1);
            adaptableCompletionArray.add(date + " Completions: " + getCompletions(date));
        }
        return adaptableCompletionArray;
    }
}
