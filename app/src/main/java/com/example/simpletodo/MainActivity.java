package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.simpletodo.adapter.RecyclerTaskAdapter;
import com.example.simpletodo.adapter.RecyclerTaskAdapter.TaskClickListener;
import com.example.simpletodo.data.local.TaskDatabase;
import com.example.simpletodo.data.models.Priority;
import com.example.simpletodo.data.models.TaskItem;
import com.example.simpletodo.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import static com.example.simpletodo.util.Constant.DB_NAME;

public class MainActivity extends AppCompatActivity implements  TaskClickListener {
    private ActivityMainBinding binding;
    private TaskDatabase taskDatabase;
    private RecyclerTaskAdapter recyclerTaskAdapter;
    private static Context mContext;
    public static final int NEW_TASK_REQUEST_CODE = 200;
    public static final int UPDATE_TASK_REQUEST_CODE = 300;
    ArrayList<TaskItem> todoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mContext = this;

        initView();
        taskDatabase = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();

        checkIfAppLaunchedFirstTime();

    }

    private void initView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerTaskAdapter= new RecyclerTaskAdapter(this,this);
        binding.recyclerView.setAdapter(recyclerTaskAdapter);
    }

    public static Context getContext() {
        return mContext;
    }

    private void buildDummyTodos() {
        TaskItem todo = new TaskItem();
        todo.subject = "Android Retrofit Tutorial";
        todo.description = "Cover a tutorial on the Retrofit networking library using a RecyclerView to show the data.";
        todo.priority = Priority.HIGH;

        todoArrayList.add(todo);

        todo = new TaskItem();
        todo.subject = "iOS TableView Tutorial";
        todo.description = "Covers the basics of TableViews in iOS using delegates.";
        todo.priority = Priority.MEDIUM;

        todoArrayList.add(todo);

        todo = new TaskItem();
        todo.subject = "Kotlin Arrays";
        todo.description = "Cover the concepts of Arrays in Kotlin and how they differ from the Java ones.";
        todo.priority = Priority.MEDIUM;

        todoArrayList.add(todo);

        todo = new TaskItem();
        todo.subject = "Swift Arrays";
        todo.description = "Cover the concepts of Arrays in Swift and how they differ from the Java and Kotlin ones.";
        todo.priority = Priority.LOW;

        todoArrayList.add(todo);
        insertList(todoArrayList);
    }

    @SuppressLint("StaticFieldLeak")
    private void insertList(List<TaskItem> todoList) {
        AsyncTask<List<TaskItem>, Void, Void> asyncTask = new AsyncTask<List<TaskItem>, Void, Void>() {
            @Override
            protected Void doInBackground(List<TaskItem>... params) {
                taskDatabase.taskDAO().insertTaskItemList(params[0]);
                return null;
            }
            @Override
            public void onPostExecute(Void voids) {
                super.onPostExecute(voids);
                loadAllTodos();
            }
        }.execute(todoList);

    }

    @SuppressLint("StaticFieldLeak")
    private void loadAllTodos() {
        new AsyncTask<String, Void, List<TaskItem>>() {
            @Override
            protected List<TaskItem> doInBackground(String... params) {
                return taskDatabase.taskDAO().fetchAllTaskItem();
            }

            @Override
            protected void onPostExecute(List<TaskItem> todoList) {
                recyclerTaskAdapter.updateTaskList(todoList);
            }
        }.execute();
    }

    private void checkIfAppLaunchedFirstTime() {
        final String PREFS_NAME = "SharedPrefs";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("firstTime", true)) {
            settings.edit().putBoolean("firstTime", false).apply();
            buildDummyTodos();
        } else {
            loadAllTodos();
        }
    }

    @Override
    public void launchIntent(int id) {
        startActivityForResult(new Intent(MainActivity.this, TaskDetailActivity.class).putExtra("id", id), UPDATE_TASK_REQUEST_CODE);
    }

}