package ui;

import source.Genre;
import source.Book;
import source.InfoValidator;

import javax.swing.*;
import javax.xml.validation.Validator;
import java.awt.*;
import com.formdev.flatlaf.FlatLaf;
import javax.swing.*;
import java.awt.Window;
import java.util.prefs.Preferences;

public class Settings extends JDialog{

    private JComboBox<Theme> themeBox;
    private JButton approveBtn;
    private JButton cancelBtn;
    public Theme curTheme;

    public Settings(JFrame owner) {
        super(owner, "Настройки", true);
        initUI();
    }

    private static final Preferences prefs = Preferences.userNodeForPackage(Settings.class);
    private static final String THEME_KEY = "selected_theme";

    public static void saveTheme(Theme theme) {
        prefs.put(THEME_KEY, theme.name());
    }

    public static Theme loadTheme() {
        String themeName = prefs.get(THEME_KEY, Theme.LIGHT.name());
        try {
            return Theme.valueOf(themeName);
        } catch (Exception e) {
            return Theme.LIGHT;
        }
    }

    public void changeTheme(LookAndFeel newLaf) {
        try {
            UIManager.setLookAndFeel(newLaf);

            for (Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window);
            }

        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Эта тема не поддерживается на данной системе");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //changeTheme(Theme.MACDARK.getLaf());
    }

    public void initUI() {

        getContentPane().removeAll();
        setTitle("Настройки темы");
        setModal(true);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        themeBox = new JComboBox<>(Theme.values());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(themeBox, gbc);


        approveBtn = new JButton("Применить");
        approveBtn.addActionListener(e -> apply());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(approveBtn, gbc);


        cancelBtn = new JButton("Закрыть");
        cancelBtn.addActionListener(e -> dispose());
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(cancelBtn, gbc);

        this.add(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);

        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(getOwner());
    }



    private void apply() {
        Theme selected = (Theme) themeBox.getSelectedItem();

        if (selected != null) {
            changeTheme(selected.getLaf());
            Settings.saveTheme(selected);
        }
        dispose();
    }

    public Settings showDialog()
    {
        initUI();
        setVisible(true);
        return null;
    }

}