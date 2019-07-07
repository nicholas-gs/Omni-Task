package com.example.ntu_timetable_calendar.RVAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntu_timetable_calendar.CourseModels.Course;
import com.example.ntu_timetable_calendar.CourseModels.Index;
import com.example.ntu_timetable_calendar.Helper.StringHelper;
import com.example.ntu_timetable_calendar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RecyclerView adapter for the PlanFragment Bottom Sheet
 */
public class PlanFragmentBottomSheetRVAdapter extends RecyclerView.Adapter<PlanFragmentBottomSheetRVAdapter.MyViewHolder> {

    private List<Course> courseList;
    private Context context;
    private Map<String, String> indexesSel;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public interface ItemSpinnerInterface {
        void spinnerItemSelected(String courseCode, String newIndexSelection);
    }

    private ItemSpinnerInterface itemSpinnerInterface;

    public void setItemSpinnerInterface(ItemSpinnerInterface itemSpinnerInterface) {
        this.itemSpinnerInterface = itemSpinnerInterface;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public PlanFragmentBottomSheetRVAdapter(List<Course> courseList, Context context, Map<String, String> indexesSel) {
        this.courseList = courseList;
        this.context = context;
        this.indexesSel = indexesSel;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTV;
        private Spinner spinner;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.bs_rv_item_course_title);
            spinner = itemView.findViewById(R.id.bs_rv_item_spinner);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (itemSpinnerInterface != null) {
                        itemSpinnerInterface.spinnerItemSelected(courseList.get(getAdapterPosition()).getCourseCode(),
                                adapterView.getItemAtPosition(i).toString());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plan_fragment_bs_rv_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.titleTV.setText(String.format("%s - %s", course.getCourseCode(),
                StringHelper.formatNameString(course.getName())));
        bindSpinnerData(course, holder.spinner);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    /**
     * Bind the data for each course item's spinner with the course's indexes
     *
     * @param course
     * @param spinner
     */
    private void bindSpinnerData(Course course, Spinner spinner) {

        final List<String> listOfIndexes = new ArrayList<>();
        for (Index index : course.getIndexes()) {
            listOfIndexes.add(index.getIndexNumber());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, R.layout.course_detail_fragment_spinner, listOfIndexes);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.course_detail_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setSelection(spinnerArrayAdapter.getPosition(indexesSel.get(course.getCourseCode())));

    }

}
