package com.gmail.kingarthuralagao.us.civicengagement.data.model.user;

import java.util.List;
import java.util.Map;

public class User {
    private String name;
    private String location;
    private List<String> causes;
    private List<String> events;
    private List<String> checkIns;
    private Map<String, Boolean> accessibilities;
    private String userID;

    public User() {

    }

    public User(String name, String location, List<String> causes, List<String> events, List<String> checkIns,
                 Map<String, Boolean> accessibilities, String userID) {
        this.name = name;
        this.location = location;
        this.events = events;
        this.checkIns = checkIns;
        this.causes = causes;
        this.accessibilities = accessibilities;
        this.userID = userID;
    }

    public String getID() {
        return userID;
    }

    public void setID(String id) {
        this.userID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getCauses() {
        return causes;
    }

    public void setCauses(List<String> causes) {
        this.causes = causes;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public List<String> getCheckIns() {
        return checkIns;
    }

    public void setCheckIns(List<String> checkIns) {
        this.checkIns = checkIns;
    }

    public Map<String, Boolean> getAccessibilities() {
        return accessibilities;
    }

    public void setAccessibilities(Map<String, Boolean> accessibilities) {
        this.accessibilities = accessibilities;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}


