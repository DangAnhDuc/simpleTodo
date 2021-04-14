package com.example.simpletodo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpletodo.R;
import com.example.simpletodo.callback.TaskCompletedCheckListener;
import com.example.simpletodo.callback.TaskItemClickListener;
import com.example.simpletodo.data.models.TaskItem;
import com.example.simpletodo.databinding.TaskItemRowLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class RecyclerTaskAdapter extends RecyclerView.Adapter<RecyclerTaskAdapter.ViewHolder> {
    List<TaskItem> taskItemList;
    Context context;
    private final TaskItemClickListener taskItemClickListener;
    private final TaskCompletedCheckListener taskCompletedCheckListener;


    public RecyclerTaskAdapter(Context context, TaskItemClickListener taskClickListener, TaskCompletedCheckListener taskCompletedCheckListener) {
        this.taskItemList = new ArrayList<>();
        this.context = context;
        this.taskItemClickListener = taskClickListener;
        this.taskCompletedCheckListener = taskCompletedCheckListener;
    }

    public void updateTaskList(List<TaskItem> data) {
        taskItemList.clear();
        taskItemList.addAll(data);
        notifyDataSetChanged();
    }

    public void addRow(TaskItem taskItem) {
        taskItemList.add(taskItem);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        TaskItemRowLayoutBinding taskItemRowLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.task_item_row_layout, parent, false);
        return new ViewHolder(taskItemRowLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(taskItemList.get(position), position + 1);
        holder.taskItemRowLayoutBinding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            taskCompletedCheckListener.onTaskCompletedCheck(isChecked,
                    position, taskItemList.get(position));
            taskItemList.get(position).setCompleted(isChecked);
        });

    }

    @Override
    public int getItemCount() {
        return taskItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TaskItemRowLayoutBinding taskItemRowLayoutBinding;

        public ViewHolder(TaskItemRowLayoutBinding taskItemRowLayoutBinding) {
            super(taskItemRowLayoutBinding.getRoot());
            this.taskItemRowLayoutBinding = taskItemRowLayoutBinding;
        }

        public void bind(TaskItem taskItem, int position) {
            taskItemRowLayoutBinding.setTaskItem(taskItem);
            taskItemRowLayoutBinding.setPosition(position);
            taskItemRowLayoutBinding.cardView.setOnClickListener(v -> taskItemClickListener.launchIntent(taskItemList.get(getAdapterPosition()).taskId));
            taskItemRowLayoutBinding.executePendingBindings();
        }
    }

}
