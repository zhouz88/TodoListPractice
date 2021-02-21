package com.zhezhe.todolist;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.gson.reflect.TypeToken;
import com.zhezhe.todolist.models.ModelUtils;
import com.zhezhe.todolist.models.Todo;
import com.zhezhe.todolist.views.TodoListAdapter;
import com.zhezhe.todolist.views.TodoListFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.Builder;

import static com.zhezhe.todolist.MainActivity.TODOS;

public final class TodoFragmentPagerAdapter extends FragmentPagerAdapter {

    private final Context context;
    private final Map<Integer, Fragment> map = new HashMap<>(4);

    @Builder
    public TodoFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        if (map.containsKey(i)) {
            return map.get(i);
        }
        List<Todo> list = ModelUtils.read(context, TODOS+i, new TypeToken<List<Todo>>() {});
        if (Objects.isNull(list)) {
            list = new ArrayList<>();
        }
        TodoListFragment fragment = TodoListFragment
                .newInstance()
                .list(list)
                .id(i)
                .todoListAdapter(TodoListAdapter.builder().list(list).build());
        map.put(i, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Todo " + (position + 1);
    }
}
