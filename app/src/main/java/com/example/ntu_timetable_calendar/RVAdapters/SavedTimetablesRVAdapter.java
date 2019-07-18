package com.example.ntu_timetable_calendar.RVAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntu_timetable_calendar.Entity.TimetableEntity;
import com.example.ntu_timetable_calendar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter for the saved timetable fragment
 */
public class SavedTimetablesRVAdapter extends RecyclerView.Adapter<SavedTimetablesRVAdapter.MyViewHolder> {

    private Context context;
    private List<TimetableEntity> timetableEntities = new ArrayList<>();

    public SavedTimetablesRVAdapter(Context context) {
        this.context = context;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public interface RecyclerViewItemClickListener {
        void itemListener(TimetableEntity timetableEntity);
    }

    private RecyclerViewItemClickListener mListener;

    public void setRecyclerViewItemClickListener(RecyclerViewItemClickListener mListener) {
        this.mListener = mListener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout layoutContainer;
        private TextView titleTV, descriptionTV;
        private ImageView starIcon;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutContainer = itemView.findViewById(R.id.saved_timetable_fragment_rv_item_container);
            titleTV = itemView.findViewById(R.id.saved_timetable_fragment_rv_item_title);
            descriptionTV = itemView.findViewById(R.id.saved_timetable_fragment_rv_item_description);
            starIcon = itemView.findViewById(R.id.saved_timetable_fragment_rv_item_icon);

            layoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        mListener.itemListener(timetableEntities.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public void passData(List<TimetableEntity> timetableEntities) {
        this.timetableEntities.clear();
        this.timetableEntities.addAll(timetableEntities);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.saved_timetable_fragment_rv_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TimetableEntity timetableEntity = timetableEntities.get(position);

        holder.titleTV.setText(timetableEntity.getName());
        holder.descriptionTV.setText(timetableEntity.getDescription());

        if (timetableEntity.getIsMainTimetable()) {
            holder.starIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_yellow_24dp));
        } else {
            holder.starIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border_grey_24dp));
        }
    }

    @Override
    public int getItemCount() {
        return timetableEntities.size();
    }


}
