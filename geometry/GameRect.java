package geometry;

import game.Game;

import java.awt.*;


public class GameRect extends Rectangle{
    private int originalX;
    private int originalY;
    private int originalWidth;
    private int originalHeight;
    public GameRect(int x, int y, int width, int height){
        this.originalX = x;
        this.originalY = y;
        this.originalWidth = width;
        this.originalHeight = height;
        this.x = x + Game.world.offsetX;
        this.y = y + Game.world.offsetY;
        this.width = width;
        this.height = height;
    }
}
