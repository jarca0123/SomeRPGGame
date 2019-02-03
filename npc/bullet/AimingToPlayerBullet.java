package npc.bullet;

import animation.Direction;
import game.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;


public class AimingToPlayerBullet extends Bullet {
    public Direction direction;
    double moveHeartangle = 0d;
    public AimingToPlayerBullet(int x, int y, int width, int height, Direction direction) {
        super(x, y, width, height);
        this.direction = direction;
        if (direction == Direction.UP || direction == Direction.DOWN){
            this.x = Game.world.player.bullet.x;
        } else if(direction == Direction.LEFT || direction == Direction.RIGHT)  {
            this.y = Game.world.player.bullet.y;
        }
        if(direction == Direction.LEFT){
            this.x = 10 + Game.world.battle.battleWidth;
        } else if(direction == Direction.RIGHT){
            this.x = -10;
        } else if(direction == Direction.UP){
            this.y = 10 + Game.world.battle.battleHeight;
        } else if(direction == Direction.DOWN){
            this.y = -10;
        }
    }

    @Override
    public void update() {
        super.update();
        moveHeartangle = Math.atan2(Game.world.player.bullet.y - y, Game.world.player.bullet.x - x);
        double moveHeartdeltaX = Math.cos(moveHeartangle);
        double moveHeartdeltaY = Math.sin(moveHeartangle);
        this.dx = (int)(double)(2 * moveHeartdeltaX);
        this.dy = (int)(double)(2 * moveHeartdeltaY);


    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);


        AffineTransform old = g2d.getTransform();
        g2d.translate(Game.world.battle.gui.battleInteraction.x + x + (width / 2), Game.world.battle.gui.battleInteraction.y + y + (height / 2));
        g2d.rotate(moveHeartangle);
        System.out.println(Math.toDegrees(moveHeartangle));


        g2d.translate(-(Game.world.battle.gui.battleInteraction.x + x + (width / 2)), -(Game.world.battle.gui.battleInteraction.y + y + (height / 2)));
        g2d.fillRect(Game.world.battle.gui.battleInteraction.x + x, Game.world.battle.gui.battleInteraction.y + y, width, height);


        g2d.rotate(-moveHeartangle);
        g2d.setTransform(old);
    }
}
