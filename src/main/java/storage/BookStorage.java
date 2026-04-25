package storage;

import source.Book;
import source.Genre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookStorage {

    private Connection conn;
    private List<Book> books = new ArrayList<>();

    String url = "jdbc:sqlite:booksDB.db";

    public BookStorage()
    {
        initTable();
        loadBooks();
    }

    public void initTable()
    {


        //Creating table
        String sql = "CREATE TABLE IF NOT EXISTS books (" +
                "id TEXT PRIMARY KEY, " +
                "title TEXT NOT NULL, " +
                "author TEXT, " +
                "genre TEXT, " +
                "place INTEGER, " +
                "isAvailable INTEGER" +
                ");";

        try
        {
            this.conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();

            stmt.execute(sql);
            System.out.println("Таблица готова к работе.");

        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }
    public void updateBook(Book b) {
        String sql = "UPDATE books SET title = ?," +
                "author = ?, genre = ?, place = ?," +
                " isAvailable = ? WHERE id = ?";


        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {


            preparedStatement.setString(1, b.getTitle());
            preparedStatement.setString(2, b.getAuthor());
            preparedStatement.setString(3, b.getGenre().name());
            preparedStatement.setInt(4, b.getPlace());
            preparedStatement.setBoolean(5, b.isAvailable());
            preparedStatement.setString(6, b.getId());

            preparedStatement.executeUpdate();

            if (this.books != null) {
                this.books.remove(b);
                this.books.add(b);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении книги: " + e.getMessage());
        }
    }

    public int getMaxPlace() {
        String sql = "SELECT MAX(place) FROM books";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addBook(Book b)
    {
        String sql = "INSERT INTO books(id, title," +
                "author, genre, place, isAvailable) " +
                "VALUES(?, ?, ?, ?, ?, ?)";


        try (Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, b.getId());
            preparedStatement.setString(2, b.getTitle());
            preparedStatement.setString(3, b.getAuthor());
            preparedStatement.setString(4, b.getGenre().name());
            preparedStatement.setInt(5, b.getPlace());
            preparedStatement.setBoolean(6, b.isAvailable());

            preparedStatement.executeUpdate();

            if (this.books != null) {
                this.books.add(b);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении книги: " + e.getMessage());
        }
    }

    public void rmBook(Book b)
    {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, b.getId());

            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0 && this.books != null) {
                this.books.remove(b);
                System.out.println("Книга удалена из базы и списка.");
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при удалении книги: " + e.getMessage());
        }
    }

    public List<Book> getBooks()
    {
        return books;
    }


    public List<Book> loadBooks() {
        List<Book> loadedBooks = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String genreStr = rs.getString("genre");
                Genre genre = (genreStr != null) ? Genre.valueOf(genreStr) : null;

                int place = rs.getInt("place");
                boolean isAvailable = rs.getInt("isAvailable") == 1;
                Book book = new Book(title, author, genre, place, isAvailable);
                book.setId(id);

                loadedBooks.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при загрузке книг: " + e.getMessage());
        }

        this.books = loadedBooks;
        return loadedBooks;
    }
}

