package com.example.ntu_timetable_calendar.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.ntu_timetable_calendar.PagerAdapter.TodoPagerAdapter;
import com.example.ntu_timetable_calendar.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private void initTabLayout() {
        todoPagerAdapter = new TodoPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new TaskFragmentFrag1());
        fragmentList.add(new TaskFragmentFrag2());
        fragmentList.add(new TaskFragmentFrag3());
        todoPagerAdapter.addFragments(fragmentList);

        List<String> titleList = new ArrayList<>();
        titleList.add("Next 7 days");
        titleList.add("This Month");
        titleList.add("Projects");
        todoPagerAdapter.addTitles(titleList);

        viewPager.setAdapter(todoPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.todo_fragment_fab) {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .add(R.id.activitymain_fragment_container, new AddNewTaskFragment(), "add_new_task_fragment")
                    .hide(this).addToBackStack(null).commit();
        }

    }
}
