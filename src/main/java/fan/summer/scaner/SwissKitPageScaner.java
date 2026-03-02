package fan.summer.scaner;

import fan.summer.annoattion.SwissKitPage;
import fan.summer.api.KitPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 类的详细说明
 *
 * @author summer
 * @version 1.00
 * @Date 2026/3/2
 */
public class SwissKitPageScaner {
    private static final Logger logger = LoggerFactory.getLogger(SwissKitPageScaner.class);
    public static List<KitPage> scan() {
        List<KitPage> pages = new ArrayList<>();

        ServiceLoader<KitPage> loader = ServiceLoader.load(
                KitPage.class,
                Thread.currentThread().getContextClassLoader()
        );

        for (KitPage page : loader) {
            SwissKitPage annotation = page.getClass().getAnnotation(SwissKitPage.class);

            // 没有注解的跳过
            if (annotation == null) {
                logger.debug("Skipped (no annotation): {}", page.getClass().getName());
                continue;
            }

            // visible=false 的跳过
            if (!annotation.visible()) {
                logger.debug("Skipped (invisible): {}", page.getClass().getName());
                continue;
            }

            pages.add(page);
            logger.info("Registered KitPage: {} (order: {})",
                    annotation.menuName(), annotation.order());
        }

        // 按 order 排序
        pages.sort(Comparator.comparingInt(
                p -> p.getClass().getAnnotation(SwissKitPage.class).order()
        ));

        logger.info("Total KitPage(s) loaded: {}", pages.size());
        return pages;
    }
}
