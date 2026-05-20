package nye.bence.ui.gui;

import javax.swing.*;

/**
 * Abstract base class for GUI screens.
 */
public abstract class GuiScreen extends JPanel {

    protected final ScreenManager screenManager;

    public GuiScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    /**
     * Called when the screen is shown.
     */
    public void onShow() {
        // Override in subclasses if needed
    }

    /**
     * Called when the screen is hidden.
     */
    public void onHide() {
        // Override in subclasses if needed
    }
}
