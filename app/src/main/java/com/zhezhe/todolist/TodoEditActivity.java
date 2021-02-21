package com.zhezhe.todolist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhezhe.todolist.models.Todo;
import com.zhezhe.todolist.utils.DateUtils;
import com.zhezhe.todolist.utils.UIUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.zhezhe.todolist.TodoApplication.UI_UTIL;

public final class TodoEditActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public static final String DELETE_ID = "FROM_TODO_ACTIVITY_DELETE";
    public static final String POST_OR_PUT_ID = "FROM_TODO_ACTIVITY";
    public static final String TODO_KEY = "TODO_KEY";

    private EditText todoEdit;
    private TextView date;
    private TextView time;
    private CheckBox completedCheckBox;
    private TextView deleteButton;
    private FloatingActionButton fab;
    private LinearLayout checkboxAndTextView;
    private Toolbar toolbar;

    private Todo todo;
    private Date remindDate;
    private UIUtils uiUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todo = getIntent().getParcelableExtra(TODO_KEY);
        if (Objects.nonNull(todo)) {
            remindDate = todo.getRemindDate();
        }
        uiUtils = ((TodoApplication) this.getApplication()).getSingletons().get(UI_UTIL);
        setContentView(R.layout.edit);
        todoEdit = findViewById(R.id.edit_text);
        date = findViewById(R.id.todo_detail_date);
        time = findViewById(R.id.todo_detail_time);
        completedCheckBox = findViewById(R.id.todo_detail_complete);
        deleteButton = findViewById(R.id.todo_delete);
        fab = findViewById(R.id.fab);
        checkboxAndTextView = findViewById(R.id.todo_detail_complete_wrapper);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setTitle(null);
        setSupportActionBar(toolbar);

        if (Objects.nonNull(todo)) {
            //初始化 数据
            todoEdit.setText(todo.getText());
            uiUtils.setTextViewStrikeThrough(todoEdit, todo.isDone());
            completedCheckBox.setChecked(todo.isDone());
            deleteButton.setOnClickListener(this);
        } else {
            deleteButton.setVisibility(View.GONE);
        }

        if (Objects.isNull(remindDate)) {
            date.setText(R.string.set_date);
            time.setText(R.string.set_time);
        } else {
            date.setText(DateUtils.dateToStringDate(remindDate));
            time.setText(DateUtils.dateToStringTime(remindDate));
        }

        date.setOnClickListener(this);
        time.setOnClickListener(this);
        completedCheckBox.setOnCheckedChangeListener(this);
        checkboxAndTextView.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == deleteButton) {
            // delele

            Intent intent = new Intent();
            intent.putExtra(DELETE_ID, todo.getId());
            setResult(RESULT_OK, intent);
            finish();
        } else if (v == date) {
            final Calendar calendar = Calendar.getInstance();
            if (Objects.nonNull(remindDate)) {
                calendar.setTime(remindDate);
            }
            Dialog dialog = new DatePickerDialog(TodoEditActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        if (remindDate != null) {
                            calendar.setTime(remindDate);
                        }
                        calendar.set(year, month, dayOfMonth);
                        remindDate = calendar.getTime();
                        date.setText(DateUtils.dateToStringDate(remindDate));
                    }, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONDAY),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        } else if (v == time) {
            final Calendar cur = Calendar.getInstance();
            if (remindDate != null) {
                cur.setTime(remindDate);
            }
            Dialog dialog = new TimePickerDialog(TodoEditActivity.this,
                    (view, hourOfDay, minute) -> {
                        if (remindDate != null) {
                            cur.setTime(remindDate);
                        }
                        cur.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cur.set(Calendar.MINUTE, minute);
                        this.remindDate = cur.getTime();
                        time.setText(DateUtils.dateToStringTime(this.remindDate));
                    }
                    , cur.get(Calendar.HOUR),
                    cur.get(Calendar.MINUTE), false);
            dialog.show();
        } else if (v == checkboxAndTextView) {
            boolean check = !completedCheckBox.isChecked();
            completedCheckBox.setChecked(check);
        } else if (v == fab) {
            if (Objects.isNull(todo)) {
                // post
                todo = Todo.builder()
                        .text(todoEdit.getText().toString())
                        .remindDate(remindDate)
                        .build();
            } else {
                //put
                todo.setText(todoEdit.getText().toString());
                todo.setRemindDate(remindDate);
            }
            todo.setDone(completedCheckBox.isChecked());
            Intent intent = new Intent();
            intent.putExtra(POST_OR_PUT_ID, todo);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        uiUtils.setTextViewStrikeThrough(todoEdit, isChecked);
        todoEdit.setTextColor(isChecked ? Color.GRAY : Color.WHITE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
