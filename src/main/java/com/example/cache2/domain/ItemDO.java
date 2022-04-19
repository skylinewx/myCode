package com.example.cache2.domain;

import java.util.Objects;

public class ItemDO {
    private String id;
    private String groupId;
    private String title;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

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
        return "ItemDO{" +
                "id='" + id + '\'' +
                ", groupId='" + groupId + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDO itemDO = (ItemDO) o;
        return id.equals(itemDO.id) && groupId.equals(itemDO.groupId) && Objects.equals(title, itemDO.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, title);
    }
}
