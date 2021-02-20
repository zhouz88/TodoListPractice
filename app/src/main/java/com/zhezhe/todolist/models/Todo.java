package com.zhezhe.todolist.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Todo implements Parcelable {

    private String id;

    private String text;

    private boolean done = false;

    private Date remindDate;

    public Todo(String text, Date remindDate) {
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.remindDate = remindDate;
    }

    protected Todo(Parcel in) {
        id = in.readString();
        text = in.readString();
        done = in.readByte() != 0;
        this.remindDate = (in.readLong() == -1 ? null : new Date(in.readLong()));
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(text);
        dest.writeByte((byte) (done ? 1 : 0));
        dest.writeLong(remindDate == null ? -1 : remindDate.getTime());
    }
}
