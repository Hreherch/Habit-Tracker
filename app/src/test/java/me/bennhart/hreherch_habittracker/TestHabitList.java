package me.bennhart.hreherch_habittracker;

import junit.framework.TestCase;
import static org.junit.Assert.*;

/**
 * Created by Ben on 2016-09-26.
 */
public class TestHabitList  extends TestCase {

    public void testAddingHabit() {
        String habitName = "myHabit";
        Habit myHabit = new Habit( habitName );
        HabitList habitList = new HabitList();

        habitList.addHabit( myHabit );

        assertEquals( "TestHabitList: There should be only one element in the HabitList",
                      1, habitList.getSize() );

        Habit returnHabit = habitList.getHabit( habitName );
        assertEquals( "TestHabitList: Should not have modified the habit given to the habitList",
                      myHabit.getName(), returnHabit.getName() );
    }

    public void testRemovingHabit() {
        String habitName = "aHabit";
        Habit aHabit = new Habit( habitName );
        HabitList habitList = new HabitList();

        habitList.addHabit( aHabit );

        habitList.removeHabit( habitName );

        assertEquals( "testRemovingHabits: there should be no habits left in the HabitList",
                      0, habitList.getSize() );


        habitList.getHabit( habitName );
        assertEquals( "testRemovingHabits: Shouldn't be able to get habit that was removed.",
                      null, habitList.getHabit( habitName ) );

    }

    public void testGettingHabit() {
        String habit1 = "habit1";
        String habit2 = "habit2";
        Habit aHabit = new Habit( habit1 );
        Habit anotherHabit = new Habit( habit2 );
        HabitList habitList = new HabitList();

        habitList.addHabit( aHabit );
        habitList.addHabit( anotherHabit );

        assertEquals( "testRemovingHabits: HabitList returns the wrong habit",
                       habit1, habitList.getHabit( habit1 ).getName() );

        assertEquals( "testRemovingHabits: HabitList returns the wrong habit",
                       habit2, habitList.getHabit( habit2 ).getName() );
    }
}
