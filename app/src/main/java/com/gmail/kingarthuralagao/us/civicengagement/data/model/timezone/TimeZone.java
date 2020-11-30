package com.gmail.kingarthuralagao.us.civicengagement.data.model.timezone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeZone
{
    @SerializedName("dstOffset")
    @Expose(serialize = false)
    private Integer dstOffset;
    @SerializedName("rawOffset")
    @Expose(serialize = false)
    private Integer rawOffset;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("timeZoneId")
    @Expose
    private String timeZoneId;
    @SerializedName("timeZoneName")
    @Expose
    private String timeZoneName;

    public Integer getDstOffset() {
        return dstOffset;
    }

    public void setDstOffset(Integer dstOffset) {
        this.dstOffset = dstOffset;
    }

    public Integer getRawOffset() {
        return rawOffset;
    }

    public void setRawOffset(Integer rawOffset) {
        this.rawOffset = rawOffset;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public String getTimeZoneName() {
        return timeZoneName;
    }

    public void setTimeZoneName(String timeZoneName) {
        this.timeZoneName = timeZoneName;
    }

}
