package gui;

import game.Game;
import graphics.JGraphics2D;

import java.awt.*;
import java.awt.image.BufferedImage;


public class ImageNText extends GUIComponent {
    private BufferedImage image;
    private String text;
    public ImageNText(int id, BufferedImage image, String text) {
        super(id);
        this.image = image;
    }

    public ImageNText(int id, int x, int y, int width, int height, BufferedImage image, String text) {
        super(id, x, y, width, height);
        this.image = image;
        this.text = text;
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        if(image != null && visible)
        g2d.drawImage(image, x, y, width / 4, width / 4, null);
        drawCenteredString(g2d, text, new Rectangle(x, y, width, height), g2d.getFont());
    }
}
