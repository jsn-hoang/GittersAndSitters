package com.example.habittracker;

import android.location.Location;

import java.io.File;
import java.util.Calendar;

/**
 * This class represents a HabitEvent in the HabitTracker app.
 */
public class HabitEvent {

    private String eventName;
    private Calendar eventDate;
    private Location eventLocation;
    private String eventComment;
    private File eventPhoto;

    // Constructor with the required attributes: name and date
    public HabitEvent(String eventName, Calendar eventDate) {
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

    public Calendar getEventDate() {
        return eventDate;
    }

    public void setEventDate(Calendar eventDate) {
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

    public File getEventPhoto() {
        return eventPhoto;
    }

    public void setEventPhoto(File eventPhoto) {
        this.eventPhoto = eventPhoto;
    }
}
