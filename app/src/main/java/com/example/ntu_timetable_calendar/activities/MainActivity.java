package com.example.ntu_timetable_calendar.activities;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ntu_timetable_calendar.models.entities.AlarmEntity;
import com.example.ntu_timetable_calendar.fragments.HomeFragment;
import com.example.ntu_timetable_calendar.fragments.PlanFragment;
import com.example.ntu_timetable_calendar.fragments.SearchFragment;
import com.example.ntu_timetable_calendar.fragments.TaskFragment;
import com.example.ntu_timetable_calendar.converters.AlarmParser;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.viewmodels.HomeFragmentActivityViewModel;
import com.example.ntu_timetable_calendar.viewmodels.PlanFragmentActivityViewModel;
import com.example.ntu_timetable_calendar.viewmodels.SQLViewModel;
import com.example.ntu_timetable_calendar.viewmodels.SearchFragmentActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.activitymain_fragment_container,
                    new HomeFragment(), "home_fragment").commit();
        }

        initialiseViews();
        setUpBottomNav();
        initActivityViewModels();
    }

    private void initialiseViews() {
        bottomNavigationView = findViewById(R.id.activitymain_bottom_navview);
        bottomNavigationView.setItemIconTintList(null);
    }

    private void setUpBottomNav() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // DON'T USE .add(), use .replace() - using .add() causes the livedata's onchanged() to trigger twice
                // for whatever reason!!!

                switch (item.getItemId()) {
                    case R.id.bottomNav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.activitymain_fragment_container,
                                new HomeFragment(), "home_fragment").commit();
                        break;
                    case R.id.bottomNav_search:
                        getSupportFragmentManager().beginTransaction().replace(R.id.activitymain_fragment_container,
                                new SearchFragment(), "home_fragment").commit();

                        break;
                    case R.id.bottomNav_plan:
                        getSupportFragmentManager().beginTransaction().replace(R.id.activitymain_fragment_container,
                                new PlanFragment(), "plan_fragment").commit();
                        break;
                    case R.id.bottomNav_calendar:
                        getSupportFragmentManager().beginTransaction().replace(R.id.activitymain_fragment_container,
                                new TaskFragment(), "task_fragment").commit();
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Initialize ViewModel whose lifecycle is tied to the application
     */
    private void initActivityViewModels() {
        SearchFragmentActivityViewModel searchFragmentActivityViewModel = ViewModelProviders.of(this).get(SearchFragmentActivityViewModel.class);
        searchFragmentActivityViewModel.setSearchQuery(null);
        PlanFragmentActivityViewModel planFragmentActivityViewModel = ViewModelProviders.of(this).get(PlanFragmentActivityViewModel.class);
        final SQLViewModel sqlViewModel = ViewModelProviders.of(this).get(SQLViewModel.class);
        HomeFragmentActivityViewModel homeFragmentActivityViewModel = ViewModelProviders.of(this).get(HomeFragmentActivityViewModel.class);

        /* We schedule the alarms here when there is a change in the alarm_table in Room */
        sqlViewModel.getAllAlarms().observe(this, new Observer<List<AlarmEntity>>() {
            @Override
            public void onChanged(List<AlarmEntity> alarmEntities) {
                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                AlarmParser.scheduleAlarmsHelper(getApplicationContext(), alarmManager, alarmEntities);
            }
        });
    }

}