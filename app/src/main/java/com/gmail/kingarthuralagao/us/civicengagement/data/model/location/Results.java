package com.gmail.kingarthuralagao.us.civicengagement.data.model.location;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Results implements Serializable, Parcelable
{

    @SerializedName("results")
    @Expose
    private List<GeolocationResult> geolocationResults = null;
    public final static Creator<Results> CREATOR = new Creator<Results>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Results createFromParcel(Parcel in) {
            return new Results(in);
        }

        public Results[] newArray(int size) {
            return (new Results[size]);
        }

    }
            ;
    private final static long serialVersionUID = 5501885994971779204L;

    protected Results(Parcel in) {
        in.readList(this.geolocationResults, (GeolocationResult.class.getClassLoader()));
    }

    public Results() {
    }

    public List<GeolocationResult> getGeolocationResults() {
        return geolocationResults;
    }

    public void setGeolocationResults(List<GeolocationResult> geolocationResults) {
        this.geolocationResults = geolocationResults;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(geolocationResults);
    }

    public int describeContents() {
        return 0;
    }

}