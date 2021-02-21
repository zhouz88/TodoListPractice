package com.zhezhe.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zhezhe.todolist.models.ModelUtils;
import com.zhezhe.todolist.models.Todo;
import com.zhezhe.todolist.views.TodoListFragment;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.zhezhe.todolist.TodoApplication.UI_UTIL;
import static com.zhezhe.todolist.TodoEditActivity.DELETE_ID;
import static com.zhezhe.todolist.TodoEditActivity.POST_OR_PUT_ID;

public final class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TODOS = "TODOS";
    public static final int REQ_CODE_BACK_ONE = 0;
    public static final int REQ_CODE_BACK_TWO = 1;
    public static final int REQ_CODE_BACK_THREE = 2;
    public static final int REQ_CODE_BACK_FOUR = 3;
    private static final Set<Integer> set;

    static {
        set = new HashSet<>(
                Arrays.asList(REQ_CODE_BACK_ONE, REQ_CODE_BACK_TWO, REQ_CODE_BACK_THREE, REQ_CODE_BACK_FOUR));
    }

    private FloatingActionButton fab;
    private ViewPager viewPager;
    private TodoFragmentPagerAdapter viewPagerAdapter;
    private Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setPopupTheme(R.style.ToolbarOverlay);
        toolbar.setElevation(R.dimen.spacing_xxsmall);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(toolbar);

        this.fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        viewPagerAdapter =
                TodoFragmentPagerAdapter.builder()
                        .context(this)
                        .fragmentManager(getSupportFragmentManager())
                        .uiUtils(((TodoApplication) this.getApplication()).getSingletons().get(UI_UTIL))
                        .build();
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (set.contains(requestCode) && resultCode == RESULT_OK) {
            TodoListFragment fragment =  ((TodoListFragment) viewPagerAdapter.getItem(requestCode));
            List<Todo> list = fragment.list();
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
                ModelUtils.save(this, TODOS + requestCode, list);
            } else {
                // Post or Put  // put from other activities
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
                ModelUtils.save(this, TODOS + requestCode, list);
            }
        }
    }

    public void updateTodo(int i, boolean status, int fragmentId) {
        // Put from this activity
        TodoListFragment fragment =  ((TodoListFragment) viewPagerAdapter.getItem(fragmentId));
        List<Todo> list = fragment.list();
        list.get(i).setDone(status);
        fragment.todoListAdapter().notifyDataSetChanged();
        ModelUtils.save(this, TODOS + fragmentId, list);

    }

    @Override
    public void onClick(View v) {
        if (v == fab) {
            Intent intent = new Intent(this, TodoEditActivity.class);
            startActivityForResult(intent, viewPager.getCurrentItem());
        }
    }
}
