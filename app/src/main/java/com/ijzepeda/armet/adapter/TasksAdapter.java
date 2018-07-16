package com.ijzepeda.armet.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ijzepeda.armet.R;
import com.ijzepeda.armet.activity.AddServiceActivity;
import com.ijzepeda.armet.model.Servicio;
import com.ijzepeda.armet.model.Task;

import java.util.List;

import static com.ijzepeda.armet.util.Constants.EXTRA_EDITING_SERVICE;
import static com.ijzepeda.armet.util.Constants.EXTRA_EDITING_TASK;
import static com.ijzepeda.armet.util.Constants.EXTRA_EDIT_SERVICE;
import static com.ijzepeda.armet.util.Constants.EXTRA_TASK_ID;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private List<Task> mData;
    private LayoutInflater mInflater;
    private TasksAdapter.ItemClickListener mClickListener;
    private Context context;

    private static int EMPTY_ROW = 1;
    private static int UNFINISHED_ROW = 2;
    private static int ANY_ROW = 0;

    // data is passed into the constructor
    public TasksAdapter(Context context, List<Task> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     * <p>
     * <p>The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * <code>position</code>. Type codes need not be contiguous.
     */
    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
//        if (mData.size() <= 0) {
        if (mData==null) {
            return EMPTY_ROW;
        } else if (mData.get(position).getFinalTime().equals("")) {
            return UNFINISHED_ROW;
        }
        else
            return ANY_ROW;

//        return 0;

    }

    // inflates the row layout from xml when needed
    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//int viewType compes from getItemViewType, there I select what int to return
        View view ;
        Log.e("ADAPTER=====", "onCreateViewHolder viewtype no is: "+viewType );
        if(viewType==EMPTY_ROW){
            view = mInflater.inflate(R.layout.task_empty_list_item, parent, false);
            return new TasksAdapter.ViewHolder(view);
        }
        else if (viewType == ANY_ROW){
            view = mInflater.inflate(R.layout.task_list_item, parent, false);
            return new TasksAdapter.ViewHolder(view);
        }
        else if (viewType == UNFINISHED_ROW) {
            view = mInflater.inflate(R.layout.task_incomplete_list_item, parent, false);
            return new TasksAdapter.ViewHolder(view);
        }
        return null;
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final TasksAdapter.ViewHolder holder, int position) {
        final Task task = mData.get(position);
//        final String itemName=mData.get(position).getName();
        holder.taskActionTextView.setText(task.getAction());
        holder.taskPlaceTextView.setText(task.getAddress());
        holder.startTimeTextView.setText(task.getStartingTime());
        holder.totalTimeTextView.setText("("+task.getFinalTime()+" mins.)");
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Deleting " + task.getAction(), Toast.LENGTH_SHORT).show();
                removeAt(holder.getAdapterPosition());
            }
        });
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productIntent = new Intent(context, AddServiceActivity.class);
                productIntent.putExtra(EXTRA_TASK_ID, task.getId());
                productIntent.putExtra(EXTRA_EDITING_TASK, true);
                context.startActivity(productIntent);
            }
        });
    }

    public void removeAt(int position) {
//        mData.get(position).setLocalQty(0);
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView taskActionTextView;
        TextView startTimeTextView;
        TextView totalTimeTextView;
        TextView taskPlaceTextView;
        //        TextView qtyTextView;
        ImageButton deleteBtn;
        ImageButton editBtn;

        ViewHolder(View itemView) {
            super(itemView);
           {
            taskActionTextView = itemView.findViewById(R.id.taskActionTextView);
            startTimeTextView = itemView.findViewById(R.id.taskStartTimeTextView);
            totalTimeTextView = itemView.findViewById(R.id.taskEndTimeTextView);
            taskPlaceTextView = itemView.findViewById(R.id.taskPlaceTextView);

//            qtyTextView = itemView.findViewById(R.id.itemQtyTextView);
            deleteBtn = itemView.findViewById(R.id.deleteTaskBtn);
            editBtn = itemView.findViewById(R.id.editTaskBtn);
            itemView.setOnClickListener(this);
        }
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Task getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(TasksAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}