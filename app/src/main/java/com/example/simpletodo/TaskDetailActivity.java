package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.simpletodo.data.local.TaskDatabase;
import com.example.simpletodo.data.models.Priority;
import com.example.simpletodo.data.models.TaskItem;
import com.example.simpletodo.databinding.ActivityTaskDetailBinding;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.simpletodo.util.Constant.DB_NAME;

public class TaskDetailActivity extends AppCompatActivity {
    private ActivityTaskDetailBinding activityTaskDetailBinding;
    private Priority[] priorities = {
            Priority.LOW,
            Priority.MEDIUM,
            Priority.HIGH,
    };
    boolean isNewTodo = false;
    TaskDatabase taskDatabase;
    TaskItem taskItem;
    ArrayList<Priority> spinnerList = new ArrayList<>(Arrays.asList(priorities));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTaskDetailBinding= DataBindingUtil.setContentView(this, R.layout.activity_task_detail);
        setSupportActionBar(activityTaskDetailBinding.toolbar);
        ArrayAdapter<Priority> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityTaskDetailBinding.spinner.setAdapter(adapter);
        taskDatabase = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();

        int task_id = getIntent().getIntExtra("id", -100);

        if (task_id == -100)
            isNewTodo = true;

        if (!isNewTodo) {
            fetchTodoById(task_id);
            btnDelete.setVisibility(View.VISIBLE);
        }
    }
}