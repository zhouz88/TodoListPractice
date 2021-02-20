package com.zhezhe.todolist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zhezhe.todolist.models.Todo;
import com.zhezhe.todolist.utils.UIUtils;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.zhezhe.todolist.MainActivity.REQ_CODE_BACK;
import static com.zhezhe.todolist.TodoEditActivity.TODO_KEY;

public class TodoListAdapter extends RecyclerView.Adapter {
    private List<Todo> list;
    private UIUtils uiUtils = UIUtils.builder().build();

    @Builder
    public TodoListAdapter(List<Todo> list) {
        this.list = list;
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
        // todo set strike

        uiUtils.setTextViewStrikeThrough(holder.getTextView(), todo.isDone());
        holder.getCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                cb.setChecked(!todo.isDone());
                activity.updateTodo(i, !todo.isDone());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TodoEditActivity.class);
                intent.putExtra(TODO_KEY , todo);
                activity.startActivityForResult(intent, REQ_CODE_BACK);
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
