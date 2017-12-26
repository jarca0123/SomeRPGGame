package npc.bullet;

import game.Game;

import java.awt.*;


public class Bullet {
    public int x;
    public int y;
    int width;
    int height;
    public int dx;
    public int dy;
    private int dWidth;
    private int dHeight;
    Rectangle bounds;
    Bullet(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle(Game.world.battle.battleInteraction.x + x, Game.world.battle.battleInteraction.y +y, width, height);
    }

    public void update(){

    }

    public void draw(Graphics2D g2d){
        x += dx;
        y += dy;
        width += dWidth;
        height += dHeight;
        dx = 0;
        dy = 0;
        dWidth = 0;
        dHeight = 0;
        bounds = new Rectangle(Game.world.battle.battleInteraction.x + x, Game.world.battle.battleInteraction.y +y, width, height);

    }

}
