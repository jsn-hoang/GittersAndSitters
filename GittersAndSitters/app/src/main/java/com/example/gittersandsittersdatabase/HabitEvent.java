package com.example.gittersandsittersdatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class represents a HabitEvent in the HabitTracker app.
 */
public class HabitEvent implements Serializable, Comparable<HabitEvent> {

    private String eventID;
    private String parentHabitID;
    private String eventName;
    private Calendar eventDate;     // always today's date
    private String eventComment;
    private ArrayList<Double> eventLocation;
    private byte[] eventPhoto;

    // Constructor with the required attributes: name and date
    public HabitEvent(String eventID, String parentHabitID, String eventName, Calendar eventDate,
             String eventComment) {
        this.eventID = eventID;
        this.parentHabitID = parentHabitID;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventComment = eventComment;
    }

    // Constructor with the required attributes: name and date
    public HabitEvent(String parentHabitID, String eventName, Calendar eventDate,
             String eventComment) {
        this.eventID = "temp";
        this.parentHabitID = parentHabitID;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventComment = eventComment;
    }

    // Getters and Setters

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getParentHabitID() {
        return parentHabitID;
    }

    public void setParentHabitID(String parentHabitID) {
        this.parentHabitID = parentHabitID;
    }

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
        // Set EventDate to today's date
        this.eventDate = eventDate;
    }

    public ArrayList<Double> getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(ArrayList<Double> eventLocation) {
        if (eventLocation.size() != 2) {
            throw new IllegalArgumentException("Arraylist must be of size 2 (lat and long)");
        }
        this.eventLocation = eventLocation;
    }

    public String getEventComment() {
        return eventComment;
    }

    public void setEventComment(String eventComment) {
        this.eventComment = eventComment;
    }

    public byte[] getEventPhoto() {
        return eventPhoto;
    }

    public void setEventPhoto(byte[] eventPhoto) {
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
