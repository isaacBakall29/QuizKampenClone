package Client;

import java.awt.image.BufferedImage;

public class ImagePanelTest {

        public static void main(String[] args) {
            testImageLoading();
            testImageLoadingWithInvalidPath();
        }

        public static void testImageLoading() {
            String validFilePath = "src/background.png";
            ImagePanel panel = new ImagePanel(validFilePath);

            BufferedImage backgroundImage = null;
            try {
                java.lang.reflect.Field field = ImagePanel.class.getDeclaredField("backgroundImage");
                field.setAccessible(true);
                backgroundImage = (BufferedImage) field.get(panel);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                System.out.println("Reflection failed to access backgroundImage field");
            }

            if (backgroundImage != null) {
                System.out.println("testImageLoading: PASSED");
            } else {
                System.out.println("testImageLoading: FAILED");
            }
        }

        public static void testImageLoadingWithInvalidPath() {
            String invalidFilePath = "invalid/path/to/image.png";
            ImagePanel panel = new ImagePanel(invalidFilePath);

            BufferedImage backgroundImage = null;
            try {
                java.lang.reflect.Field field = ImagePanel.class.getDeclaredField("backgroundImage");
                field.setAccessible(true);
                backgroundImage = (BufferedImage) field.get(panel);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                System.out.println("Reflection failed to access backgroundImage field");
            }

            if (backgroundImage == null) {
                System.out.println("testImageLoadingWithInvalidPath: PASSED");
            } else {
                System.out.println("testImageLoadingWithInvalidPath: FAILED");
            }
        }
    }

