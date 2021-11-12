package com.example.gittersandsittersdatabase;

import android.location.Location;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;

/**
 * This class represents a HabitEvent in the HabitTracker app.
 */
public class HabitEvent implements Serializable {

    private String eventName;
    private String parentHabitName;     // Each event is associated with a particular Habit
    private Calendar eventDate;     // always today's date
    private Location eventLocation;
    private String eventComment;
    private File eventPhoto;

    // Constructor with the required attributes: name and date
    public HabitEvent(String eventName, String parentHabitName, Calendar eventDate,
                      /*Location eventLocation,*/ String eventComment /*File eventPhoto*/) {
        this.eventName = eventName;
        this.parentHabitName = parentHabitName;
        this.eventDate = eventDate;
        //this.eventLocation = eventLocation;
        this.eventComment = eventComment;
        //this.eventPhoto = eventPhoto;
    }

    // Getters and Setters

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getParentHabitName() {
        return parentHabitName;
    }

    public void setParentHabit(String parentHabitName) {
        this.parentHabitName = parentHabitName;
    }

    public Calendar getEventDate() {
        return eventDate;
    }

    public void setEventDate(Calendar eventDate) {
        // Set EventDate to today's date
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
