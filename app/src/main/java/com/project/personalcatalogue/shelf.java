package com.project.personalcatalogue;

public class shelf {
    // Variable to store data corresponding
    // to shelf name keyword in database
    private String name;

    // Mandatory empty constructor
    // for use of FirebaseUI
    public shelf() {}

    // Getter and setter methods
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
}
