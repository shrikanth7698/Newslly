package com.shrikanthravi.newslly;

/**
 * Created by shrikanthravi on 18/12/17.
 */

public class Category {
    String name;
    String selected;

    public Category(String name, String selected) {
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
