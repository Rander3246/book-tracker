package source;

import ui.shelfFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        try {
            // Устанавливаем современную светлую тему
            UIManager.setLookAndFeel(new com.formdev.flatlaf.themes.FlatMacLightLaf());
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);

        } catch (Exception ex) {
            System.err.println("Не удалось запустить FlatLaf");
        }

        SwingUtilities.invokeLater(() -> {
            new shelfFrame().setVisible(true);
        });
    }
}