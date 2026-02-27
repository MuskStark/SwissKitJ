package fan.summer.kitpage;

import javax.swing.*;
import java.awt.*;

public interface KitPage {
    /**
     * Get page panel
     */
        JPanel getPanel();
    
        /**
         * Get page title
     */
        String getTitle();
    
        /**
         * Get menu display name
     */
        default String getMenuName() {
            return getTitle();
        }
    
        /**
         * Get menu icon (can return null)
     */
        default Icon getMenuIcon() {
            return null;
        }
    
        /**
         * Get menu tooltip text
     */
        default String getMenuTooltip() {
            return null;
        }}
