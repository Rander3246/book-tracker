package source;

import java.util.UUID;

public class Book {

    private String id;
    private String title;
    private String author;
    private Genre genre;
    private int place;
    private boolean isAvailable;


    public Book(String title, String author,
                Genre genre, int place, boolean isAvailable) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.place = place;
        this.isAvailable = isAvailable;

    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public Genre getGenre() { return genre; }
    public Integer getPlace() { return place; }
    public boolean isAvailable() { return isAvailable; }

    public void  setId(String id)        { this.id = id; }
    public void  setTitle(String title)     { this.title = title; }
    public void  setAuthor(String author)    { this.author = author; }
    public void  setGenre(Genre genre)     { this.genre = genre; }
    public void  setPlace(int place)     { this.place = place; }
    public void  setAvailable(boolean isAvailable) { this.isAvailable = isAvailable; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book b = (Book) o;
        return id.equals(b.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Book{" + "title='" + title + '\'' + ", genre=" + genre + '}';
    }
}