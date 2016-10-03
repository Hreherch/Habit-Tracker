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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Handles string dates and converting them to other dates, also holds the format that
 * all dates must be in
 */
public class GetToday {
    private final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Returns a string that represents today's date with DATE_FORMAT
     *
     * @return a string that represents today
     */
    public String getString() {
        GregorianCalendar calendar = new GregorianCalendar();
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return formatter.format(calendar.getTime());
    }

    /**
     * Gets a string for a date that is plusDays days before or after today.
     *
     * @param plusDays the number of days plus today that you want a date string for
     * @return a string that represents today plus the number of days specified
     */
    public String getTodayPlus(int plusDays) {
        GregorianCalendar calendar = new GregorianCalendar();
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        calendar.add(Calendar.DATE, plusDays);
        return formatter.format(calendar.getTime());
    }

    /**
     * Gets a string for a date that is plusDays days before or after the given date string.
     *
     * @param date the date that you would like to modify to another string
     * @param plusDays the number of days you would like to add/subtract to date
     * @return a string that represents the given date plus the number of days specified
     */
    public String getDatePlus(String date, int plusDays) {
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        GregorianCalendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(formatter.parse(date));
        } catch (ParseException e) {
            String message = date + " is not a valid date. Ensure format: " + DATE_FORMAT;
            throw new IllegalArgumentException(message);
        }
        calendar.add(Calendar.DATE, plusDays);
        return formatter.format(calendar.getTime());
    }

    // i.e On Monday it returns "Monday"
    public String getDayName() {
        GregorianCalendar calendar = new GregorianCalendar();
        DateFormat formatter = new SimpleDateFormat("EEEE", Locale.getDefault());
        return formatter.format(calendar.getTime());
    }

    public String getDateFormat() {
        return DATE_FORMAT;
    }
}
