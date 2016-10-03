package me.bennhart.hreherch_habittracker;

/*
 * Copyright 2016 Hreherchuk, Bennett
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 * { description of file/class name, etc... }
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String FILENAME = "sav.dat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HabitListController.setSaveContext( this );
        load();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show();
                startActivity( new Intent( MainActivity.this, AddHabitActivity.class ));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Start navigation window with to-do selected
        navigationView.setCheckedItem( R.id.nav_button_todo );
    }

    @Override
    public void onResume() {
        super.onResume();

        // set day of the week text for today
        TextView dayText = (TextView) findViewById( R.id.editText_dayName );
        GetToday today = new GetToday();
        dayText.setText( today.getDayName() );

        // reset navigation button to the to-do on return from other activities
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem( R.id.nav_button_todo );

        // update listView
        ListView habitListView = (ListView) findViewById( R.id.listView_ofHabits );
        final HabitAdapter habitArrayAdapter;
        //final ArrayList<Habit> list = new ArrayList<Habit>();
        ArrayList<Habit> habitList = HabitListController.getHabitList().getHabits();
        habitArrayAdapter = new HabitAdapter( this, R.layout.habitlist_item,
                                              habitList );
        habitListView.setAdapter( habitArrayAdapter );

        HabitListController.getHabitList().clearListeners();

        //Toast.makeText( MainActivity.this, "" + HabitListController.getHabitList().getNumListeners(), Toast.LENGTH_LONG ).show();

        HabitListController.getHabitList().addListener(new Listener() {
            @Override
            public void update() {
                // TODO why was this the same effect?
//                list.clear();
//                ArrayList<Habit> newList = HabitListController.getHabitList().getHabits();
//                list.addAll( newList );
                habitArrayAdapter.notifyDataSetChanged();
            }
        });

        habitListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                HabitListController habitListController = new HabitListController();
                habitListController.setViewHabit( position );
                Intent intent = new Intent( MainActivity.this, ViewHabitActivity.class );
                startActivity( intent );
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
    public boolean onNavigationItemSelected( @NonNull MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_button_todo) {
            // do nothing (you're already here)

        } else if (id == R.id.nav_button_history) {
            Intent intent = new Intent( MainActivity.this, ViewHistoryActivity.class );
            startActivity( intent );

        } else if (id == R.id.nav_button_statistics) {
            Intent intent = new Intent( MainActivity.this, ViewStatisticsActivity.class );
            startActivity( intent );

        } else if (id == R.id.nav_reset_all) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void load() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            // Code from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            Type listType = new TypeToken<HabitList>(){}.getType();

            HabitListController.setHabitList( (HabitList) gson.fromJson(in,listType) );

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            HabitListController.setHabitList( null );
        }
    }

    public void save() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, 0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson( HabitListController.getHabitList(), out );
            out.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }
}
