package com.bomberman;

import com.bomberman.views.MainWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.*;
import java.util.Enumeration;
import javax.swing.plaf.FontUIResource;

public class Main {
    public static void main(String[] args) {
        // ✅ YENİ: TÜM UI component'lerine emoji font uygula
        try {
            Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 14);
            setUIFont(new FontUIResource(emojiFont));
            System.out.println("✅ Emoji font applied to all UI components");
        } catch (Exception e) {
            System.out.println("⚠️ Could not set emoji font");
        }

        // Swing GUI'yi başlat
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }

    // ✅ YENİ: Tüm UI'ya font uygula
    public static void setUIFont(FontUIResource font) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
}