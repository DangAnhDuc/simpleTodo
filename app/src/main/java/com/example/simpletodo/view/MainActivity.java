package com.example.simpletodo.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.example.simpletodo.R;
import com.example.simpletodo.adapter.RecyclerTaskAdapter;
import com.example.simpletodo.callback.TaskCompletedCheckListener;
import com.example.simpletodo.callback.TaskItemClickListener;
import com.example.simpletodo.data.local.TaskDatabase;
import com.example.simpletodo.data.models.TaskItem;
import com.example.simpletodo.databinding.ActivityMainBinding;

import java.util.List;

import static com.example.simpletodo.util.Constant.DB_NAME;
import static com.example.simpletodo.util.Constant.NEW_TASK_REQUEST_CODE;
import static com.example.simpletodo.util.Constant.UPDATE_TASK_REQUEST_CODE;

public class MainActivity extends AppCompatActivity implements TaskItemClickListener, TaskCompletedCheckListener {
    private ActivityMainBinding binding;
    private TaskDatabase taskDatabase;
    private RecyclerTaskAdapter recyclerTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();
        taskDatabase = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
        loadAllTask();
        binding.fab.setOnClickListener(v -> startActivityForResult(new Intent(MainActivity.this, TaskDetailActivity.class), NEW_TASK_REQUEST_CODE));
    }

    private void initView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerTaskAdapter = new RecyclerTaskAdapter(this, this, this);
        binding.recyclerView.setAdapter(recyclerTaskAdapter);
    }

    @Override
    public void launchIntent(int id) {
        startActivityForResult(new Intent(MainActivity.this, TaskDetailActivity.class).putExtra("id", id), UPDATE_TASK_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_TASK_REQUEST_CODE) {
                assert data != null;
                long id = data.getLongExtra("id", -1);
                Toast.makeText(getApplicationContext(), "Task has been added", Toast.LENGTH_SHORT).show();
                fetchTaskAndAdd((int) id);
            } else if (requestCode == UPDATE_TASK_REQUEST_CODE) {
                assert data != null;
                boolean isDeleted = data.getBooleanExtra("isDeleted", false);
                int number = data.getIntExtra("number", -1);
                if (isDeleted) {
                    Toast.makeText(getApplicationContext(), number + " task has been deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), number + " task has been updated", Toast.LENGTH_SHORT).show();
                }
                loadAllTask();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void loadAllTask() {
        new AsyncTask<String, Void, List<TaskItem>>() {
            @Override
            protected List<TaskItem> doInBackground(String... params) {
                return taskDatabase.taskDAO().fetchAllTaskItem();
            }

            @Override
            protected void onPostExecute(List<TaskItem> taskItemList) {
                recyclerTaskAdapter.updateTaskList(taskItemList);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchTaskAndAdd(int id) {
        new AsyncTask<Integer, Void, TaskItem>() {
            @Override
            protected TaskItem doInBackground(Integer... params) {
                return taskDatabase.taskDAO().fetchTaskItemListById(params[0]);

            }

            @Override
            protected void onPostExecute(TaskItem taskItemList) {
                recyclerTaskAdapter.addRow(taskItemList);
            }
        }.execute(id);
    }


    @Override
    @SuppressLint("StaticFieldLeak")
    public void onTaskCompletedCheck(boolean isChecked, int position, TaskItem taskItem) {
        new AsyncTask<TaskItem, Void, Integer>() {
            @Override
            protected Integer doInBackground(TaskItem... params) {
                return taskDatabase.taskDAO().updateTaskItem(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);
            }
        }.execute(taskItem);
    }
}