package nye.bence.ui.gui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages navigation between GUI screens.
 */
public class ScreenManager {

    private final JPanel mainPanel;
    private final CardLayout cardLayout;
    private final Map<String, GuiScreen> screens;
    private String currentScreen;

    public ScreenManager(JPanel mainPanel, CardLayout cardLayout) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        this.screens = new HashMap<>();
    }

    public void showScreen(String screenName) {
        GuiScreen nextScreen = screens.get(screenName);
        if (nextScreen == null) {
            cardLayout.show(mainPanel, screenName);
            currentScreen = screenName;
            return;
        }

        if (currentScreen != null) {
            GuiScreen activeScreen = screens.get(currentScreen);
            if (activeScreen != null) {
                activeScreen.onHide();
            }
        }

        cardLayout.show(mainPanel, screenName);

        nextScreen.onShow();
        currentScreen = screenName;
    }

    public void addScreen(String screenName, GuiScreen screen) {
        mainPanel.add(screen, screenName);
        screens.put(screenName, screen);
    }
}
