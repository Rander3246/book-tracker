package source;

import ui.Settings;
import ui.Theme;
import ui.shelfFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {


        Theme savedTheme = Settings.loadTheme();
        try {
            UIManager.setLookAndFeel(savedTheme.getLaf());
        }
        catch (Exception ex) {
            System.err.println("Не удалось запустить FlatLaf");
        }

        SwingUtilities.invokeLater(() -> {
            new shelfFrame().setVisible(true);
        });
    }
}