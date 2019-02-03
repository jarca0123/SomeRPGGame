package graphics;

import game.Game;

import sun.java2d.SunGraphics2D;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;


public abstract class JGraphics2D{




    public static boolean drawGameImage(Graphics2D graphics2D, Image img, int x, int y) {
        return graphics2D.drawImage(img, x + Game.world.offsetX, y + Game.world.offsetY, null);
    }
    public static boolean drawGameImage(Graphics2D graphics2D, Image img, int x, int y, int width, int height) {
        if(x + Game.world.offsetX < -48 || Game.GAME_WIDTH <x + Game.world.offsetX ||y + Game.world.offsetY < -48 || Game.GAME_HEIGHT<y + Game.world.offsetY) {
            return false;
        } else {
            return graphics2D.drawImage(img, x + Game.world.offsetX, y + Game.world.offsetY, width, height, null);
        }
    }
    public static boolean drawGUIImage(Graphics2D graphics2D, Image img, int x, int y, int width, int height) {
        return graphics2D.drawImage(img, x, y, null);
    }
    public static void drawGameRect(Graphics2D graphics2D, int x, int y, int width, int height) {
        graphics2D.drawRect(x + Game.world.offsetX, y + Game.world.offsetY, width, height);
    }
    public static void drawGUIRect(Graphics2D graphics2D, int x, int y, int width, int height) {
        graphics2D.drawRect(x, y, width, height);
    }
    public static void fillGameRect(Graphics2D graphics2D, int x, int y, int width, int height){


        graphics2D.fillRect(x + Game.world.offsetX, y + Game.world.offsetY, width, height);
    }
    public static void fillGUIRect(Graphics2D graphics2D, int x, int y, int width, int height){
        graphics2D.fillRect(x, y, width, height);
    }
}
