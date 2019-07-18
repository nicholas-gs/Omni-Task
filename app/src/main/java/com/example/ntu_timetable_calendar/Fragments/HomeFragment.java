package com.example.ntu_timetable_calendar.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.SecondActivity;
import com.google.android.material.appbar.AppBarLayout;

public class HomeFragment extends Fragment {

    private AppBarLayout mAppbarLayout;
    private Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    private void initViews(View view){
        mToolbar = view.findViewById(R.id.home_fragment_toolbar);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.saved_timetables_menu_item:
                        startSecondActivity(getString(R.string.SAVED_TIMETABLES_INTENT));
                        break;
                    case R.id.about_menu_item:
                        startSecondActivity(getString(R.string.ABOUT_APP_INTENT));
                        break;
                }
                return true;
            }
        });
    }

    private void startSecondActivity(String intentString){
        Intent intent = new Intent(getActivity(), SecondActivity.class);
        intent.putExtra(getString(R.string.ACTIVITY_INTENT), intentString);
        startActivity(intent);
    }
}
