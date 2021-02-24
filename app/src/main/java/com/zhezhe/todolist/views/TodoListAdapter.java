package com.zhezhe.todolist.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zhezhe.todolist.MainActivity;
import com.zhezhe.todolist.R;
import com.zhezhe.todolist.TodoEditActivity;
import com.zhezhe.todolist.models.Todo;
import com.zhezhe.todolist.utils.UIUtils;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.zhezhe.todolist.TodoEditActivity.TODO_KEY;

public final class TodoListAdapter extends RecyclerView.Adapter {
    private final List<Todo> list;
    private final int fragmentId;
    private final UIUtils uiUtils;

    @Builder
    public TodoListAdapter(List<Todo> list, int fragmentId, UIUtils uIutils) {
        this.list = list;
        this.fragmentId = fragmentId;
        this.uiUtils = uIutils;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return TodoHolder.builder().itemView(LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.main_list_item, viewGroup, false)).build();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final Todo todo = list.get(i);
        TodoHolder holder = (TodoHolder) viewHolder;
        holder.getCheckBox().setChecked(todo.isDone());
        holder.getTextView().setText(todo.getText());
        final MainActivity activity = (MainActivity) (holder.itemView.getContext());

        uiUtils.setTextViewStrikeThrough(holder.getTextView(), todo.isDone());
        holder.getCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // put
                CheckBox cb = (CheckBox) v;
                cb.setChecked(!todo.isDone());
                activity.updateTodo(i, !todo.isDone(),fragmentId);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TodoEditActivity.class);
                intent.putExtra(TODO_KEY, todo);
                activity.startActivityForResult(intent, fragmentId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Setter
    @Getter
    private static final class TodoHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView textView;

        @Builder
        public TodoHolder(@NonNull View itemView) {
            super(itemView);
            this.checkBox = itemView.findViewById(R.id.main_list_item_check);
            this.textView = itemView.findViewById(R.id.main_list_item_text);
        }
    }
}
