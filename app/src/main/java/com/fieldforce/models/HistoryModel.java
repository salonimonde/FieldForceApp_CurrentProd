package com.fieldforce.models;

import java.io.Serializable;

public class HistoryModel implements Serializable {
    public String date;
    public String open;
    public String completed;
    public String screen;

    public String name;
    public String nscId;
    public String area;
    public String status;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public void setscreen(String screen) {
        this.screen = screen;
    }

    public String getScreen() {
        return screen;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNscId(String nscId) {
        this.nscId = nscId;
    }

    public String getNscId() {
        return nscId;
    }

    public void setArea(String area) {
        this.area = area;
    }
    public String getArea() {
        return area;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }


}