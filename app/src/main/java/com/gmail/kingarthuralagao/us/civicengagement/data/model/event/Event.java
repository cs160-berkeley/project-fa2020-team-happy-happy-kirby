package com.gmail.kingarthuralagao.us.civicengagement.data.model.event;

import java.util.List;
import java.util.Map;

public class Event {

    private String name;
    private String dateStart;
    private String dateEnd;
    private String timeStart;
    private String timeEnd;
    private String description;
    private String location;
    private String timeZone;
    private Integer checkIns;
    private List<String> causes;
    private Map<String, Boolean> accessibilities;
    private Integer eventID;

    public Event() {
        // Empty Constructor for Firebase
    }

    public Event(String name, String dateStart, String dateEnd, String timeStart, String timeEnd,
                 String description, String location, String timeZone, Integer checkIns, List<String> causes,
                 Map<String, Boolean> accessibilities, Integer eventID) {
        this.name = name;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.description = description;
        this.location = location;
        this.timeZone = timeZone;
        this.checkIns = checkIns;
        this.causes = causes;
        this.accessibilities = accessibilities;
        this.eventID = eventID;
    }

    public Integer getID() {
        return eventID;
    }

    public void setID(Integer id) {
        this.eventID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Integer getCheckIns() {
        return checkIns;
    }

    public void setCheckIns(Integer checkIns) {
        this.checkIns = checkIns;
    }

    public List<String> getCauses() {
        return causes;
    }

    public void setCauses(List<String> causes) {
        this.causes = causes;
    }

    public Map<String, Boolean> getAccessibilities() {
        return accessibilities;
    }

    public void setAccessibilities(Map<String, Boolean> accessibilities) {
        this.accessibilities = accessibilities;
    }
}

