package com.gmail.kingarthuralagao.us.civicengagement.data.model.event;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Event implements Serializable {

    private String name;
    private Long dateStart;
    private Long dateEnd;
    private String timeStart;
    private String city;
    private String timeEnd;
    private String description;
    private String location;
    private String timeZone;
    private Integer checkIns;
    private List<String> causes;
    private Map<String, Boolean> accessibilities;
    private String eventID;
    private String goFundMeLink;
    private List<String> keyWords;

    public Event() {
        // Empty Constructor for Firebase
    }

    public Event(String name, Long dateStart, Long dateEnd, String timeStart, String timeEnd,
                 String description, String location, String timeZone, Integer checkIns, List<String> causes,
                 Map<String, Boolean> accessibilities, String eventID, String city, String goFundMeLink, List<String> keyWords) {
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
        this.city = city;
        this.goFundMeLink = goFundMeLink;
        this.keyWords = keyWords;
    }

    public String getID() {
        return eventID;
    }

    public void setID(String id) {
        this.eventID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDateStart() {
        return dateStart;
    }

    public void setDateStart(Long dateStart) {
        this.dateStart = dateStart;
    }

    public Long getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Long dateEnd) {
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGoFundMeLink() {
        return goFundMeLink;
    }

    public void setGoFundMeLink(String goFundMeLink) {
        this.goFundMeLink = goFundMeLink;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }
}

