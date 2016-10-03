package me.bennhart.hreherch_habittracker;

import junit.framework.TestCase;

/**
 * Created by Ben on 2016-09-26.
 */
public class TestHabitList extends TestCase {

    public void testAddingHabit() {
        String habitName = "myHabit";
        Habit myHabit = new Habit(habitName);
        HabitList habitList = new HabitList();

        habitList.addHabit(myHabit);

        assertEquals("TestHabitList: There should be only one element in the HabitList",
                1, habitList.getSize());

        Habit returnHabit = habitList.getHabit(habitName);
        assertEquals("TestHabitList: Should not have modified the habit given to the habitList",
                myHabit.getName(), returnHabit.getName());
    }

    public void testRemovingHabit() {
        String habitName = "aHabit";
        Habit aHabit = new Habit(habitName);
        HabitList habitList = new HabitList();

        habitList.addHabit(aHabit);

        habitList.removeHabit(habitName);

        assertEquals("testRemovingHabits: there should be no habits left in the HabitList",
                0, habitList.getSize());


        habitList.getHabit(habitName);
        assertEquals("testRemovingHabits: Shouldn't be able to get habit that was removed.",
                null, habitList.getHabit(habitName));

    }

    public void testGettingHabit() {
        String habit1 = "habit1";
        String habit2 = "habit2";
        Habit aHabit = new Habit(habit1);
        Habit anotherHabit = new Habit(habit2);
        HabitList habitList = new HabitList();

        habitList.addHabit(aHabit);
        habitList.addHabit(anotherHabit);

        assertEquals("testRemovingHabits: HabitList returns the wrong habit",
                habit1, habitList.getHabit(habit1).getName());

        assertEquals("testRemovingHabits: HabitList returns the wrong habit",
                habit2, habitList.getHabit(habit2).getName());
    }

    boolean update = false;

    public void testListenerUpdateOnAddHabit() {
        HabitList habitList = new HabitList();
        Listener listen = new Listener() {
            @Override
            public void update() {
                update = true;
            }
        };
        habitList.addListener(listen);
        habitList.addHabit(new Habit("meditate"));
        assertTrue("habitList listener saw update on add habit", update);
    }


    public void testOrderingHabits() {
        // order: b (true, 3) > a (true, 5) > d (false, 0) > c (false, 5)
        boolean[] dotwList = {true, true, true, true, true, true, true};
        Habit a = new Habit("a");
        a.setActive(dotwList);
        a.setHabitCompletions(null, 5);

        Habit b = new Habit("b");
        b.setActive(dotwList);
        b.setHabitCompletions(null, 3);

        Habit c = new Habit("c");
        c.setHabitCompletions(null, 5);

        Habit d = new Habit("d");

        HabitList habitList = new HabitList();
        habitList.addHabit(a);
        habitList.addHabit(b);
        habitList.addHabit(c);
        habitList.addHabit(d);

        System.out.println(habitList.getHabits().get(0).getName() +
                habitList.getHabits().get(1).getName() +
                habitList.getHabits().get(2).getName() +
                habitList.getHabits().get(3).getName());

        assertEquals("HabitList failed to order as expected",
                b.getName(), habitList.getHabits().get(0).getName());

        assertEquals("HabitList failed to order as expected",
                a.getName(), habitList.getHabits().get(1).getName());

        assertEquals("HabitList failed to order as expected",
                d.getName(), habitList.getHabits().get(2).getName());

        assertEquals("HabitList failed to order as expected",
                c.getName(), habitList.getHabits().get(3).getName());
    }

    public void testChangeHabitName() {
        String habitName = "myHabit";
        String newHabitName = "newHabit";
        Habit aHabit = new Habit(habitName);
        HabitList habitList = new HabitList();
        habitList.addHabit(aHabit);
        habitList.setHabitName(habitName, newHabitName);

        assertEquals("HabitList did not set new name to habit",
                newHabitName, habitList.getHabits().get(0).getName());
    }
}
