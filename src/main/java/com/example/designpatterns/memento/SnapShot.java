package com.example.designpatterns.memento;

import java.time.LocalDateTime;

public class SnapShot {
    private final String text;
    private final int cursorBegin;
    private final int cursorEnd;
    private final LocalDateTime creatTime;

    SnapShot(String text, int cursorBegin, int cursorEnd){
        this.text = text;
        this.cursorBegin = cursorBegin;
        this.cursorEnd = cursorEnd;
        creatTime = LocalDateTime.now();
    }

    public LocalDateTime getCreatTime() {
        return creatTime;
    }

    public String getText() {
        return text;
    }

    public int getCursorBegin() {
        return cursorBegin;
    }

    public int getCursorEnd() {
        return cursorEnd;
    }

    @Override
    public String toString() {
        return "SnapShot{" +
                "text='" + text + '\'' +
                ", cursorBegin=" + cursorBegin +
                ", cursorEnd=" + cursorEnd +
                ", creatTime=" + creatTime +
                '}';
    }
}
