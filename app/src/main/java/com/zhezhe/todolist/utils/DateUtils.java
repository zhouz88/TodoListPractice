package com.zhezhe.todolist.utils;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {
    private static final DateFormat dateFormat =
            new SimpleDateFormat("yyyy MM dd HH:mm", Locale.getDefault());

    // "Wed, 09 30, 1980"
    private static final DateFormat dateFormatDate = new
            SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());

    //12:13
    private static final DateFormat dateFormatTime = new
            SimpleDateFormat("HH:mm", Locale.getDefault());

    @NonNull
    public static Date stringToDate(String string) {
        try {
            return dateFormat.parse(string);
        } catch (ParseException e) {
            return Calendar.getInstance().getTime();
        }
    }

    @NonNull
    public static String dateToString(Date date) {
        return dateFormat.format(date);
    }

    @NonNull
    public static String dateToStringDate(Date date) {
        return dateFormatDate.format(date);
    }

    @NonNull
    public static String dateToStringTime(Date date) {
        return dateFormatTime.format(date);
    }
}
