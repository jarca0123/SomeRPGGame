package npc;

import animation.Direction;
import game.Game;
import npc.bullet.AimingToPlayerBullet;
import npc.bullet.Bullet;
import npc.bullet.SomeBullet;


public class SomeBoss extends Boss {
    private long lastSpawn = 0l;
    public boolean horizontal = true;


    public SomeBoss(int id, String name, String imageSrc, int x, int y, int tileSetWidth, int tileSetHeight, int width, int height, boolean useTileset) {
        super(id, name, imageSrc, x, y, tileSetWidth, tileSetHeight, width, height, useTileset);
        health = 50;
        dialogueImages.add(Game.sprites.get(9));
        dialogueImages.add(Game.sprites.get(10));
        dialogueImages.add(Game.sprites.get(11));
    }

    public SomeBoss(int id, String name, String imageSrc, int x, int y) {
        super(id, name, imageSrc, x, y);
    }

    @Override
    public void onInteraction() {
        super.onInteraction();
    }


    @Override
    public void battleLevelEnd() {
        super.battleLevelEnd();
        canBeSpared = true;
    }

    @Override
    public void onBattleEnd(boolean isDead) {
            super.onBattleEnd(isDead);
        if(isDead) Game.world.decisions.get(0).setDecision(true);
    }

    @Override
    public void battleLevelUpdate() {
        super.battleLevelUpdate();
        if(lastSpawn + 500 < System.currentTimeMillis()){
            if(horizontal) {


            } else {

                bullets.add(new AimingToPlayerBullet(0, 0, 20, 20, Direction.DOWN));
            }
            lastSpawn = System.currentTimeMillis();
            horizontal = !horizontal;
        }
    }



    @Override
    public void battleLevelInit() {
        super.battleLevelInit();

    }
}
