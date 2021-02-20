package com.zhezhe.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.zhezhe.todolist.models.ModelUtils;
import com.zhezhe.todolist.models.Todo;
import com.zhezhe.todolist.views.TodoListAdapter;
import com.zhezhe.todolist.views.TodoListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.zhezhe.todolist.TodoEditActivity.DELETE_ID;
import static com.zhezhe.todolist.TodoEditActivity.POST_OR_PUT_ID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQ_CODE_BACK = 100;
    public final String TODOS = "TODOS";

    private List<Todo> list;

    private FloatingActionButton fab;
    private TodoListFragment fragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.fab = this.findViewById(R.id.fab);
        this.list = ModelUtils.read(this, TODOS, new TypeToken<List<Todo>>() {
        });
        if (Objects.isNull(list)) {
            this.list = new ArrayList<>();
        }
        fab.setOnClickListener(this);
        fragment = TodoListFragment
                .newInstance()
                .list(list)
                .todoListAdapter(TodoListAdapter.builder().list(list).build());

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,
                        fragment).commit();
    }

    @Override
    public void onClick(View v) {
        if (v == fab) {
            Intent intent = new Intent(this, TodoEditActivity.class);
            startActivityForResult(intent, REQ_CODE_BACK);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQ_CODE_BACK && resultCode == RESULT_OK) {
            String todoId = intent.getStringExtra(DELETE_ID);
            if (Objects.nonNull(todoId)) {
                // Delete from other activites
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId().equals(todoId)) {
                        list.remove(i);
                        break;
                    }
                }
                fragment.todoListAdapter().notifyDataSetChanged();
                ModelUtils.save(this, TODOS, list);
            } else {
                // Post or Put  // put from other activites
                boolean found = false;
                Todo todo = intent.getParcelableExtra(POST_OR_PUT_ID);
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId().equals(todo.getId())) {
                        found = true;
                        list.set(i, todo);
                        break;
                    }
                }
                if (!found) {
                    list.add(todo);
                }
                fragment.todoListAdapter().notifyDataSetChanged();
                ModelUtils.save(this, TODOS, list);
            }
        }
    }

    public void updateTodo(int i, boolean status) {
        // Put from this activity
        list.get(i).setDone(status);
        fragment.todoListAdapter().notifyDataSetChanged();
        ModelUtils.save(this, TODOS, list);
    }
}
