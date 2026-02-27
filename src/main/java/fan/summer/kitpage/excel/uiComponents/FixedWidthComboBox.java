package fan.summer.kitpage.excel.uiComponents;

import javax.swing.*;
import java.awt.*;

public class FixedWidthComboBox extends JComboBox {
    
    private final int fixedWidth;
    
    public FixedWidthComboBox(int fixedWidth) {
        this.fixedWidth = fixedWidth;
    }
    
    @Override
    public Dimension getPreferredSize() {
        // Use parent class for height calculation, fixed width
        return new Dimension(fixedWidth, super.getPreferredSize().height);
    }
    
    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }
    
    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
}