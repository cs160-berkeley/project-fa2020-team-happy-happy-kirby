package com.gmail.kingarthuralagao.us.civicengagement.data.model.location;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Northeast implements Serializable, Parcelable
{

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    public final static Creator<Northeast> CREATOR = new Creator<Northeast>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Northeast createFromParcel(Parcel in) {
            return new Northeast(in);
        }

        public Northeast[] newArray(int size) {
            return (new Northeast[size]);
        }

    }
            ;
    private final static long serialVersionUID = -877448700245030988L;

    protected Northeast(Parcel in) {
        this.lat = ((Double) in.readValue((Double.class.getClassLoader())));
        this.lng = ((Double) in.readValue((Double.class.getClassLoader())));
    }

    public Northeast() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(lat);
        dest.writeValue(lng);
    }

    public int describeContents() {
        return 0;
    }

}