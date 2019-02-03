package gui;

import game.Game;

import java.awt.*;


public class DamageSlider extends GUIComponent {
    private int originalX;
    private int originalY;
    public boolean fighting = false;
    public boolean waitForEnemyAttack = false;
    public DamageSlider(int id) {
        super(id);
    }

    public DamageSlider(int id, int x, int y, int width, int height) {
        super(id, x, y, width, height);
        this.originalX = x;
        this.originalY = y;
    }

    public DamageSlider(int id, double x, double y, double width, double height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int leftMargin, int rightMargin, int upMargin, int downMargin) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown, leftMargin, rightMargin, upMargin, downMargin);
        this.originalX = actualX;
        this.originalY = 0;
    }



    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);

        if(fighting && !waitForEnemyAttack) {
            g2d.drawString(Integer.toString(x), 10, 10);
            dx += 5;
        }
        if(x > windowParent.width - sideOffset && fighting){
            onFightPress();
            Game.world.player.timeOfAttackPress = System.currentTimeMillis();
        }
        if(fighting || waitForEnemyAttack){
            g2d.setColor(Color.white);
            g2d.fillRect(x, y, width, height);
        }
    }

    public void onFightStart(){


        ((GUIBattle)Game.gui).hasSelectedCHARActer = true;

        ((GUIBattle)Game.gui).battleInteraction.makeButtonsInvisible();
        actualX = originalX;



        fighting = true;
    }

    public void onFightPress() {

        Game.world.battle.applyDamage((double)((double)((double)300 - (double)Math.abs((double)windowParent.width / (double)2 -(double)x))/ (double)300));
        (Game.gui.getComponentById(-13376)).visible = true;
        ((PercentBar)Game.gui.getComponentById(-13376)).percent = (int)((float)Game.world.battle.enemies.get(0).health / Game.world.battle.enemies.get(0).constantHP * 100);
        fighting = false;
        waitForEnemyAttack = true;
    }

    public void onFightEnd() {

        (Game.gui.getComponentById(-13376)).visible = false;
        waitForEnemyAttack = false;
    }
}
