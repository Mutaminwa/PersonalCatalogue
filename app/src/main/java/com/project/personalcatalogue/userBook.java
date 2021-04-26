package com.project.personalcatalogue;

public class userBook {
    // Variable to store data corresponding
    // to shelf name keyword in database
    private String title;
    private String authors;
    private String ISBN;
    private String ISBN13;
    private String description;
    private String notes;

    // Mandatory empty constructor
    // for use of FirebaseUI
    public userBook() {}

    // Getter and setter methods
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthors()
    {
        return authors;
    }
    public void setAuthors(String authors)
    {
        this.authors = authors;
    }

    public String getISBN()
    {
        return ISBN;
    }
    public void setISBN(String ISBN)
    {
        this.ISBN = ISBN;
    }

    public String getISBN13()
    {
        return ISBN13;
    }
    public void setISBN13(String ISBN13)
    {
        this.ISBN13 = ISBN13;
    }

    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getNotes()
    {
        return notes;
    }
    public void setNotes(String notes)
    {
        this.notes = notes;
    }
}
