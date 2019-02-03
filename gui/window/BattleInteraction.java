package gui.window;

import battle.Battle;
import game.Game;
import graphics.JGraphics2D;
import gui.*;
import npc.NPC;
import npc.bullet.Bullet;

import java.awt.*;
import java.util.ArrayList;


public class BattleInteraction extends Window {

    public ImageComponent heartSelectImage;
    public NPC target;
    int dialogueId = 0;
    int charaDelay = 200;
    public SelectionMenu selectionMenu;
    public BattleInteractionText battleInteractionText;
    public DamageSlider damageSlider;

    public BattleInteraction(int id, boolean actionBarEnabled, NPC target) {
        super(id, actionBarEnabled);
        this.target = target;
    }

    public BattleInteraction(int id, int x, int y, int width, int height, boolean actionBarEnabled, NPC target) {
        super(id, x, y, width, height, actionBarEnabled);
        this.target = target;
        battleInteractionText = (BattleInteractionText) addComponent(new BattleInteractionText(-313370, 0, 0, 0, Game.game.getGraphics().getFontMetrics().getHeight(), this, this, this, this, 10, 10, 10, 10, Game.world.battle.battleDialogue.enemyDialogueText, false, 25));

        selectionMenu = new SelectionMenu(-313371, 40, 10, 580, 100, null, null, null, null, 8, 2);
        addComponent(selectionMenu);
        selectionMenu.addSelection(new InteractionText(-313371, 0, 0, (300 / 3), 100 / 2, ""));
        selectionMenu.addSelection(new InteractionText(-313372, 0, 40, (300 / 3), 100 / 2, ""));
        selectionMenu.addSelection(new InteractionText(-313373, 300, 0, (300 / 3), 100 / 2, ""));
        selectionMenu.addSelection(new InteractionText(-313374, 300, 40, (300 / 3), 100 / 2, ""));
        selectionMenu.addSelection(new InteractionText(-313375, 0, 0, (300 / 3), 100 / 2, ""));
        selectionMenu.addSelection(new InteractionText(-313376, 0, 40, (300 / 3), 100 / 2, ""));
        selectionMenu.addSelection(new InteractionText(-313377, 300, 0, (300 / 3), 100 / 2, ""));
        selectionMenu.addSelection(new InteractionText(-313378, 300, 40, (300 / 3), 100 / 2, ""));
        heartSelectImage = (ImageComponent) addComponent(new ImageComponent(-313379, 20, 10, Game.sprites.get(12).getWidth(), Game.sprites.get(12).getHeight(), Game.sprites.get(12), null, false)).setVisible(false);
        damageSlider = (DamageSlider) addComponent(new DamageSlider(-6913370, 0, -15, 10, height, null, null, this, this, 0, 0, 0, 0));
        getComponentById(-313371).visible = false;


    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        heartSelectImage.setVisible(((GUIBattle)Game.gui).hasSelectedAction &&(!(((GUIBattle)Game.gui).hasSelectedCHARActer || ((GUIBattle)Game.gui).hasSelectedSubAction)));
        if(Game.world.battle.battleDialogue != null) {
            if (Game.world.battle.battleDialogue.dialogueText != null) {

                selectionMenu.setTextOfSelections(Game.world.battle.battleDialogue.dialogueText);
            }
            if (Game.world.battle.battleDialogue.enemyDialogueText!= null) {

                battleInteractionText.text = Game.world.battle.battleDialogue.enemyDialogueText;
            }
            if (Game.world.battle.selectedMenu != 0) {
                heartSelectImage.visible = true;
                selectionMenu.visible = true;
                heartSelectImage.actualX = ((SelectionMenu)getComponentById(-313371)).selections.get(((GUIBattle)Game.gui).selectedSelectionId - 1).actualX - 40;
                heartSelectImage.actualY = ((SelectionMenu)getComponentById(-313371)).selections.get(((GUIBattle)Game.gui).selectedSelectionId - 1).actualY - 8;
            } else {
                selectionMenu.visible = false;
            }
        }
        if(Game.world.player.bullet != null) Game.world.player.bullet.draw(g2d);
        for(NPC npc : Game.world.battle.enemies){
            for(Bullet bullet : npc.bullets){
                g2d.setClip(windowBounds);
                bullet.draw(g2d);
            }
        }

    }

    public void nextDialogue() {
        dialogueId++;
        if(target.npcBattleDialogue.dialogueText.size() < dialogueId + 1){
            Game.world.isInteracting = false;

            Game.gui.removeGUIWindow(this);
        } else {


        }
    }

    public void makeButtonsInvisible() {
        getComponentById(-313371).visible = false;

        Game.world.battle.selectedMenu  = 0;
    }
}


