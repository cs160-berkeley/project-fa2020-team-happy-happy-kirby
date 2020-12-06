package com.gmail.kingarthuralagao.us.civicengagement.data.model.location;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GeolocationResult implements Serializable, Parcelable
{
    @SerializedName("address_components")
    @Expose
    private List<AddressComponent> addressComponents = null;
    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("types")
    @Expose
    private List<String> types = null;
    public final static Creator<GeolocationResult> CREATOR = new Creator<GeolocationResult>() {
        @SuppressWarnings({
                "unchecked"
        })
        public GeolocationResult createFromParcel(Parcel in) {
            return new GeolocationResult(in);
        }

        public GeolocationResult[] newArray(int size) {
            return (new GeolocationResult[size]);
        }
    };
    private final static long serialVersionUID = -2575614696178070255L;

    protected GeolocationResult(Parcel in) {
        in.readList(this.addressComponents, (AddressComponent.class.getClassLoader()));
        this.formattedAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.geometry = ((Geometry) in.readValue((Geometry.class.getClassLoader())));
        this.placeId = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.types, (String.class.getClassLoader()));
    }

    public GeolocationResult() {
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public List<AddressComponent> getAddressComponents() {
        return addressComponents;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getPlaceId() {
        return placeId;
    }

    public List<String> getTypes() {
        return types;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(addressComponents);
        dest.writeValue(formattedAddress);
        dest.writeValue(geometry);
        dest.writeValue(placeId);
        dest.writeList(types);
    }

    public int describeContents() {
        return 0;
    }

}