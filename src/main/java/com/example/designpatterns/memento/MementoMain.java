package com.example.designpatterns.memento;

/**
 * 备忘录模式
 */
public class MementoMain {
    public static void main(String[] args) {
        Editor editor = new Editor();
        editor.setText("大家好！");
        editor.save();
        editor.setText(editor.getText()+"我是");
        editor.save();
        editor.setText(editor.getText()+"张三");
        editor.save();
        editor.setText(editor.getText()+"，法外狂徒");
        editor.revoke();
        editor.revoke();
        editor.revoke();
    }
}
