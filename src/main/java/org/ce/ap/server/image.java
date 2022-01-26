package main.java.org.ce.ap.server;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * this class assigns a default image to image of each user
 *
 * @author ashkan_mogharab
 */
public class image implements Serializable {
    private BufferedImage image;

    /**
     * creates a new image
     */
    public image() {
        try {
            this.image = ImageIO.read(new File("C:\\Users\\ashkan mogharab\\Desktop\\default_profile.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * getter
     *
     * @return a default image for each user
     */
    public BufferedImage getImage() {
        return image;
    }
}
