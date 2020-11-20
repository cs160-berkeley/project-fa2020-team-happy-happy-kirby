package com.gmail.kingarthuralagao.us.civicengagement.data.model.accessibility;

public class AccessibilityAvailability {

    private String resource;
    private Boolean isAvailable;

    public AccessibilityAvailability() {
    }

    public AccessibilityAvailability(String r, Boolean available) {
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
