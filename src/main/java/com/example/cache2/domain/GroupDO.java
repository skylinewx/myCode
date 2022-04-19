package com.example.cache2.domain;

import java.util.Objects;

public class GroupDO {
    private String id;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "GroupDO{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof GroupDO){
            GroupDO groupDO = (GroupDO) o;
            return id.equals(groupDO.id) && Objects.equals(title, groupDO.title);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
