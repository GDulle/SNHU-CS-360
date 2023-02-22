package com.cs360.gunnardullecs360project;

//Class to hold structure for Event object
public class Event {
    int id;
    String user_name;
    String date;
    String time;
    String description;

    public Event() {
        super();
    }

    //Constructor
    public Event(int id, String userName, String date, String time, String description) {
        super();
        this.id = id;
        this.user_name = userName;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    //Constructor (without id)
    public Event (String userName, String date, String time, String description) {
        this.user_name = userName;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    //Constructor (without id and username)
    public Event(String date, String time, String description) {
        this.date = date;
        this.time = time;
        this.description = description;
    }

    //Setters and Getters
    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return user_name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }
}
