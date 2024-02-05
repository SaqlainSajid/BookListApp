// WsCube Tech, 2022, Youtube, Recycler View in Android Studio Explained with Example | Android Recycler View Tutorial, https://www.youtube.com/watch?v=FEqF1_jDV-A


// All code in Book class, is taken and repurposed from the above mentioned Youtube video


package com.example.ssajid_mybookwishlist;

// the Book class is created for the purpose of representing a book, since this is a book list book
// is used through out the code base, it's used in MainActivity and in RecyclerBookAdapter
public class Book {
    private String title;
    private String author;
    private String genre;
    private Integer pubYear;
    private Boolean read;



    public Book(String title, String author, String genre, Integer pubYear, Boolean read) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.pubYear = pubYear;
        this.read = read;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getPubYear() {
        return pubYear;
    }

    public void setPubYear(Integer pubYear) {
        this.pubYear = pubYear;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

}
