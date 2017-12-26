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
    private int playerHP;
    public ArrayList<NPC> enemies;
    public BattleDialogue battleDialogue;
    public BattleInteraction battleInteraction;

    public boolean fighting = false;
    public boolean enemyFighting = false;
    public boolean enemyPreFighting = false;
    private int originalWidth;
    private int originalHeight;
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
        this.battleDialogue = new BattleDialogue("some text");

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
        originalWidth = Game.gui.getWindowById(-13370).width;
        originalHeight = Game.gui.getWindowById(-13370).height;
        hasBattleStarted = true;

    }

    public void onBattleEnd(){


        Game.world.battle = null;

        Game.world.room.onEnter();
        Game.world.spawnNPCWithId(1);
        Game.gamePanel.fadeIn(1);
        Game.game.setGUI(new GUIGame());
    }

    public void onFightStart(){
        ((DamageSlider)Game.gui.getWindowById(-13370).getComponentById(-6913370)).onFightStart();
        fighting = true;
    }

    public void onFightPress() {
        ((DamageSlider)Game.gui.getWindowById(-13370).getComponentById(-6913370)).onFightPress();
        hasPressedFight = true;
    }

    public void onFightEnd(){
        hasPressedFight = false;
        ((DamageSlider)Game.gui.getWindowById(-13370).getComponentById(-6913370)).onFightEnd();
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


            Game.gui.getWindowById(-13370).resizeWindow(originalWidth, originalHeight, 2);
            Game.gui.getWindowById(-13370).getComponentById(-313370).visible = true;
            isBattleEnding = true;
            battleDialogue.enemyDialogueText = "YOU WIN!!!111";
        } else {
            onEnemyAttackStart();
        }
    }

    private void onEnemyAttackStart(){

        canSeeEnemyDialogue = true;
        ((GUIBattle)Game.gui).getComponentById(-696969).visible = true;
        ((EnemyBattleInteractionText)Game.gui.getComponentById(-696969)).nextDialogue("text");
        ((EnemyBattleInteractionText)Game.gui.getComponentById(-696969)).setEnabled(true).setVisible(true);
        Game.gui.getWindowById(-13370).resizeWindow(Math.abs(25 - (Game.GAME_HEIGHT - 75)) - 20, Math.abs(25 - (Game.GAME_HEIGHT - 75)) - 20, 20);
        for(NPC npc: enemies){
            npc.onEnemyAttackStart();
        }
    }

    private void battleLevelEnd(){
        Game.world.player.battleLevelEnd();
        for(NPC npc : enemies){
            npc.battleLevelEnd();
        }

        enemyFighting = false;
        Game.gui.getWindowById(-13370).resizeWindow(originalWidth, originalHeight, 20);
        battleDialogue.parseDialogue("There is not much to say...");
        for(NPC npc : enemies){
            if(npc.canBeSpared){
                battleDialogue.parseDialogue(npc.name + " is sparing you.");
            }
        }
        ((JustText)battleInteraction.getComponentById(-313370)).visible = true;
        Game.gui.getComponentById(-13377).visible = true;

        ((GUIBattle)Game.gui).hasSelectedAction = false;
        ((GUIBattle)Game.gui).hasSelectedSubAction = false;
        ((GUIBattle)Game.gui).hasSelectedCHARActer = false;
    }

    private void battleLevelInit(){
        enemyFighting = true;
        enemyPreFighting = false;
        for(NPC npc : enemies){
            npc.isFighting = true;
        }
        battleWidth = Game.gui.getWindowById(-13370).width;
        battleHeight = Game.gui.getWindowById(-13370).height;
    }

    private void battleLevelUpdate(){

        Game.world.player.bullet.update();
        for (NPC npc : enemies) {
            npc.battleLevelUpdate();
        }

    }

    public void update(){
        if(((!Game.gui.getWindowById(-13370).isResizing && !enemyFighting && ((GUIBattle)Game.gui).hasSelectedAction) || (!Game.gui.getWindowById(-13370).isResizing && !enemyFighting && ((GUIBattle)Game.gui).hasSelectedCHARActer)) && !canSeeEnemyDialogue) {
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
        if(!Game.gui.getWindowById(-13370).isResizing) {
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
