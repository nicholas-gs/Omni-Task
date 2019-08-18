package com.example.ntu_timetable_calendar.RVAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntu_timetable_calendar.Entity.ProjectEntity;
import com.example.ntu_timetable_calendar.R;

public class ProjectsRVAdapter extends ListAdapter<ProjectEntity, ProjectsRVAdapter.ProjectRVAdapterVH> {

    private Context context;

    public ProjectsRVAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public interface onItemClickedListener {
        void onItemClicked(ProjectEntity projectEntity, int position);
    }

    private ProjectsRVAdapter.onItemClickedListener mListener;

    public void setOnItemClickedListener(ProjectsRVAdapter.onItemClickedListener mListener) {
        this.mListener = mListener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    private static final DiffUtil.ItemCallback<ProjectEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<ProjectEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull ProjectEntity oldItem, @NonNull ProjectEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ProjectEntity oldItem, @NonNull ProjectEntity newItem) {
            return ((oldItem.getName().equals(newItem.getName()))
                    && (oldItem.getDescription().equals(newItem.getDescription()))
                    && (oldItem.getPriority() == newItem.getPriority())
            );
        }
    };

    class ProjectRVAdapterVH extends RecyclerView.ViewHolder {

        private RelativeLayout layoutContainer;
        private TextView titleTV, numberOfTasksTV;
        private ImageView priorityImageView, tasksCompletedImageView;

        ProjectRVAdapterVH(@NonNull View itemView) {
            super(itemView);

            layoutContainer = itemView.findViewById(R.id.project_rv_item_container);
            titleTV = itemView.findViewById(R.id.project_rv_item_title);
            numberOfTasksTV = itemView.findViewById(R.id.project_rv_item_number_of_tasks);
            priorityImageView = itemView.findViewById(R.id.project_rv_item_priority_icon);
            tasksCompletedImageView = itemView.findViewById(R.id.project_rv_item_all_completed_icon);

            layoutContainer.setOnClickListener(new View.OnClickListener() {
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
    public ProjectRVAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.project_rv_item_layout, parent, false);
        return new ProjectRVAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectRVAdapterVH holder, int position) {
        ProjectEntity projectEntity = getItem(position);

        holder.priorityImageView.setImageDrawable(context.getDrawable(formatPriorityDrawable(projectEntity.getPriority())));
        holder.titleTV.setText(projectEntity.getName());
        holder.numberOfTasksTV.setText(formatNumberOfTasksTextView(projectEntity.getNumberOfTasks(), projectEntity.getNumberOfUncompletedTasks()));
        setTaskCompletedImageView(holder.tasksCompletedImageView, projectEntity.getNumberOfUncompletedTasks());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param priority Priority of the project
     * @return Return the drawable to display depending on the priority of the project
     */
    private int formatPriorityDrawable(int priority) {
        if (priority == 1) {
            return R.drawable.ic_event_note_red_24dp;
        } else if (priority == 2) {
            return R.drawable.ic_event_note_yellow_24dp;
        } else if (priority == 3) {
            return R.drawable.ic_event_note_green_24dp;
        } else if (priority == 4) {
            return R.drawable.ic_event_note_lightblue_24dp;
        } else {
            return R.drawable.ic_event_note_darkgray_24dp;
        }
    }

    /**
     * @param numberOfTasks            Number of tasks variable of the project entity
     * @param numberOfUncompletedTasks Number of uncompleted tasks variable of the project entity
     * @return String to display in the TextView depending on numberOfTasks & numberOfUncompletedTasks variables
     */
    private String formatNumberOfTasksTextView(int numberOfTasks, int numberOfUncompletedTasks) {
        if (numberOfTasks == 0) {
            return "No tasks!";
        } else if (numberOfUncompletedTasks == 0) {
            return "All tasks completed!";
        } else {
            return numberOfUncompletedTasks + " task uncompleted!";
        }
    }

    /**
     * @param imageView                ImageView that displays a "tick" if there are no uncompleted tasks
     * @param numberOfUncompletedTasks numberOfUncompletedTasks variable of ProjectEntity
     */
    private void setTaskCompletedImageView(ImageView imageView, int numberOfUncompletedTasks) {
        if (numberOfUncompletedTasks == 0) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_check_white_24dp));
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }
}
