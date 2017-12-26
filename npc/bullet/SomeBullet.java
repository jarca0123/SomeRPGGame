package npc.bullet;

import animation.Direction;
import game.Game;

import java.awt.*;


public class SomeBullet extends Bullet {
    private Direction direction;
    public SomeBullet(int x, int y, int width, int height, Direction direction) {
        super(x, y, width, height);
        this.direction = direction;
        if (direction == Direction.UP || direction == Direction.DOWN){
            this.x = Game.world.player.bullet.x;
        } else if(direction == Direction.LEFT || direction == Direction.RIGHT)  {
            this.y = Game.world.player.bullet.y;
        }
        if(direction == Direction.LEFT){
            this.x = 100 + Game.world.battle.battleWidth;
        } else if(direction == Direction.RIGHT){
            this.x = -100;
        } else if(direction == Direction.UP){
            this.y = 100 + Game.world.battle.battleHeight;
        } else if(direction == Direction.DOWN){
            this.y = -100;
        }
    }

    @Override
    public void update() {
        super.update();
        if(direction == Direction.LEFT){
            this.dx = -2;
        } else if(direction == Direction.RIGHT){
            this.dx = 2;
        } else if(direction == Direction.UP){
            this.dy = -2;
        } else if(direction == Direction.DOWN){
            this.dy = 2;
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        g2d.fillRect(Game.world.battle.battleInteraction.x + x, Game.world.battle.battleInteraction.y + y, width, height);
    }
}
