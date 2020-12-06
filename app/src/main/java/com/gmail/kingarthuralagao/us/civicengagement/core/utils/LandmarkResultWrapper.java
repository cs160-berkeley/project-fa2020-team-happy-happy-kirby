package com.gmail.kingarthuralagao.us.civicengagement.core.utils;

import java.io.Serializable;

public class LandmarkResultWrapper implements Serializable {

    private Exception e;
    private double lat;
    private double lon;
    private String landmarkName;

    public LandmarkResultWrapper(Exception e) {
        this.e = e;
    }

    public LandmarkResultWrapper(double lat, double lng, String name) {
        this.lat = lat;
        this.lon = lng;
        this.landmarkName = name;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getLandmarkName() {
        return landmarkName;
    }

    public void setLandmarkName(String landmarkName) {
        this.landmarkName = landmarkName;
    }
}
