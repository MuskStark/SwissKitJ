package fan.summer.kitpage;

import fan.summer.annoattion.SwissKitPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

/**
 * Welcome page with quick access to documentation
 *
 * @author phoebej
 * @version 1.00
 * @Date 2026/3/1
 */
@SwissKitPage(order = 0)
public class WelcomePage implements KitPage {
    private static final Logger logger = LoggerFactory.getLogger(WelcomePage.class);
    
    private JPanel welcome;
    private JLabel doc;

    public WelcomePage() {
        doc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://muskstark.github.io/SwissKitJ"));
                } catch (Exception ex) {
                    logger.error("Failed to open documentation URL", ex);
                }
            }
        });
    }

    @Override
    public JPanel getPanel() {
        return welcome;
    }

    @Override
    public String getMenuName() {
        return "🏠 Welcome";
    }
}
