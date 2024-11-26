package Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImagePanel extends JPanel {
    private BufferedImage backgroundImage;


    public ImagePanel(String filepath) {
        try {
            File imageFile = new File(filepath);
            System.out.println("Loading image from: " + imageFile.getAbsolutePath());
            System.out.println("exxits: " + imageFile.exists());

            backgroundImage = ImageIO.read(imageFile);


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
