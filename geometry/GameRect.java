package geometry;

import game.Game;

import java.awt.*;


public class GameRect extends Rectangle{
    public int originalX;
    public int originalY;
    public int originalWidth;
    public int originalHeight;
    Rectangle originalRect;
    public GameRect(int x, int y, int width, int height){
        this.originalX = x;
        this.originalY = y;
        this.originalWidth = width;
        this.originalHeight = height;
        originalRect = new Rectangle(originalX, originalY, originalWidth, originalHeight);
        this.x = x + Game.world.offsetX;
        this.y = y + Game.world.offsetY;
        this.width = width;
        this.height = height;
    }

    public Rectangle getOriginalRect() {
        return originalRect;
    }
}
