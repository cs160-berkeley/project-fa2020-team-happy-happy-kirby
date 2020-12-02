package com.gmail.kingarthuralagao.us.civicengagement.data.model.event;

import android.util.Log;

import com.gmail.kingarthuralagao.us.civicengagement.core.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/* Just a POJO used for creating an Event */
public class EventBuilder {

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
    private Event eventDTO;
    private String goFundMeLink;

    public EventBuilder withName(String eventName) {
        this.name = eventName;
        return this;
    }

    public EventBuilder withDateStart(Long timeStamp) {
        this.dateStart = timeStamp;
        return this;
    }

    public EventBuilder withDateStart(String dateStart, String timeStart) {
        String[] dateParts = dateStart.split("/");
        String month = dateParts[0];
        String day = dateParts[1];
        String year = dateParts[2];

        month = month.length() == 2 ? month : "0" + month;
        day = day.length() == 2 ? day : "0" + day;

        String[] timeParts = timeStart.split(":");
        String hour = timeParts[0];
        String minute = timeParts[1];

        hour = hour.length() == 2 ? hour : "0" + hour;

        String date = month + "/" + day + "/" + year;
        String time = 12 + ":" + "00" + ":" + "00";
        this.dateStart = Utils.getTimeStampFromDate(date + " " + time);
        return this;
    }

    public EventBuilder withDateEnd(Long timeStamp) {
        this.dateEnd = timeStamp;
        return this;
    }

    public EventBuilder withDateEnd(String dateEnd, String timeEnd) {
        String[] dateParts = dateEnd.split("/");
        String month = dateParts[0];
        String day = dateParts[1];
        String year = dateParts[2];

        month = month.length() == 2 ? month : "0" + month;
        day = day.length() == 2 ? day : "0" + day;

        String[] timeParts = timeEnd.split(":");
        String hour = timeParts[0];
        String minute = timeParts[1];

        hour = hour.length() == 2 ? hour : "0" + hour;

        String date = month + "/" + day + "/" + year;
        String time = 12 + ":" + "00" + ":" + "00";
        Log.i("EventBuilder", date + " " + time);
        this.dateEnd = Utils.getTimeStampFromDate(date + " " + time);
        return this;
    }

    public EventBuilder withTimeStart(String time) {
        this.timeStart = time;
        return this;
    }


    public EventBuilder withTimeEnd(String time) {
        this.timeEnd = time;
        return this;
    }

    public EventBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public EventBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public EventBuilder withLocation(String location) {
        this.location = location;
        return this;
    }

    public EventBuilder withTimeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public EventBuilder withCheckIns() {
        this.checkIns = 0;
        return this;
    }

    public EventBuilder withCauses(List<String> causesList) {
        this.causes = causesList;
        return this;
    }

    public EventBuilder withAccessibilities(Map<String, Boolean> accessibilitiesList) {
        this.accessibilities = accessibilitiesList;
        return this;
    }

    public EventBuilder withEventID() {
        this.eventID = UUID.randomUUID().toString();
        return this;
    }

    public EventBuilder withGoFundMeLink(String link) {
        this.goFundMeLink = (link != null && link.length() >= 0) ? link : "";
        return this;
    }

    public Event build() {
        this.eventDTO = new Event();
        eventDTO.setName(this.name);
        eventDTO.setDateStart(this.dateStart);
        eventDTO.setDateEnd(this.dateEnd);
        eventDTO.setTimeStart(this.timeStart);
        eventDTO.setTimeEnd(this.timeEnd);
        eventDTO.setCity(this.city);
        eventDTO.setDescription(this.description);
        eventDTO.setLocation(this.location);
        eventDTO.setTimeZone(this.timeZone);
        eventDTO.setCheckIns(this.checkIns);
        eventDTO.setCauses(this.causes);
        eventDTO.setAccessibilities(this.accessibilities);
        eventDTO.setID(this.eventID);
        eventDTO.setGoFundMeLink(this.goFundMeLink);
        return eventDTO;
    }

    public Event getEventDTO() {
        return this.eventDTO;
    }

    @Override
    public String toString() {
        return "EventBuilder{" +
                "name='" + name + '\'' +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", timeStart='" + timeStart + '\'' +
                ", city='" + city + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", checkIns=" + checkIns +
                ", causes=" + causes +
                ", accessibilities=" + accessibilities +
                ", eventID='" + eventID + '\'' +
                ", eventDTO=" + eventDTO +
                '}';
    }
}