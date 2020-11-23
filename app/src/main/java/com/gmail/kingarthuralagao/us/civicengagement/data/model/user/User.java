package com.gmail.kingarthuralagao.us.civicengagement.data.model.user;

import java.util.List;
import java.util.Map;

public class User {
    private String name;
    private String location;
    private List<String> causes;
    private List<Integer> events;
    private List<Integer> checkins;
    private Map<String, Boolean> accessibilities;
    private Integer userID;

    public User() {

    }

    public User(String name, String location, List<String> causes, List<Integer> events, List<Integer> checkins,
                 Map<String, Boolean> accessibilities, Integer userID) {
        this.name = name;
        this.location = location;
        this.events = events;
        this.checkins = checkins;
        this.causes = causes;
        this.accessibilities = accessibilities;
        this.userID = userID;
    }

    public Integer getID() {
        return userID;
    }

    public void setID(Integer id) {
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

    public List<Integer> getEvents() {
        return events;
    }

    public void setEvents(List<Integer> events) {
        this.events = events;
    }

    public List<Integer> getCheckins() {
        return checkins;
    }

    public void setCheckins(List<Integer> checkins) {
        this.checkins = checkins;
    }

    public Map<String, Boolean> getAccessibilities() {
        return accessibilities;
    }

    public void setAccessibilities(Map<String, Boolean> accessibilities) {
        this.accessibilities = accessibilities;
    }
}


