package gui;

import game.Game;
import gui.window.BattleInteraction;
import gui.window.GameInteraction;
import npc.NPC;
import npc.bullet.Bullet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;


public class GUIBattle extends GUI {
    public ArrayList<NPC> enemies;
    public BattleInteraction battleInteraction;
    public EnemyBattleInteractionText enemyBattleInteractionText;
    public ImageComponent enemyPic;
    public int selectedSelectionId = 1;
    public int selectedSubSelectionId = 1;
    public int selectedBattleButton = 1;
    public boolean hasSelectedAction = false;
    public boolean hasSelectedCHARActer = false;
    public boolean hasSelectedSubAction = false;
    public Color tempColor;
    public PercentBar enemyHealthBar;
    public PercentBar healthBar;

    public GUIBattle(){

    }

    @Override
    public void onStart() {
        super.onStart();
        tempColor = Game.game.getGraphics().getColor();
        enemies = Game.world.battle.enemies;
        Game.world.battle.gui = this;
        for(NPC npc: enemies){
            npc.bullets = new ArrayList<>();
        }
        battleInteraction = new BattleInteraction(-13370, 20, 250, 600, Math.abs(250 - (Game.GAME_HEIGHT - 50)) - 50, false, enemies.get(0));
        System.out.println(battleInteraction.height);
        addGUIWindow(battleInteraction);

        addComponent(new JustText(-3133711, 20, 380, 40, 50, null, null, null, null, 0, 0, 5, 0, "You", true, 30));
        addComponent(new JustText(-3133712, 135, 380, 40, 50, null, null, null, null, 0, 0, 5, 0, "NO LVL", true, 30));
        addComponent(new JustText(-3133713, (Game.GAME_WIDTH / 2) - 90, 380, 40, 50, null, null, null, null, 0, 0, 5, 0, "HP", true, 20));
        addComponent(new JustText(-3133714, (Game.GAME_WIDTH / 2) - 100 + Game.world.player.constantHP + 50, 380, 100, 50, null, null, null, null, 0, 0, 5, 0, Integer.toString(Game.world.player.health) + " / " + Integer.toString(Game.world.player.constantHP), true, 20));
        addComponent(new ImageComponent(-13371, 20, Game.GAME_HEIGHT - 50, Game.sprites.get(0).getWidth(), Game.sprites.get(0).getHeight(), Game.sprites.get(0), Game.sprites.get(1), true));
        addComponent(new ImageComponent(-13372, 183, Game.GAME_HEIGHT - 50, Game.sprites.get(0).getWidth(), Game.sprites.get(0).getHeight(), Game.sprites.get(2), Game.sprites.get(3), true));
        addComponent(new ImageComponent(-13373, 346, Game.GAME_HEIGHT - 50, Game.sprites.get(0).getWidth(), Game.sprites.get(0).getHeight(), Game.sprites.get(4), Game.sprites.get(5), true));
        addComponent(new ImageComponent(-13374, 509, Game.GAME_HEIGHT - 50, Game.sprites.get(0).getWidth(), Game.sprites.get(0).getHeight(), Game.sprites.get(6), Game.sprites.get(7), true));
        addComponent(new ImageComponent(-13375, 20, 0, Game.sprites.get(8).getWidth(), Game.sprites.get(8).getHeight(), Game.sprites.get(8), null, false));
        ((ImageComponent) getComponentById(-13370 - selectedBattleButton)).isSelected = true;
        enemyHealthBar = (PercentBar)addComponent(new PercentBar(-13376, (Game.GAME_WIDTH / 2) - 100, 150, 200, 30, null, null, null, null, 0, 0, 0, 0, Color.black, Color.green));
        healthBar = (PercentBar) addComponent(new PercentBar(-23371, (Game.GAME_WIDTH / 2) - 50, 390, Game.world.player.constantHP, 30, null, null, null, null, 0, 0, 0, 0, Color.red, Color.YELLOW));
        enemyPic = (ImageComponent)addComponent(new ImageComponent(-13377, 30, Game.GAME_HEIGHT - 40, Game.sprites.get(12).getWidth(), Game.sprites.get(12).getHeight(), Game.sprites.get(12), null, false));
        enemyBattleInteractionText = (EnemyBattleInteractionText) addComponent(new EnemyBattleInteractionText(-696969, 320 + 50, 50, 100, 100, null, null, null, null, 0, 0, 0, 0).setEnabled(false).setVisible(false));

    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g2d.setColor(tempColor);
        super.draw(g2d);
        g2d.setClip(null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));




        if(enemies.size() > 0){
            g2d.drawImage(enemies.get(0).image, (Game.GAME_WIDTH / 2) -( enemies.get(0).image.getWidth() / 2),  100, null);
        }


    }

    @Override
    public void actionPerfomed(GUIComponent guiComponent) {
        super.actionPerfomed(guiComponent);


        enemyPic.visible = false;
        if(guiComponent.id == -13371){

            Game.world.battle.selectedMenu = 1;
            Game.world.battle.generateDialogue("1");
            hasSelectedAction = true;
        }
        if(guiComponent.id == -13372){

            Game.world.battle.selectedMenu = 2;
            Game.world.battle.generateDialogue("2");
            hasSelectedAction = true;

        }
        if(guiComponent.id == -13373){

            Game.world.battle.selectedMenu = 3;
            Game.world.battle.generateDialogue("3");

            hasSelectedAction = true;
        }
        if(guiComponent.id == -13374){

            Game.world.battle.selectedMenu = 4;
            Game.world.battle.generateDialogue("4");
            hasSelectedAction = true;
        }
        if(hasSelectedAction){
            ((GUIBattle)Game.gui).battleInteraction.battleInteractionText.visible = false;
        }
    }

    @Override
    public void onBackKey() {
        super.onBackKey();
        if (hasSelectedAction) {
            if (!hasSelectedCHARActer && !hasSelectedSubAction) {
                ((GUIBattle)Game.gui).enemyPic.visible = true;
                ((GUIBattle)Game.gui).battleInteraction.battleInteractionText.visible = true;
                hasSelectedAction = false;
                Game.world.battle.selectedMenu = 0;
            }
        }
    }

    public void makeButtonsInvisible() {
        getComponentById(-13371).visible = false;
        getComponentById(-13371).enabled = false;
        getComponentById(-13372).visible = false;
        getComponentById(-13372).enabled = false;
        getComponentById(-13373).visible = false;
        getComponentById(-13373).enabled = false;
        getComponentById(-13374).visible = false;
        getComponentById(-13374).enabled = false;
    }
}
