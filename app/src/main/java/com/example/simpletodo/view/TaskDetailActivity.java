package com.example.simpletodo.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.room.Room;

import com.example.simpletodo.R;
import com.example.simpletodo.data.local.TaskDatabase;
import com.example.simpletodo.data.models.Priority;
import com.example.simpletodo.data.models.TaskItem;
import com.example.simpletodo.databinding.ActivityTaskDetailBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static com.example.simpletodo.util.Constant.DB_NAME;

public class TaskDetailActivity extends AppCompatActivity {
    private ActivityTaskDetailBinding binding;
    private final Priority[] priorities = {
            Priority.LOW,
            Priority.MEDIUM,
            Priority.HIGH,
    };
    boolean isNewTask = false;
    TaskDatabase taskDatabase;
    TaskItem updateTaskItem;
    ArrayList<Priority> spinnerList = new ArrayList<>(Arrays.asList(priorities));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_detail);
        ArrayAdapter<Priority> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        taskDatabase = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();

        int task_id = getIntent().getIntExtra("id", -100);

        if (task_id == -100)
            isNewTask = true;

        if (!isNewTask) {
            fetchTaskById(task_id);
            binding.btnDelete.setVisibility(View.VISIBLE);
        }

        binding.btnDone.setOnClickListener(v -> {
            if (checkAllFieldFilled(binding)) {
                if (isNewTask) {
                    TaskItem taskItem = new TaskItem();
                    taskItem.title = binding.inTitle.getText().toString();
                    taskItem.description = binding.inDescription.getText().toString();
                    taskItem.dueDate = binding.inDisplayDate.getText().toString();
                    taskItem.priority = Priority.valueOf(binding.spinner.getSelectedItem().toString());

                    insertTask(taskItem);
                } else {
                    updateTaskItem.title = binding.inTitle.getText().toString();
                    updateTaskItem.description = binding.inDescription.getText().toString();
                    updateTaskItem.dueDate = binding.inDisplayDate.getText().toString();
                    updateTaskItem.priority = Priority.valueOf(binding.spinner.getSelectedItem().toString());

                    updateTask(updateTaskItem);
                }
            }
        });

        binding.btnDelete.setOnClickListener(v -> deleteTask(updateTaskItem));
        binding.btnCancel.setOnClickListener(v -> finish());
        binding.btnDueDate.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(TaskDetailActivity.this,
                (view, year, month, dayOfMonth) -> {
                    String date = (++month) + "/" + dayOfMonth + "/" + year;
                    binding.inDisplayDate.setText(date);
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show();
    }

    public static boolean checkAllFieldFilled(ActivityTaskDetailBinding binding) {
        if (TextUtils.isEmpty(binding.inTitle.getText().toString())) {
            binding.inTitle.setError("Title cannot be empty");
            return false;
        }
        if (TextUtils.isEmpty(binding.inDescription.getText().toString())) {
            binding.inDescription.setError("Task description cannot be empty");
            return false;
        }
        if (TextUtils.isEmpty(binding.inDisplayDate.getText().toString())) {
            binding.inDisplayDate.setError("Task due date cannot be empty");
            return false;
        }
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchTaskById(final int taskId) {
        new AsyncTask<Integer, Void, TaskItem>() {
            @Override
            protected TaskItem doInBackground(Integer... params) {

                return taskDatabase.taskDAO().fetchTaskItemListById(params[0]);

            }

            @Override
            protected void onPostExecute(TaskItem taskItem) {
                super.onPostExecute(taskItem);
                updateTaskItem = taskItem;
                binding.setTaskItem(updateTaskItem);
            }
        }.execute(taskId);

    }

    @SuppressLint("StaticFieldLeak")
    private void insertTask(TaskItem taskItem) {
        new AsyncTask<TaskItem, Void, Long>() {
            @Override
            protected Long doInBackground(TaskItem... params) {
                return taskDatabase.taskDAO().insertTaskItem(params[0]);
            }

            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);
                Intent intent = getIntent();
                intent.putExtra("isNew", true).putExtra("id", id);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(taskItem);

    }

    @SuppressLint("StaticFieldLeak")
    private void deleteTask(TaskItem taskItem) {
        new AsyncTask<TaskItem, Void, Integer>() {
            @Override
            protected Integer doInBackground(TaskItem... params) {
                return taskDatabase.taskDAO().deleteTaskItem(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isDeleted", true).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(taskItem);

    }


    @SuppressLint("StaticFieldLeak")
    private void updateTask(TaskItem taskItem) {
        new AsyncTask<TaskItem, Void, Integer>() {
            @Override
            protected Integer doInBackground(TaskItem... params) {
                return taskDatabase.taskDAO().updateTaskItem(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isNew", false).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(taskItem);
    }

}