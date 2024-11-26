package Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {
    private Image backgroundImage;

    public ImagePanel(String filepath) {
        try {
            File imageFile = new File(filepath);
            System.out.println("Loading image from: " + imageFile.getAbsolutePath());
            backgroundImage = ImageIO.read(imageFile);

            if (backgroundImage == null) {
                System.err.println("Image not found: " + filepath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
