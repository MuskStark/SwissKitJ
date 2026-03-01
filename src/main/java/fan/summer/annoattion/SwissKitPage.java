package fan.summer.annoattion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a class as a KitPage implementation.
 * Used for automatic page registration and menu configuration.
 *
 * @author phoebej
 * @version 1.00
 * @Date 2026/3/1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SwissKitPage {

    /**
     * Menu display name.
     * If empty, the class simple name will be used.
     *
     * @return menu display name
     */
    String menuName() default "";

    /**
     * Menu tooltip text shown on hover.
     *
     * @return tooltip text
     */
    String menuTooltip() default "";

    /**
     * Whether this page is visible in the menu.
     *
     * @return true if visible, false otherwise
     */
    boolean visible() default true;

    /**
     * Menu item order for sorting.
     * Lower values appear first.
     *
     * @return order value
     */
    int order() default 0;
}
