package Client;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.Color;

import static java.awt.Color.*;

public class BorderGUI {
    public static final Border THIN_BORDER = BorderFactory.createCompoundBorder
            (BorderFactory.createMatteBorder(3, 3, 3, 3, ColorGUI.olive),
                    BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorGUI.saddlebrown, 3),
                            BorderFactory.createDashedBorder(ColorGUI.darkKhaki, 2, 2, 2, true)));
    public static final Border THICK_BORDER = BorderFactory.createCompoundBorder
            (BorderFactory.createMatteBorder(3, 3, 3, 3, ColorGUI.olive),
                    BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorGUI.saddlebrown, 3),
                            BorderFactory.createSoftBevelBorder(2, ColorGUI.darkKhaki, ColorGUI.saddlebrown, ColorGUI.golden, ColorGUI.sandyBrown)));
    public static final Border SIMPLE_BORDER = BorderFactory.createCompoundBorder
            (BorderFactory.createMatteBorder(3, 3, 3, 3, ColorGUI.olive),
                    BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorGUI.saddlebrown, 3),
                            BorderFactory.createSoftBevelBorder(3, ColorGUI.darkKhaki, ColorGUI.saddlebrown, ColorGUI.golden, ColorGUI.sandyBrown)));

}
