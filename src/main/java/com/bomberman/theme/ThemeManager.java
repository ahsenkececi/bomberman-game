package com.bomberman.theme;

import com.bomberman.models.Theme;
import java.awt.Color;

public class ThemeManager {

    private static ThemeManager instance;
    private Theme currentTheme;

    private ThemeManager() {
        currentTheme = Theme.FOREST; // Varsayılan
    }

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public void setTheme(Theme theme) {
        this.currentTheme = theme;
        System.out.println("🎨 Theme changed to: " + theme);
    }

    public Theme getCurrentTheme() {
        return currentTheme;
    }

    // Zemin renkleri (çim/kum/asfalt)
    public Color getGroundColor1() {
        switch (currentTheme) {
            case DESERT:
                return new Color(237, 201, 175); // Açık kum
            case FOREST:
                return new Color(34, 139, 34); // Koyu yeşil
            case CITY:
                return new Color(80, 80, 80); // Koyu gri asfalt
            default:
                return Color.GREEN;
        }
    }

    public Color getGroundColor2() {
        switch (currentTheme) {
            case DESERT:
                return new Color(210, 180, 140); // Koyu kum
            case FOREST:
                return new Color(50, 155, 50); // Açık yeşil
            case CITY:
                return new Color(100, 100, 100); // Açık gri
            default:
                return Color.DARK_GRAY;
        }
    }

    // Unbreakable duvar rengi
    public Color getUnbreakableWallColor() {
        switch (currentTheme) {
            case DESERT:
                return new Color(139, 90, 43); // Kahverengi taş
            case FOREST:
                return new Color(40, 80, 40); // Koyu yeşil ağaç
            case CITY:
                return new Color(60, 60, 60); // Gri beton
            default:
                return Color.DARK_GRAY;
        }
    }

    // Breakable duvar rengi
    public Color getBreakableWallColor() {
        switch (currentTheme) {
            case DESERT:
                return new Color(218, 165, 32); // Altın sarısı (kum blok)
            case FOREST:
                return new Color(101, 67, 33); // Kahverengi (tahta)
            case CITY:
                return new Color(139, 90, 43); // Turuncu tuğla
            default:
                return new Color(139, 90, 43);
        }
    }

    // Hard duvar rengi
    public Color getHardWallColor() {
        switch (currentTheme) {
            case DESERT:
                return new Color(160, 82, 45); // Sienna (kırmızı taş)
            case FOREST:
                return new Color(85, 107, 47); // Koyu zeytin yeşili (sağlam ağaç)
            case CITY:
                return new Color(105, 105, 105); // Gümüş gri (metal)
            default:
                return Color.GRAY;
        }
    }

    // Tema adı
    public String getThemeName() {
        switch (currentTheme) {
            case DESERT:
                return "🏜️ Desert Theme";
            case FOREST:
                return "🌲 Forest Theme";
            case CITY:
                return "🏙️ City Theme";
            default:
                return "Theme";
        }
    }
}