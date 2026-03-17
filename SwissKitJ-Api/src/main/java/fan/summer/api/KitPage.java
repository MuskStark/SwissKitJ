package fan.summer.api;

import fan.summer.annoattion.SwissKitPage;

import javax.swing.*;

/**
 * Interface for SwissKit plugin pages.
 * All plugin pages must implement this interface and be annotated with {@link SwissKitPage}.
 *
 * <h2>Implementation Steps:</h2>
 * <ol>
 *   <li>Create a class that implements this interface</li>
 *   <li>Annotate the class with {@code @SwissKitPage}</li>
 *   <li>Implement the {@link #getPanel()} method to return your JPanel</li>
 *   <li>Register the class in {@code META-INF/services/fan.summer.api.KitPage}</li>
 * </ol>
 *
 * <h2>Example:</h2>
 * <pre>
 * {@code @SwissKitPage(menuName = "My Tool", menuTooltip = "My tool description", order = 10)}
 * public class MyToolPage implements KitPage {
 *     private JPanel panel;
 *
 *     public MyToolPage() {
 *         panel = new JPanel();
 *         // initialize your UI
 *     }
 *
 *     {@code @Override}
 *     public JPanel getPanel() {
 *         return panel;
 *     }
 * }
 * </pre>
 */
public interface KitPage {

    /**
     * Returns the main panel for this page.
     * This panel will be displayed in the content area when the page is selected.
     *
     * @return the JPanel containing the page content
     */
    JPanel getPanel();

    /**
     * Returns the menu display name.
     * Default implementation reads from {@link SwissKitPage#menuName()} annotation.
     * Override this method to provide custom dynamic menu names.
     *
     * @return menu display name
     */
    default String getMenuName() {
        SwissKitPage annotation = getClass().getAnnotation(SwissKitPage.class);
        if (annotation != null && !annotation.menuName().isEmpty()) {
            return annotation.menuName();
        }
        return getClass().getSimpleName();
    }

    /**
     * Returns the menu icon.
     * Override this method to provide a custom icon for the menu item.
     *
     * @return menu icon, or null for no icon
     */
    default Icon getMenuIcon() {
        return null;
    }

    /**
     * Returns the menu tooltip text shown on hover.
     * Default implementation reads from {@link SwissKitPage#menuTooltip()} annotation.
     * Override this method to provide custom dynamic tooltips.
     *
     * @return tooltip text, or null for no tooltip
     */
    default String getMenuTooltip() {
        SwissKitPage annotation = getClass().getAnnotation(SwissKitPage.class);
        if (annotation != null) {
            return annotation.menuTooltip();
        }
        return null;
    }
}
