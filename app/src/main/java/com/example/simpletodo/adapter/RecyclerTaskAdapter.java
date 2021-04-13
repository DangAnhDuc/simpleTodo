package com.example.simpletodo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpletodo.R;
import com.example.simpletodo.data.models.TaskItem;
import com.example.simpletodo.databinding.TaskItemRowLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class RecyclerTaskAdapter extends RecyclerView.Adapter<RecyclerTaskAdapter.ViewHolder> {
    List<TaskItem> taskItemList;
    Context context;
    private RecyclerTaskAdapter.TaskClickListener taskClickListener;


    public RecyclerTaskAdapter(Context context,TaskClickListener taskClickListener) {
        this.taskItemList= new ArrayList<>();
        this.context= context;
        this.taskClickListener = taskClickListener;
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
        TaskItemRowLayoutBinding taskItemRowLayoutBinding= DataBindingUtil.inflate(inflater, R.layout.task_item_row_layout,parent,false);
        return new ViewHolder(taskItemRowLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(taskItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return taskItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TaskItemRowLayoutBinding taskItemRowLayoutBinding;

        public ViewHolder(TaskItemRowLayoutBinding taskItemRowLayoutBinding){
            super(taskItemRowLayoutBinding.getRoot());
            this.taskItemRowLayoutBinding= taskItemRowLayoutBinding;
        }

        public void bind(TaskItem taskItem){
            taskItemRowLayoutBinding.setTaskItem(taskItem);
            taskItemRowLayoutBinding.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskClickListener.launchIntent(taskItemList.get(getAdapterPosition()).taskId);
                }
            });
            taskItemRowLayoutBinding.executePendingBindings();
        }
    }

    public interface TaskClickListener {
        void launchIntent(int id);
    }
}
