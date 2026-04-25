package source;

import java.time.LocalDate;

public class InfoValidator {

    public static boolean isValidTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }
        return title.trim().length() >= 2;
    }

    public static boolean isValidAuthor(String author) {
        return author != null;
    }



    public static boolean isValidPlace(Integer place) {
        return place != null;
    }

    public static boolean isValidGenre(Genre genre) {
        return genre != null;
    }

    public static String validateTask(Book book) {
        if (book == null) {
            return "Книга не может быть пустой!";
        }

        if (!isValidTitle(book.getTitle())) {
            return "Название должно быть минимум из 2 символов";
        }

        if (!isValidAuthor(book.getAuthor())) {
            return "Имя автора некорректно";
        }

        if (!isValidGenre(book.getGenre())) {
            return "Жанр не должен быть пустым";
        }

        return "";
    }
}