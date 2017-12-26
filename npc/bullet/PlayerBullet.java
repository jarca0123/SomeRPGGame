package npc.bullet;

import game.Game;
import gui.JustText;
import gui.PercentBar;
import npc.NPC;

import java.awt.*;


public class PlayerBullet extends Bullet {
    private long lastDamage;
    private int msOfDamageResistance = 2000;

    public PlayerBullet(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update() {
        super.update();

        for (NPC npc : Game.world.battle.enemies) {
            for (Bullet bullet : npc.bullets) {
                if (bullet.bounds.intersects(bounds)) {
                    if (System.currentTimeMillis() > lastDamage + msOfDamageResistance) {
                        lastDamage = System.currentTimeMillis();
                        Game.world.player.health -= ((double) npc.AT / (double) Game.world.player.stats.DF);
                        ((PercentBar) Game.gui.getComponentById(-23371)).percent = (int) ((float) Game.world.player.health / Game.world.player.constantHP * 100);
                        ((JustText) Game.gui.getComponentById(-3133714)).text = Integer.toString(Game.world.player.health) + " / " + Integer.toString(Game.world.player.constantHP);
                    }
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        g2d.drawImage(Game.sprites.get(12), Game.world.battle.battleInteraction.x + x, Game.world.battle.battleInteraction.y + y, 20, 20, null);
    }
}
