package com.example.ntu_timetable_calendar.RVAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntu_timetable_calendar.CourseModels.Detail;
import com.example.ntu_timetable_calendar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter for the recyclerview inside the course detail fragment
 * It takes in a single list of details (using setData())from inside a single of index object inside a course POJO and
 * displays all the detail POJO inside it in the RV
 */
public class IndexRVAdapter extends RecyclerView.Adapter<IndexRVAdapter.MyViewHolder> {

    private Context context;
    private List<Detail> detailList = new ArrayList<>();

    public IndexRVAdapter(Context context) {
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView typeTV, groupTV, timeTV, remarksTV, locationTV;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            typeTV = itemView.findViewById(R.id.index_rv_item_type);
            groupTV = itemView.findViewById(R.id.index_rv_item_group);
            timeTV = itemView.findViewById(R.id.index_rv_item_time);
            remarksTV = itemView.findViewById(R.id.index_rv_item_remarks);
            locationTV = itemView.findViewById(R.id.index_rv_item_location);
        }
    }

    /**
     * Use this method to pass data to be presented to the recyclerview
     * @param details
     */
    public void setData(List<Detail> details){
        this.detailList = details;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_detail_index_rv_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Detail detail = detailList.get(position);

        holder.typeTV.setText(detail.getType().trim());
        holder.groupTV.setText(String.format("Group : %s", detail.getGroup().trim()));
        holder.locationTV.setText(String.format("Location : %s", detail.getLocation().trim()));
        holder.timeTV.setText(String.format("Time : %s, %s%s", detail.getTime().getFull().trim(),
                Float.toString(detail.getTime().getDuration()).trim(), 'h'));
        holder.remarksTV.setText(String.format("Remarks : %s", detail.getRemarks().trim()));
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

}
