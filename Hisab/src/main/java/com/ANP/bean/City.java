package com.ANP.bean;

import com.ANP.util.ANPUtils;

public class City {

    private long id;
    private String name;


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        if (ANPUtils.isNullOrEmpty(this.name)) {
            return (this.name.toUpperCase());
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}