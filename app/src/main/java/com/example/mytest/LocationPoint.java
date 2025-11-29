package com.example.mytest;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class LocationPoint implements Parcelable {
    private double latitude;
    private double longitude;
    private float speed; // в м/с
    private long timestamp;

    public LocationPoint() {}

    public LocationPoint(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.speed = location.getSpeed();
        this.timestamp = location.getTime();
    }

    // Getters and Setters
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    // Parcelable implementation
    protected LocationPoint(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        speed = in.readFloat();
        timestamp = in.readLong();
    }

    public static final Creator<LocationPoint> CREATOR = new Creator<LocationPoint>() {
        @Override
        public LocationPoint createFromParcel(Parcel in) {
            return new LocationPoint(in);
        }

        @Override
        public LocationPoint[] newArray(int size) {
            return new LocationPoint[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeFloat(speed);
        dest.writeLong(timestamp);
    }
}