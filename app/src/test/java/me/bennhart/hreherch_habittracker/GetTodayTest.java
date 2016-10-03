package me.bennhart.hreherch_habittracker;

import junit.framework.TestCase;

/**
 * Created by Ben on 2016-09-24.
 */
public class GetTodayTest extends TestCase {

    public void testGetDatePlus() {
        GetToday today = new GetToday();
        String aDay = "2016-09-24";
        String theNextDay = "2016-09-25";
        assertEquals("testGetDatePlus: next day was not returned",
                0, theNextDay.compareTo(today.getDatePlus(aDay, 1)));

        aDay = "2015-02-28";
        theNextDay = "2015-03-01";
        assertEquals("testGetDatePlus: formatter can't handle non-leap years",
                theNextDay, today.getDatePlus(aDay, 1));
    }
}
