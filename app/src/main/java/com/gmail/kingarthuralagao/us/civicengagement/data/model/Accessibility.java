package com.gmail.kingarthuralagao.us.civicengagement.data.model;

public class Accessibility {

    private String resource;
    private Boolean isAvailable;

    public Accessibility() {
    }

    public Accessibility(String r, Boolean available) {
        resource = r;
        isAvailable = available;
    }

    public String getResource() {
        return resource;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }
}
