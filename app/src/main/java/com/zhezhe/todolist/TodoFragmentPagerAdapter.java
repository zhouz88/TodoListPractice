package com.zhezhe.todolist;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.gson.reflect.TypeToken;
import com.zhezhe.todolist.models.ModelUtils;
import com.zhezhe.todolist.models.Todo;
import com.zhezhe.todolist.utils.UIUtils;
import com.zhezhe.todolist.views.TodoListAdapter;
import com.zhezhe.todolist.views.TodoListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Builder;

import static com.zhezhe.todolist.MainActivity.TODOS;

public final class TodoFragmentPagerAdapter extends FragmentPagerAdapter {

    private final Context context;
    private final TodoListFragment[] map = new TodoListFragment[4];
    private final UIUtils uiUtils;

    @Builder
    public TodoFragmentPagerAdapter(FragmentManager fragmentManager, Context context, UIUtils uiUtils) {
        super(fragmentManager);
        this.context = context;
        this.uiUtils = uiUtils;
    }

    @Override
    public Fragment getItem(int i) {
        if (Objects.nonNull(map[i])) {
            return map[i];
        }
        List<Todo> list = ModelUtils.read(context, TODOS+i, new TypeToken<List<Todo>>() {});
        if (Objects.isNull(list)) {
            list = new ArrayList<>();
        }
        TodoListFragment fragment = TodoListFragment
                .newInstance()
                .list(list)
                .id(i)
                .todoListAdapter(
                        TodoListAdapter
                        .builder()
                        .list(list)
                        .fragmentId(i)
                        .uIutils(uiUtils)
                        .build());
        map[i] = fragment;
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
