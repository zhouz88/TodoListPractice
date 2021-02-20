package com.zhezhe.todolist.utils;

import android.graphics.Paint;
import android.widget.TextView;

import lombok.Builder;

@Builder
public final class UIUtils {
    /**
     *  Set up strike through
     * @param tv textview
     * @param strike boolean value
     */
    public void setTextViewStrikeThrough(TextView tv, boolean strike) {
        if (strike) {
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tv.setPaintFlags(tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}
