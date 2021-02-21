package com.zhezhe.todolist;

import com.zhezhe.todolist.utils.UIUtils;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class TodoApplication extends android.app.Application {
    @Getter
    private final Map<Integer, UIUtils> singletons;
    public static final int UI_UTIL = 0;

    public TodoApplication() {
        this.singletons = new HashMap<>();
        singletons.put(UI_UTIL, UIUtils.builder().build());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
