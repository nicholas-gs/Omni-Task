package com.example.ntu_timetable_calendar;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.ntu_timetable_calendar.Fragments.CalendarFragment;
import com.example.ntu_timetable_calendar.Fragments.HomeFragment;
import com.example.ntu_timetable_calendar.Fragments.PlanFragment;
import com.example.ntu_timetable_calendar.Fragments.SearchFragment;
import com.example.ntu_timetable_calendar.Helper.KeyboardHelper;
import com.example.ntu_timetable_calendar.JsonDatabase.JsonDatabase;
import com.example.ntu_timetable_calendar.ViewModels.ActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        // We instantiate the jsonDatabase here (even though we don't the data yet) in order to prevent
        // the fragment loading to lag, since instantiating the database carries out the deserialization
        JsonDatabase jsonDatabase = JsonDatabase.getJsonDatabaseInstance(getApplicationContext());

        initialiseViews();
        setUpBottomNav();
        setupViewModel();
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
                                new CalendarFragment(), "calendar_fragment").commit();
                        break;
                }
                return true;
            }
        });
    }

    private void setupViewModel(){
        ActivityViewModel activityViewModel = ViewModelProviders.of(this).get(ActivityViewModel.class);
        activityViewModel.setSearchQuery(null);
    }

}
