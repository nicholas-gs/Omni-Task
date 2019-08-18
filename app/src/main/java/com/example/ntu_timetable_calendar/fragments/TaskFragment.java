package com.example.ntu_timetable_calendar.fragments;

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
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.ntu_timetable_calendar.adapters.pageradapters.TodoPagerAdapter;
import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.activities.SecondActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment implements View.OnClickListener {

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private TodoPagerAdapter todoPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initToolbar();
        initTabLayout();
    }

    private void initViews(View view) {
        appBarLayout = view.findViewById(R.id.todo_fragment_appbarlayout);
        toolbar = view.findViewById(R.id.todo_fragment_toolbar);
        tabLayout = view.findViewById(R.id.todo_fragment_tablayout);
        viewPager = view.findViewById(R.id.todo_fragment_viewpager);
        fab = view.findViewById(R.id.todo_fragment_fab);
        fab.setOnClickListener(this);

    }

    private void initToolbar() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.task_fragment_toolbar_menu_completedtasks) {
                    // TODO - A new fragment to show all the already completed tasks
                } else if (item.getItemId() == R.id.task_fragment_toolbar_menu_addnewproject) {
                   // TODO - A new fragment for user to add a new project
                }
                return true;
            }
        });
    }

    private void initTabLayout() {
        todoPagerAdapter = new TodoPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new TaskFragmentFrag1());
        fragmentList.add(new TaskFragmentFrag2());
        fragmentList.add(new TaskFragmentFrag3());
        fragmentList.add(new TaskFragmentFrag4());
        todoPagerAdapter.addFragments(fragmentList);

        List<String> titleList = new ArrayList<>();
        titleList.add("Next 7 days");
        titleList.add("This Month");
        titleList.add("All Tasks");
        titleList.add("Projects");
        todoPagerAdapter.addTitles(titleList);

        viewPager.setAdapter(todoPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.todo_fragment_fab) {
            String intentString = getString(R.string.ADD_NEW_TASK_INTENT);
            Intent intent = new Intent(getActivity(), SecondActivity.class);
            intent.putExtra(getString(R.string.SECOND_ACTIVITY_INTENT_KEY), intentString);
            startActivity(intent);
        }
    }
}
