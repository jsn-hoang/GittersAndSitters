package com.example.gittersandsittersdatabase;

import android.location.Location;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;

/**
 * This class represents a HabitEvent in the HabitTracker app.
 */
public class HabitEvent implements Serializable, Comparable<HabitEvent> {

    private String eventID;
    private String eventName;
    private String parentHabitName;     // Each event is associated with a particular Habit
    private Calendar eventDate;     // always today's date
    private Location eventLocation;
    private String eventComment;
    private File eventPhoto;

    // Constructor with the required attributes: name and date
    public HabitEvent(String eventID, String eventName, String parentHabitName, Calendar eventDate,
            /*Location eventLocation,*/ String eventComment /*File eventPhoto*/) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.parentHabitName = parentHabitName;
        this.eventDate = eventDate;
        //this.eventLocation = eventLocation;
        this.eventComment = eventComment;
        //this.eventPhoto = eventPhoto;
    }

    // Constructor with the required attributes: name and date
    public HabitEvent(String eventName, String parentHabitName, Calendar eventDate,
            /*Location eventLocation,*/ String eventComment /*File eventPhoto*/) {
        this.eventID = "temp";
        this.eventName = eventName;
        this.parentHabitName = parentHabitName;
        this.eventDate = eventDate;
        //this.eventLocation = eventLocation;
        this.eventComment = eventComment;
        //this.eventPhoto = eventPhoto;
    }

    // Getters and Setters

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

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


    /**
     * This method implements the HabitEvent sorting logic.
     * HabitEvents are to be sorted by the the eventDate attribute.
     */
    @Override
    public int compareTo(HabitEvent h) {
        return (this.getEventDate().compareTo(h.getEventDate()));
    }
}
