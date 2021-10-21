package com.example.habittracker;

import android.location.Location;

import java.util.Date;

public class HabitEvent {

    private String eventName;
    private Date eventDate;
    private Location eventLocation;
    private String eventComment;
    private Picture eventPhoto;

    // Constructor with the required attributes: name and date
    public HabitEvent(String eventName, Date eventDate) {
        this.eventName = eventName;
        this.eventDate = eventDate;
    }

    // Getters and Setters

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Location getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(Location eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventComment() {
        return eventComment;
    }

    public void setEventComment(String eventComment) {
        this.eventComment = eventComment;
    }

    public Picture getEventPhoto() {
        return eventPhoto;
    }

    public void setEventPhoto(Picture eventPhoto) {
        this.eventPhoto = eventPhoto;
    }
}
