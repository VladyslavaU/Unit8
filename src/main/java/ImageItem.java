import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Represents an image, drawn with its center at a specified point.
 */
public class ImageItem {

    private final BufferedImage IMAGE;
    private final int CENTER_X;
    private final int CENTER_Y;

    public ImageItem(BufferedImage image, int centerX, int centerY) {
        this.IMAGE = image;
        this.CENTER_X = centerX;
        this.CENTER_Y = centerY;
    }

    public void draw(Graphics g) {
        g.drawImage(IMAGE, CENTER_X - IMAGE.getWidth() / 2,
                CENTER_Y - IMAGE.getHeight() / 2, null);
    }

    public boolean contains(int x, int y) {
        int w = IMAGE.getWidth();
        int h = IMAGE.getHeight();
        return x > CENTER_X - w / 2 && x < CENTER_X + w / 2 && y > CENTER_Y - h / 2
                && y < CENTER_Y + h / 2;
    }

}