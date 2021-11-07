package com.example.gittersandsittersdatabase;

import android.location.Location;

import java.io.File;
import java.util.Calendar;

/**
 * This class represents a HabitEvent in the HabitTracker app.
 */
public class HabitEvent {

    private String eventName;
    private String parentHabit;     // Each event is associated with a particular Habit
    private Calendar eventDate;     // always today's date
    private Location eventLocation;
    private String eventComment;
    private File eventPhoto;

    // Constructor with the required attributes: name and date
    public HabitEvent(String eventName, String parentHabit, Calendar eventDate,
                      Location eventLocation, String eventComment, File eventPhoto) {
        this.eventName = eventName;
        this.parentHabit = parentHabit;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventComment = eventComment;
        this.eventPhoto = eventPhoto;
    }

    // Getters and Setters

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getParentHabit() {
        return eventName;
    }

    public void setParentHabit(String eventName) {
        this.eventName = eventName;
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
