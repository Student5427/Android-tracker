package com.example.mytest;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Workout implements Parcelable {
    private String id;
    private Date startTime;
    private Date endTime;
    private float totalDistance; // в км
    private float averageSpeed; // в км/ч
    private float maxSpeed; // в км/ч
    private long duration; // в миллисекундах
    private List<LocationPoint> pathPoints;
    // Добавь новое поле для калорий
    private float calories;
    public float getCalories() { return calories; }
    public void setCalories(float calories) { this.calories = calories; }

    // НОВЫЕ ПОЛЯ
    private String type; // "run", "walk", "bicycle"
    private String title;
    private boolean isPlanned;

    public Workout() {
        this.id = String.valueOf(System.currentTimeMillis());
        this.startTime = new Date();
        this.pathPoints = new ArrayList<>();
        this.maxSpeed = 0;
        this.isPlanned = false;
        this.calories = 0;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }

    public float getTotalDistance() { return totalDistance; }
    public void setTotalDistance(float totalDistance) { this.totalDistance = totalDistance; }

    public float getAverageSpeed() { return averageSpeed; }
    public void setAverageSpeed(float averageSpeed) { this.averageSpeed = averageSpeed; }

    public float getMaxSpeed() { return maxSpeed; }
    public void setMaxSpeed(float maxSpeed) { this.maxSpeed = maxSpeed; }

    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }

    public List<LocationPoint> getPathPoints() { return pathPoints; }
    public void setPathPoints(List<LocationPoint> pathPoints) { this.pathPoints = pathPoints; }

    // НОВЫЕ GETTERS AND SETTERS
    public String getType() { return type; }
    public void setType(String type) {
        this.type = type;
        // Автоматически устанавливаем заголовок при установке типа
        if (type != null) {
            switch (type) {
                case "run":
                    this.title = "Бег";
                    break;
                case "walk":
                    this.title = "Ходьба";
                    break;
                case "bicycle":
                    this.title = "Велотренировка";
                    break;
                default:
                    this.title = "Тренировка";
            }
        }
    }

    public String getTitle() {
        if (title == null && type != null) {
            // Если заголовок не установлен, но тип есть - генерируем заголовок
            setType(type);
        }
        return title;
    }
    public void setTitle(String title) { this.title = title; }

    public boolean isPlanned() { return isPlanned; }
    public void setPlanned(boolean planned) { isPlanned = planned; }

    public void addLocationPoint(LocationPoint point) {
        this.pathPoints.add(point);
    }

    // Parcelable implementation
    protected Workout(Parcel in) {
        id = in.readString();
        totalDistance = in.readFloat();
        averageSpeed = in.readFloat();
        maxSpeed = in.readFloat();
        duration = in.readLong();
        startTime = new Date(in.readLong());
        endTime = new Date(in.readLong());
        pathPoints = in.createTypedArrayList(LocationPoint.CREATOR);
        type = in.readString();
        title = in.readString();
        isPlanned = in.readByte() != 0;
        calories = in.readFloat();
    }

    public static final Creator<Workout> CREATOR = new Creator<Workout>() {
        @Override
        public Workout createFromParcel(Parcel in) {
            return new Workout(in);
        }

        @Override
        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeFloat(totalDistance);
        dest.writeFloat(averageSpeed);
        dest.writeFloat(maxSpeed);
        dest.writeLong(duration);
        dest.writeLong(startTime.getTime());
        dest.writeLong(endTime != null ? endTime.getTime() : 0);
        dest.writeTypedList(pathPoints);
        dest.writeString(type);
        dest.writeString(title);
        dest.writeByte((byte) (isPlanned ? 1 : 0));
        dest.writeFloat(calories);
    }
}