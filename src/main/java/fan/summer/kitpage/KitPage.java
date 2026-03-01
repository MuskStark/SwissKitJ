package fan.summer.kitpage;

import fan.summer.annoattion.SwissKitPage;

import javax.swing.*;
import java.awt.*;

public interface KitPage {
    /**
     * Get page panel
     */
    JPanel getPanel();

    /**
     * Get menu display name.
     * Reads from @SwissKitPage annotation if available.
     */
    default String getMenuName() {
        SwissKitPage annotation = getClass().getAnnotation(SwissKitPage.class);
        if (annotation != null && !annotation.menuName().isEmpty()) {
            return annotation.menuName();
        }
        return getClass().getSimpleName();
    }

    /**
     * Get menu icon (can return null).
     */
    default Icon getMenuIcon() {
        return null;
    }

    /**
     * Get menu tooltip text.
     * Reads from @SwissKitPage annotation if available.
     */
    default String getMenuTooltip() {
        SwissKitPage annotation = getClass().getAnnotation(SwissKitPage.class);
        if (annotation != null) {
            return annotation.menuTooltip();
        }
        return null;
    }
}
