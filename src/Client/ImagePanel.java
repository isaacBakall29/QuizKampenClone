package Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImagePanel extends JPanel {
    private BufferedImage backgroundImage;


    public ImagePanel(String filepath) {
        try {
            backgroundImage = ImageIO.read(new File(filepath));

            if (backgroundImage == null) {
                System.err.println("Image not found: " + filepath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
