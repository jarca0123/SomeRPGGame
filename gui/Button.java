package gui;

import game.Game;
import graphics.JGraphics2D;

import java.awt.*;
import java.awt.event.MouseEvent;


public class Button extends GUIComponent{
    public String text;
    public Button(int id){
        super(id);
        this.text = "Button";
    }
    public Button(String text, int id, int x, int y, int width, int height){
        super(id, x, y, width, height);
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle(x, y, width * 2, height);
    }
    public Button(String text, int id){
        super(id);
        this.text = text;
    }
    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);




        g2d.setColor(Color.black);
        g2d.drawRect(x, y, width + (height), height);
        drawCenteredString(g2d, text, new Rectangle(x, y, width + height, height), new Font("Determination Mono", Font.BOLD, 24));

    }
}
