package com.cs360.gunnardullecs360project;

//Class for User object in User database table

public class User {

    int id;
    String user_name;
    String pass_word;
    String phone_number;

    public User() {
        super();
    }

    //Constructor
    public User(int i, String name, String password, String phone) {
        super();
        this.id = i;
        this.user_name = name;
        this.pass_word = password;
        this.phone_number = phone;
    }

    //Constructor without id
    public User (String name, String password, String phone) {
        this.user_name = name;
        this.pass_word = password;
        this.phone_number = phone;
    }

    //Setters and Getters
    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String name) {
        this.user_name = name;
    }

    public void setPassword(String password) {
        this.pass_word = password;
    }

    public void setPhoneNumber(String phone) {
        this.phone_number = phone;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return user_name;
    }

    public String getPassword() {
        return pass_word;
    }

    public String getPhoneNumber() {
        return phone_number;
    }
}
