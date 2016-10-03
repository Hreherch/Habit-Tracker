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

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Main launcher activity for HabitTracker.
 * Allows user to see habit completions and complete them
 * Gives user ability to view individual habits or create new ones
 * Has Drawer functionality for resetting all data
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String FILENAME = "sav.dat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // give the HabitListController the ability to Save data at any time.
        HabitListController.setSaveContext(this);
        load();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddHabitActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        // set day of the week text for today
        TextView dayText = (TextView) findViewById(R.id.editText_dayName);
        GetToday today = new GetToday();
        dayText.setText(today.getDayName());

        // update listView with an adapter to show data
        ListView habitListView = (ListView) findViewById(R.id.listView_ofHabits);
        final HabitAdapter habitArrayAdapter;
        ArrayList<Habit> habitList = HabitListController.getHabitList().getHabits();
        habitArrayAdapter = new HabitAdapter(this, R.layout.habitlist_item,
                habitList);
        habitListView.setAdapter(habitArrayAdapter);

        // clear the listeners, to prevent listener accumulation
        HabitListController.getHabitList().clearListeners();

        HabitListController.getHabitList().addListener(new Listener() {
            @Override
            public void update() {
                habitArrayAdapter.notifyDataSetChanged();
            }
        });

        // Allow on long click of the habit to open a ViewHabitActivity for that habit
        habitListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                HabitListController habitListController = new HabitListController();
                habitListController.setViewHabit(position);
                Intent intent = new Intent(MainActivity.this, ViewHabitActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_reset_all) {
            // creates a dialogue asking if the user is sure they want to reset all data.
            AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
            adb.setMessage("Delete Everything?");
            adb.setCancelable(true);
            adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HabitListController.reset();
                    onBackPressed();
                    onResume();
                }
            });
            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //pass
                }
            });
            adb.show();
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // loads data from the save file if it exists
    // TODO Code taken from LonelyTwitter
    public void load() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            // TODO Code from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            Type listType = new TypeToken<HabitList>() {
            }.getType();

            HabitListController.setHabitList((HabitList) gson.fromJson(in, listType));

        } catch (FileNotFoundException e) {
            HabitListController.setHabitList(null);
        }
    }

    // saves data to save file (and creates the save file if it exists)
    // TODO code taken from LonelyTwitter
    public void save() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, 0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(HabitListController.getHabitList(), out);
            out.flush();

            fos.close();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
