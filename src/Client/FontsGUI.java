package Client;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontsGUI {
    public static Font MarioKart;
    public static Font MaruMonica;
    public static Font Pixel;
    public static Font NMFDisplay;

    static {
        try {
            MarioKart = Font.createFont(Font.TRUETYPE_FONT, new File("Resource/Fonts/MarioKart.ttf")).deriveFont(24f);
            NMFDisplay = Font.createFont(Font.TRUETYPE_FONT, new File("Resource/Fonts/NMFDisplay.ttf")).deriveFont(24f);
            MaruMonica = Font.createFont(Font.TRUETYPE_FONT, new File("Resource/Fonts/MaruMonica.ttf")).deriveFont(24f);
            Pixel = Font.createFont(Font.TRUETYPE_FONT, new File("Resource/Fonts/Pixel.ttf")).deriveFont(24f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}
