package com.example.ntu_timetable_calendar.adapters.recyclerviewadapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntu_timetable_calendar.models.entities.TaskEntity;
import com.example.ntu_timetable_calendar.R;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TaskFragmentRVAdapter extends ListAdapter<TaskEntity, RecyclerView.ViewHolder> {

    private Context context;

    public TaskFragmentRVAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public interface onItemClickedListener {
        void onItemClicked(TaskEntity taskEntity, int position);
    }

    private onItemClickedListener mListener;

    public void setOnItemClickedListener(onItemClickedListener mListener) {
        this.mListener = mListener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private static final DiffUtil.ItemCallback<TaskEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<TaskEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull TaskEntity oldItem, @NonNull TaskEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TaskEntity oldItem, @NonNull TaskEntity newItem) {
            return ((oldItem.getCourseEventEntityId() == newItem.getCourseEventEntityId()) && oldItem.getTitle().equals(newItem.getTitle())) && (oldItem.getDescription().equals(newItem.getDescription()))
                    && (oldItem.getDeadLine().equals(newItem.getDeadLine()) && (oldItem.getAlarmList().equals(newItem.getAlarmList()))
                    && (oldItem.getPriorityLevel() == newItem.getPriorityLevel() && (Arrays.equals(oldItem.getAlarmTimingChosen(), newItem.getAlarmTimingChosen())))
                    && (oldItem.getTimetableId() == newItem.getTimetableId() && (oldItem.getIsDone() == newItem.getIsDone()))
            );
        }
    };

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mLayoutContainer;
        private TextView mTitle, mDeadline;
        private ImageView mPriorityIcon, mAlarmIcon;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mLayoutContainer = itemView.findViewById(R.id.tasks_frag1_rv_item_container);
            mTitle = itemView.findViewById(R.id.tasks_frag1_rv_item_title);
            mDeadline = itemView.findViewById(R.id.tasks_frag1_rv_item_deadline);
            mPriorityIcon = itemView.findViewById(R.id.tasks_frag1_rv_item_priority_icon);
            mAlarmIcon = itemView.findViewById(R.id.tasks_frag1_rv_item_alarm_icon);

            mLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClicked(getItem(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_frag1_rv_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TaskEntity taskEntity = getItem(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        // Task Title
        myViewHolder.mTitle.setText(taskEntity.getTitle());

        // Deadline
        myViewHolder.mDeadline.setText(setDeadline(taskEntity.getDeadLine()));

        // Priority level
        setPriorityIcon(myViewHolder.mPriorityIcon, taskEntity.getPriorityLevel());

        // Alarm icon
        setAlarmIcon(myViewHolder.mAlarmIcon, taskEntity.getAlarmList());

        setViewsByIsDone(myViewHolder, taskEntity);
    }

    /**
     * @param deadline Deadline Calendar
     * @return String of deadline
     */
    private String setDeadline(Long deadline) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(deadline);
        Date deadlineDate = calendar.getTime();

        String str1 = DateFormat.getDateInstance(DateFormat.FULL).format(deadlineDate).trim();
        String str2 = DateFormat.getTimeInstance(DateFormat.SHORT).format(deadlineDate).trim();

        return str1 + " - " + str2;
    }

    private void setPriorityIcon(ImageView priorityIcon, int priority) {
        switch (priority) {
            case 1:
                priorityIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_flag_red_24dp));
                break;
            case 2:
                priorityIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_flag_yellow_24dp));
                break;
            case 3:
                priorityIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_flag_green_24dp));
                break;
            case 4:
                priorityIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_flag_lightblue_24dp));
                break;
            case 5:
                priorityIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_flag_darkgray_24dp));
                break;
        }
    }

    /**
     * @param alarmIcon  Alarm icon that is visible if task has alarm. If there is no alarm, the icon is gone
     * @param alarmsList List of alarms from task entity
     */
    private void setAlarmIcon(ImageView alarmIcon, List<Long> alarmsList) {
        if (alarmsList != null && alarmsList.size() != 0) {
            alarmIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_access_alarm_lightblue_24dp));
            alarmIcon.setVisibility(View.VISIBLE);
        } else {
            alarmIcon.setVisibility(View.GONE);
        }
    }

    private void setViewsByIsDone(MyViewHolder myViewHolder, TaskEntity taskEntity) {
        if (taskEntity.getIsDone()) {
            myViewHolder.mTitle.setPaintFlags(myViewHolder.mTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            myViewHolder.mDeadline.setPaintFlags(myViewHolder.mDeadline.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            myViewHolder.mTitle.setPaintFlags(myViewHolder.mTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            myViewHolder.mDeadline.setPaintFlags(myViewHolder.mDeadline.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}