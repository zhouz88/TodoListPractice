package com.zhezhe.todolist.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhezhe.todolist.R;
import com.zhezhe.todolist.models.Todo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public final class TodoListFragment extends Fragment {

    public static TodoListFragment newInstance() {
        return new TodoListFragment();
    }

    @Getter
    @Setter
    private List<Todo> list;
    @Getter
    @Setter
    private TodoListAdapter todoListAdapter;
    @Getter
    @Setter
    private int id;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView importantView = view.findViewById(R.id.main_recycler_view);
        importantView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        importantView.setAdapter(todoListAdapter);
    }
}
