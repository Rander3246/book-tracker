package ui;

import source.Genre;
import source.Book;
import source.InfoValidator;

import javax.swing.*;
import javax.xml.validation.Validator;
import java.awt.*;

public class BookDialog extends JDialog{

    private JTextField titleField;
    private JTextArea AuthorArea;
    private JComboBox<Genre> genreCombo;
    private JSpinner placeSpinner;
    private JButton okBtn;
    private JButton cancelBtn;
    private boolean succeeded = false;


    private Book book;
    private boolean isAvailable = false;

    public BookDialog(JFrame parent, Book book, int suggestedPlace) {
        super(parent, "Добавить/Редактировать книгу", true);

        this.book = book;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(parent);

        initUI(suggestedPlace);

        if (this.book != null) {
            loadBookData();
        }
    }

    private void initUI(int suggestedPlace) {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Название
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Название:"), gbc);

        gbc.gridx = 1;
        titleField = new JTextField(25);
        mainPanel.add(titleField, gbc);

        // Автор
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Автор:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        AuthorArea = new JTextArea(1, 25);
        AuthorArea.setLineWrap(true);
        AuthorArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(AuthorArea);
        mainPanel.add(scrollPane, gbc);


        // жанр
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Жанр:"), gbc);

        gbc.gridx = 1;
        genreCombo = new JComboBox<>(Genre.values());
        mainPanel.add(genreCombo, gbc);


        // Место (int)
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Ячейка:"), gbc);

        gbc.gridx = 1;
        // SpinnerNumberModel(initialValue, min, max, step)
        int startValue = (book != null) ? book.getPlace() : suggestedPlace;
        SpinnerNumberModel placeModel = new SpinnerNumberModel(startValue, 1, 1000, 1);
        placeSpinner = new JSpinner(placeModel);

        // Optional: format as integer (prevents showing ".0")
        JSpinner.NumberEditor numberEditor = new JSpinner.NumberEditor(placeSpinner, "#");
        placeSpinner.setEditor(numberEditor);

        mainPanel.add(placeSpinner, gbc);

        // Кнопки
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        okBtn = new JButton("OK");
        okBtn.addActionListener(e -> confirmDialog());
        buttonsPanel.add(okBtn);

        cancelBtn = new JButton("Отмена");
        cancelBtn.addActionListener(e -> cancelDialog());
        buttonsPanel.add(cancelBtn);

        add(mainPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void loadBookData() {
        if (book != null) {
            titleField.setText(book.getTitle());
            AuthorArea.setText(book.getAuthor());
            genreCombo.setSelectedItem(book.getGenre());
            placeSpinner.setValue(book.getPlace());
        }
    }

    private void confirmDialog() {
        String title = titleField.getText();
        String author = AuthorArea.getText();
        Integer place = (Integer) placeSpinner.getValue();
        Genre genre = (Genre) genreCombo.getSelectedItem();

        // Валидация
        if (!InfoValidator.isValidTitle(title)) {
            showError("Название должно содержать минимум 2 символа");
            return;
        }

        if (!InfoValidator.isValidAuthor(author)) {
            showError("Выберите категорию");
            return;
        }

        if (!InfoValidator.isValidPlace(place))
        {
            showError("Выберите ячейку");
            return;
        }

        if (!InfoValidator.isValidGenre(genre)) {
            showError("Выберите жанр");
            return;
        }



        // Создаём или обновляем задачу
        if (book == null) {
            book = new Book(title, author, genre, place, true);
        } else {
            book.setTitle(title);
            book.setAuthor(author);
            book.setGenre(genre);
            book.setPlace(place);
        }

        succeeded = true;
        dispose();
    }

    private void cancelDialog() {
        isAvailable = false;
        book = null;
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка валидации",
                JOptionPane.ERROR_MESSAGE);
    }

    public Book showDialog() {
        setVisible(true);
        return succeeded ? book : null;
    }
}
