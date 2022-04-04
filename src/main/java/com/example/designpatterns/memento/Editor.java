package com.example.designpatterns.memento;

import org.slf4j.LoggerFactory;

/**
 * 编辑器
 */
public class Editor {
    //历史信息栈，先进后出，最大保存个数2个
    private final LimitStack history = new LimitStack(2);
    private String text;
    private int cursorBegin;
    private int cursorEnd;

    /**
     * 保存
     */
    public void save(){
        history.addLast(createSnapShot());
    }

    /**
     * 撤销
     */
    public void revoke(){
        SnapShot snapShot = history.popLast();
        if (snapShot != null) {
            restore(snapShot);
        }
    }

    /**
     * 还原到指定版本
     * @param snapShot
     */
    public void restore(SnapShot snapShot){
        LoggerFactory.getLogger("Editor.restore").info("{}", snapShot);
        setText(snapShot.getText());
        setCursorBegin(snapShot.getCursorBegin());
        setCursorEnd(snapShot.getCursorEnd());
    }

    public SnapShot createSnapShot(){
        SnapShot snapShot = new SnapShot(text, cursorBegin, cursorEnd);
        LoggerFactory.getLogger("Editor.createSnapShot").info("{}",snapShot);
        return snapShot;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.cursorBegin = text.length();
        this.cursorEnd = text.length();
        LoggerFactory.getLogger("Editor.setText").info("{}", text);
    }

    public int getCursorBegin() {
        return cursorBegin;
    }

    public void setCursorBegin(int cursorBegin) {
        this.cursorBegin = cursorBegin;
    }

    public int getCursorEnd() {
        return cursorEnd;
    }

    public void setCursorEnd(int cursorEnd) {
        this.cursorEnd = cursorEnd;
    }

    @Override
    public String toString() {
        return "Editor{" +
                "text='" + text + '\'' +
                ", cursorBegin=" + cursorBegin +
                ", cursorEnd=" + cursorEnd +
                '}';
    }

    /**
     * 先进后出的定长栈
     */
    private static class LimitStack {

        private final int count;
        private final SnapShot[] array;
        private int index=-1;

        private LimitStack(int count) {
            this.count = count;
            this.array = new SnapShot[count];
            LoggerFactory.getLogger("LimitStack").info("可保存的历史总数{}", count);
        }

        /**
         * 向队尾插入一个
         */
        public void addLast(SnapShot snapShot){
            index++;
            array[index%count] = snapShot;
            LoggerFactory.getLogger("LimitStack.addLast").info("{}", snapShot);
        }

        public SnapShot popLast(){
            int i = index % count;
            SnapShot snapShot = array[i];
            array[i] = null;
            index--;
            LoggerFactory.getLogger("LimitStack.popLast").info("{}", snapShot);
            return snapShot;
        }
    }
}
