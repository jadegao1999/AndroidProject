package com.example.yourproject;

public class User {

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String id;
    public String firstName;
    public String lastName;
    public String location;

    User() {
        // Default constructor required for calls to DataSnapshot.getValue(com.example.yourproject.User.class)
    }

    User(String id, String firstName, String lastName, String location) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
    }


}
