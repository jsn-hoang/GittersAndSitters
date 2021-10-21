package com.example.habittracker;

public class Habit {

    private String city;
    private String province;

    public Habit(String city, String province) {
        this.city = city;
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }
}