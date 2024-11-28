package Client;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.Color;

import static java.awt.Color.orange;

public class BorderGUI {
    public static final Border THIN_BORDER = BorderFactory.createCompoundBorder
            (BorderFactory.createMatteBorder(3, 3, 3, 3, Color.getHSBColor(150/ 360f, 50 / 100f,  100 /100f)),
                    BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.getHSBColor(150 / 360f, 55 / 100f, 65 /100f), 3),
                            BorderFactory.createDashedBorder(orange, 2, 2, 2, true)));
    public static final Border THICK_BORDER = BorderFactory.createCompoundBorder
            (BorderFactory.createMatteBorder(10, 10, 10, 10, Color.getHSBColor(150/ 360f, 50 / 100f,  100 /100f)),
                    BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.getHSBColor(150 / 360f, 55 / 100f, 65 /100f), 3),
                            BorderFactory.createDashedBorder(orange, 8, 2, 2, true)));
}
