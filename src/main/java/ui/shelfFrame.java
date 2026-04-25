package ui;

import source.Book;
import storage.BookStorage;

import javax.swing.*;
import java.awt.*;

import javax.swing.table.DefaultTableModel;

public class shelfFrame extends JFrame {

    private BookStorage storage;
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> statusFilter;

    public shelfFrame() {
        super("Book Shelf");

        storage = new BookStorage();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        initUI();
        loadBooks();
    }



    private void initUI() {
        // Главная панель
        JPanel mainPanel = new JPanel(new BorderLayout());
        this.add(mainPanel);

        // Панель инструментов (поиск и фильтры)
        JPanel toolbarPanel = createToolbarPanel();
        mainPanel.add(toolbarPanel, BorderLayout.NORTH);

        // Таблица задач
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Панель кнопок
        JPanel buttonsPanel = createButtonsPanel();
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }


    private JPanel createToolbarPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(new Color(240, 240, 240));

        // Поиск
        panel.add(new JLabel("Поиск:"));
        searchField = new JTextField(15);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { loadBooks(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { loadBooks(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { loadBooks(); }
        });
        panel.add(searchField);

        // Фильтр по статусу
        panel.add(new JLabel("Статус:"));
        String[] statuses = {"Все", "В архиве", "Выдана"};
        statusFilter = new JComboBox<>(statuses);
        panel.add(statusFilter);

        // Кнопка применить фильтры
        JButton applyFilterBtn = new JButton("Применить");
        applyFilterBtn.addActionListener(e -> applyFilters());
        panel.add(applyFilterBtn);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());



        // Модель таблицы
        tableModel = new DefaultTableModel(
                new String[]{"✓", "Название", "Автор", "Жанр", "Ячейка", "Статус"},
                0
        ) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return Boolean.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Только чекбокс редактируется
            }
        };



        booksTable = new JTable(tableModel);
        booksTable.setRowHeight(25);
        booksTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        booksTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        booksTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        booksTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        booksTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        booksTable.getColumnModel().getColumn(5).setPreferredWidth(80);

        // Слушатель для чекбокса
        booksTable.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 0) { // Если кликнули по чекбоксу
                int row = e.getFirstRow();
                boolean isAvailable = (boolean) tableModel.getValueAt(row, 0);

                // Берем название из ПЕРВОЙ колонки (индекс 1)
                String title = (String) tableModel.getValueAt(row, 1);

                Book book = getBookFromRow(row);
                if (book != null) {
                    book.setAvailable(isAvailable);
                    storage.updateBook(book); // Отправляем в SQL
                } else {
                    System.out.println("Ошибка: Книга не найдена в списке по названию!");
                }

                if (book != null) {
                    book.setAvailable(isAvailable);
                    storage.updateBook(book);

                    // Обновляем текст статуса в последней колонке (индекс 5)
                    tableModel.setValueAt(isAvailable ? "В архиве" : "Выданные", row, 5);
                }

            }

        });

        JScrollPane scrollPane = new JScrollPane(booksTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        booksTable.putClientProperty("JTable.alternateRowColor", true);
        UIManager.put("Table.alternateRowColor", new Color(245, 245, 245));

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton addBtn = new JButton(" Добавить");
        addBtn.addActionListener(e -> openAddBookDialog());
        panel.add(addBtn);

        JButton editBtn = new JButton(" Редактировать");
        editBtn.addActionListener(e -> openEditBookDialog());
        panel.add(editBtn);

        JButton deleteBtn = new JButton(" Удалить");
        deleteBtn.addActionListener(e -> deleteBook());
        panel.add(deleteBtn);

        JButton clearBtn = new JButton(" Очистить фильтры");
        clearBtn.addActionListener(e -> {
            searchField.setText("");
            statusFilter.setSelectedIndex(0);
            loadBooks();
        });
        panel.add(clearBtn);

        return panel;
    }

    private void openAddBookDialog() {
        int nextPlace = storage.getMaxPlace() + 1;

        BookDialog dialog = new BookDialog(this, null, nextPlace);

        Book newBook = dialog.showDialog();
        if (newBook != null) {
            storage.addBook(newBook);
            loadBooks();
        }
    }

    private void openEditBookDialog() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите книгу для редактирования",
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Book selectedBook = getBookFromRow(selectedRow);
        if (selectedBook != null) {
            BookDialog dialog = new BookDialog(this, selectedBook, 0);
            Book editedBook = dialog.showDialog();
            if (editedBook != null) {
                storage.updateBook(editedBook);
                loadBooks();
            }
        }
    }

    private void deleteBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите книгу для удаления",
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Вы действительно хотите удалить книгу из базы?",
                "Подтверждение удаления книги", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Book book = getBookFromRow(selectedRow);
            if (book != null) {
                storage.rmBook(book);
                loadBooks();
            }
        }
    }

    private void applyFilters() {
        loadBooks();
    }

    private void loadBooks() {
        tableModel.setRowCount(0);

        String searchText = searchField.getText().toLowerCase();
        //Genre genre = (Genre) genreFilter.getSelectedItem();
        String selectedStatus = (String) statusFilter.getSelectedItem();

        for (Book book : storage.getBooks()) {
            String title = book.getTitle().toLowerCase();
            String author = book.getAuthor().toLowerCase();

            if (!searchText.isEmpty() && !title.contains(searchText) && !author.contains(searchText)) {
                continue;
            }

            // Фильтр по статусу
            if (selectedStatus.equals("В архиве") && !book.isAvailable()) {
                continue;
            }
            if (selectedStatus.equals("Выдана") && book.isAvailable()) {
                continue;
            }

            tableModel.addRow(new Object[]{
                    book.isAvailable(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getGenre().getDisplayName(),
                    book.getPlace(),
                    book.isAvailable() ? "В архиве" : "Выдана"
            });
        }
    }

    private Book getBookFromRow(int row) {
        if (row < 0 || row >= tableModel.getRowCount()) {
            return null;
        }
        String bookTitle = (String) tableModel.getValueAt(row, 1);
        for (Book book : storage.getBooks()) {
            if (book.getTitle().equals(bookTitle)) {
                return book;
            }
        }
        return null;
    }
}
