package battle;


import game.Game;
import gui.*;
import gui.window.BattleInteraction;
import npc.NPC;
import npc.dialogue.BattleDialogue;
import npc.dialogue.Dialogue;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class Battle {
    public int selectedMenu =0;
    public int enemyHP;
    public int playerHP;
    public ArrayList<NPC> enemies;
    public BattleDialogue battleDialogue;

    public GUIBattle gui;
    public boolean fighting = false;
    public boolean enemyFighting = false;
    public boolean enemyPreFighting = false;
    public int originalWidth;
    public int originalHeight;
    public int battleWidth;
    public int battleHeight;
    public ArrayList<NPC> enemiesToRemove = new ArrayList<>();
    public boolean isBattleEnding = false;
    public boolean hasBattleStarted = false;
    public boolean hasPressedFight = false;

    public boolean canSeeEnemyDialogue = false;


    public Battle(ArrayList<NPC> enemy){
        this.enemies = enemy;

        this.playerHP = 100;
        this.battleDialogue = new BattleDialogue("Well.");

    }

    public void generateDialogue(String s){
        if(s.equals("1")){

            String[] enemyNames = new String[Game.world.battle.enemies.size()];
            for(int i = 0; i < Game.world.battle.enemies.size(); i++){
                enemyNames[i] = Game.world.battle.enemies.get(i).name;
            }
            battleDialogue.dialogueText = new ArrayList<String>(Arrays.asList(enemyNames));
        }
        if(s.equals("2")){

            String[] enemyNames = new String[Game.world.battle.enemies.size()];
            for(int i = 0; i < Game.world.battle.enemies.size(); i++){
                enemyNames[i] = Game.world.battle.enemies.get(i).name;
            }
            battleDialogue.dialogueText = new ArrayList<String>(Arrays.asList(enemyNames));

        }
        if(s.equals("3")){

            battleDialogue.dialogueText = new ArrayList<String>(Arrays.asList(new String[]{"Help"}));
            String[] enemyNames = new String[Game.world.inventory.inventory.size()];
            for(int i = 0; i < Game.world.inventory.inventory.size(); i++){
                enemyNames[i] = Game.world.inventory.inventory.get(i).name;
            }
            battleDialogue.dialogueText = new ArrayList<String>(Arrays.asList(enemyNames));
        }
        if(s.equals("4")){

            String[] enemyNames = new String[Game.world.battle.enemies.size()];
            for(int i = 0; i < Game.world.battle.enemies.size(); i++){
                enemyNames[i] = Game.world.battle.enemies.get(i).name;
            }
            battleDialogue.dialogueText = new ArrayList<String>(Arrays.asList(enemyNames));
        }

    }

    public void onBattleStart() {
        Game.game.setGUI(new GUIBattle());

        originalWidth = gui.battleInteraction.width;
        originalHeight = gui.battleInteraction.height;
        Game.world.battle.hasBattleStarted = true;

    }

    public void onBattleEnd(){


        Game.world.battle = null;

        Game.world.room.onEnter();
        Game.world.spawnNPCWithId(1);
        Game.gamePanel.fadeIn(1);
        Game.game.setGUI(new GUIGame());
    }

    public void onFightStart(){
        gui.battleInteraction.damageSlider.onFightStart();
        fighting = true;
    }

    public void onFightPress() {
        gui.battleInteraction.damageSlider.onFightPress();
        hasPressedFight = true;
    }

    public void onFightEnd(){
        hasPressedFight = false;
        gui.battleInteraction.damageSlider.onFightEnd();
        fighting = false;
        enemyPreFighting = true;
        for(NPC npc : enemies){
            if(npc.health < 0){
                enemiesToRemove.add(npc);
                npc.onBattleEnd(true);
            }
        }
        for(NPC npc : enemiesToRemove){
            enemies.remove(npc);
        }
        enemiesToRemove = new ArrayList<>();
        enemies.trimToSize();
        if(enemies.size() == 0){


            gui.battleInteraction.resizeWindow(originalWidth, originalHeight, 2);
            gui.battleInteraction.battleInteractionText.visible = true;
            isBattleEnding = true;
            battleDialogue.enemyDialogueText = "YOU WIN!!!111";
        } else {
            onEnemyAttackStart();
        }
    }

    public void onEnemyAttackStart(){

        canSeeEnemyDialogue = true;
        gui.enemyBattleInteractionText.setVisible(true);
        gui.enemyBattleInteractionText.nextDialogue("My attack is lame.");
        gui.enemyBattleInteractionText.setEnabled(true).setVisible(true);
        gui.battleInteraction.resizeWindow(Math.abs(25 - (Game.GAME_HEIGHT - 75)) - 20, Math.abs(25 - (Game.GAME_HEIGHT - 75)) - 20, 20);
        for(NPC npc: enemies){
            npc.onEnemyAttackStart();
        }
    }

    public void battleLevelEnd(){
        Game.world.player.battleLevelEnd();
        for(NPC npc : enemies){
            npc.battleLevelEnd();
        }

        enemyFighting = false;
        gui.battleInteraction.resizeWindow(originalWidth, originalHeight, 20);
        battleDialogue.parseDialogue("Well again.");
        for(NPC npc : enemies){
            if(npc.canBeSpared){
                battleDialogue.parseDialogue(npc.name + " is sparing you.");
            }
        }
        gui.battleInteraction.battleInteractionText.visible = true;
        gui.enemyPic.visible = true;

        gui.hasSelectedAction = false;
        gui.hasSelectedSubAction = false;
        gui.hasSelectedCHARActer = false;
    }

    public void battleLevelInit(){
        enemyFighting = true;
        enemyPreFighting = false;
        for(NPC npc : enemies){
            npc.isFighting = true;
        }
        battleWidth = gui.battleInteraction.width;
        battleHeight = gui.battleInteraction.height;
    }

    public void battleLevelUpdate(){

        Game.world.player.bullet.update();
        for (NPC npc : enemies) {
            npc.battleLevelUpdate();
        }

    }

    public void update(){
        if(((!Game.gui.getWindowById(-13370).isResizing && !enemyFighting && gui.hasSelectedAction) || (!Game.gui.getWindowById(-13370).isResizing && !enemyFighting && gui.hasSelectedCHARActer)) && !canSeeEnemyDialogue) {
            Game.world.player.battleLevelInit();
            for(NPC npc: enemies){
                npc.timeBattleLevel = System.currentTimeMillis();
                npc.battleLevelInit();
                battleLevelInit();
            }

        }
        boolean stillFighting = false;
        for(NPC npc : enemies){
            if(npc.isFighting){
                stillFighting = true;
            }
        }
        if(!gui.battleInteraction.isResizing) {
            if(enemyFighting) {
                if(!canSeeEnemyDialogue) {
                    if (!stillFighting) {
                        battleLevelEnd();
                    } else {
                        battleLevelUpdate();
                    }
                }
            }
        }


    }

    public void applyDamage(double percent) {

        for (NPC npc : enemies) {
           npc.health -= ((double)(Game.world.player.stats.AT + Game.world.player.stats.equipedAT.AT) / (double)npc.DF) * (double)percent;

        }
    }
}
