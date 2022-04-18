package com.example.cache.domain;

import java.util.Objects;

public class MyObj {
    private String id;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MyObj{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof MyObj){
            MyObj myObj = (MyObj) o;
            return id.equals(myObj.id) && Objects.equals(content, myObj.content);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content);
    }
}
