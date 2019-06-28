package com.example.ntu_timetable_calendar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntu_timetable_calendar.CourseModels.Course;
import com.example.ntu_timetable_calendar.Helper.StringHelper;
import com.example.ntu_timetable_calendar.R;

public class SearchRVAdapter extends ListAdapter<Course, RecyclerView.ViewHolder> {

    // Constants
    private static final int HEADER_VIEW_TYPE = 0;
    private static final int ITEM_VIEW_TYPE = 1;

    private Context context;
    /*private List<Course> courseList = new ArrayList<>();*/

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private onItemClickListener itemClickListener;


    public interface onItemClickListener {
        void onClick(int position, Course course);
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public SearchRVAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<Course> DIFF_CALLBACK = new DiffUtil.ItemCallback<Course>() {
        @Override
        public boolean areItemsTheSame(@NonNull Course oldItem, @NonNull Course newItem) {
            return oldItem.getCourseCode().equals(newItem.getCourseCode());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Course oldItem, @NonNull Course newItem) {
            return oldItem.getCourseCode().equals(newItem.getCourseCode());
        }
    };

    /**
     * ViewHolder class for RV item
     */
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout itemContainer;
        private TextView nameTV, auTV, indexTV, codeTV;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemContainer = itemView.findViewById(R.id.search_rv_item_container);
            nameTV = itemView.findViewById(R.id.search_rv_item_name);
            auTV = itemView.findViewById(R.id.search_rv_item_au);
            indexTV = itemView.findViewById(R.id.search_rv_item_index);
            codeTV = itemView.findViewById(R.id.search_rv_item_code);
            itemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onClick(getAdapterPosition(), getItem(getAdapterPosition()));
                    }
                }
            });
        }
    }

    /**
     * ViewHolder class for RV Header (First item in the recyclerview)
     */
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEADER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.search_rv_header_layout, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.search_rv_item_layout, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == HEADER_VIEW_TYPE) {

        } else if (holder.getItemViewType() == ITEM_VIEW_TYPE) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Course course = getItem(position);
            itemViewHolder.nameTV.setText(StringHelper.formatNameString(course.getName()));
            itemViewHolder.auTV.setText(course.getAu().trim());
            String indexStr = StringHelper.formatIndexString(course);
            itemViewHolder.indexTV.setText(indexStr);
            itemViewHolder.codeTV.setText(course.getCourseCode());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_VIEW_TYPE;
        } else {
            return ITEM_VIEW_TYPE;
        }
    }
}
