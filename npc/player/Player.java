package npc.player;

import animation.Direction;
import animation.NPCAnimationState;
import game.Game;
import gui.GUIBattle;
import gui.ImageComponent;
import gui.window.GameInteraction;
import npc.NPC;
import npc.bullet.PlayerBullet;
import tiles.Tile;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;


public class Player extends NPC implements KeyListener {
    private int speed = 3;
    private int holdingMoveKeys = 0;
    public GameInteraction interactionInstance;
    private GUIBattle guiBattle;
    public long timeOfAttackPress;
    public PlayerBullet bullet;
    public PlayerStats stats;
    public boolean menuKey;
    public boolean actionKey;
    public boolean backKey;
    private ArrayList<Integer> movementKeys = new ArrayList<Integer>() {{
        add(KeyEvent.VK_UP);
        add(KeyEvent.VK_DOWN);
        add(KeyEvent.VK_LEFT);
        add(KeyEvent.VK_RIGHT);
    }};


    public Player(int id, String name, String imageSrc, int x, int y) {
        super(id, name, imageSrc, x, y);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        stats = Game.world.playerStats;
        stats.AT = 12;
        Game.game.addKeyListener(this);
    }

    @Override
    public void battleLevelInit() {
        super.battleLevelInit();
        bullet = new PlayerBullet(Game.world.battle.battleInteraction.width / 2, Game.world.battle.battleInteraction.height / 2, 20, 20);
        bullets.add(bullet);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public Player(int id, String name, String imageSrc, int x, int y, int tileSetWidth, int tileSetHeight, int width, int height, boolean useTileset) {
        super(id, name, imageSrc, x, y, tileSetWidth, tileSetHeight, width, height, useTileset);

    }

    @Override
    public void keyPressed(KeyEvent e) {



    }



    @Override
    public void keyReleased(KeyEvent e) {


    }

    @Override
    public void battleUpdate() {
        super.battleUpdate();
        up = Game.gui.keyStrokes.contains(KeyEvent.VK_UP);
        down = Game.gui.keyStrokes.contains(KeyEvent.VK_DOWN);
        left = Game.gui.keyStrokes.contains(KeyEvent.VK_LEFT);
        right = Game.gui.keyStrokes.contains(KeyEvent.VK_RIGHT);
        if(timeOfAttackPress != 0){
            if(System.currentTimeMillis() - timeOfAttackPress > 1000 && !Game.world.battle.enemyFighting){
                Game.world.battle.onFightEnd();
                timeOfAttackPress = 0;
            }
        }
        if(Game.world.battle != null) {
            if (((GUIBattle) (Game.gui)).hasSelectedAction) {
                if (!Game.world.battle.enemyFighting) {
                    if (left) {

                        Game.gui.keyStrokes.remove((Integer)KeyEvent.VK_LEFT);
                        if (((GUIBattle) (Game.gui)).selectedSelectionId > 2 && ((GUIBattle) (Game.gui)).selectedSelectionId < Game.world.battle.battleDialogue.dialogueText.size() + 1) {

                            ((GUIBattle) (Game.gui)).selectedSelectionId = ((GUIBattle) (Game.gui)).selectedSelectionId - 2;

                        }
                    }
                    if (right) {
                        Game.gui.keyStrokes.remove((Integer)KeyEvent.VK_RIGHT);
                        if (((GUIBattle) (Game.gui)).selectedSelectionId > 0 && ((GUIBattle) (Game.gui)).selectedSelectionId < Game.world.battle.battleDialogue.dialogueText.size() - 1) {

                            ((GUIBattle) (Game.gui)).selectedSelectionId = ((GUIBattle) (Game.gui)).selectedSelectionId + 2;

                        }
                    }
                    if (up) {
                        Game.gui.keyStrokes.remove((Integer)KeyEvent.VK_UP);
                        if (((GUIBattle) (Game.gui)).selectedSelectionId > 1 && ((GUIBattle) (Game.gui)).selectedSelectionId <Game.world.battle.battleDialogue.dialogueText.size() + 1 && ((GUIBattle) (Game.gui)).selectedSelectionId % 2 != 1) {

                            ((GUIBattle) (Game.gui)).selectedSelectionId = ((GUIBattle) (Game.gui)).selectedSelectionId - 1;

                        }
                    }
                    if (down) {
                        Game.gui.keyStrokes.remove((Integer)KeyEvent.VK_DOWN);
                        if (((GUIBattle) (Game.gui)).selectedSelectionId > 0 && ((GUIBattle) (Game.gui)).selectedSelectionId < Game.world.battle.battleDialogue.dialogueText.size() && ((GUIBattle) (Game.gui)).selectedSelectionId % 2 != 0) {

                            ((GUIBattle) (Game.gui)).selectedSelectionId = ((GUIBattle) (Game.gui)).selectedSelectionId + 1;

                        }
                    }


                } else {
                    if (up) {
                        if(!(bullet.y - speed < 0))
                        bullet.dy -= speed;
                    }
                    if (down) {
                        if(!(bullet.y + speed > Game.world.battle.battleHeight - 20))
                        bullet.dy += speed;
                    }
                    if (left) {
                        if(!(bullet.x - speed < 0))
                        bullet.dx -= speed;
                    }
                    if (right) {
                        if(!(bullet.x + speed > Game.world.battle.battleWidth - 20))
                        bullet.dx += speed;
                    }
                }
            } else {
                if (left) {
                    Game.gui.keyStrokes.remove((Integer)KeyEvent.VK_LEFT);
                    if (((GUIBattle) (Game.gui)).selectedBattleButton > 1 && ((GUIBattle) (Game.gui)).selectedBattleButton < 5) {

                        ((ImageComponent) Game.gui.getComponentById(-((GUIBattle) (Game.gui)).selectedBattleButton - 13370)).isSelected = false;

                        ((GUIBattle) (Game.gui)).selectedBattleButton--;

                        ((ImageComponent)Game.gui.getComponentById(-((GUIBattle) (Game.gui)).selectedBattleButton - 13370)).isSelected = true;
                    }
                }
                if (right) {
                    Game.gui.keyStrokes.remove((Integer)KeyEvent.VK_RIGHT);
                    if (((GUIBattle) (Game.gui)).selectedBattleButton > 0 && ((GUIBattle) (Game.gui)).selectedBattleButton < 4) {

                        ((ImageComponent) Game.gui.getComponentById(-((GUIBattle) (Game.gui)).selectedBattleButton - 13370)).isSelected = false;

                        ((GUIBattle) (Game.gui)).selectedBattleButton++;

                        ((ImageComponent) Game.gui.getComponentById(-((GUIBattle) (Game.gui)).selectedBattleButton - 13370)).isSelected = true;
                    }
                }
                Game.gui.getComponentById(-13377).x = 30 + (163 * (((GUIBattle) (Game.gui)).selectedBattleButton - 1));
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        g2d.setClip(null);

        g2d.drawRect(x + Game.world.offsetX, y + Game.world.offsetY, width, height);
        for(int i = 0; i < Game.gui.keyStrokes.size(); i++){
            Color c = g2d.getColor();
            g2d.setColor(Color.white);
            g2d.drawString(KeyEvent.getKeyText(Game.gui.keyStrokes.get(i)), 20, 20 + (20 * i));
            g2d.setColor(c);
        }
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        up = Game.gui.keyStrokes.contains(KeyEvent.VK_UP);
        down = Game.gui.keyStrokes.contains(KeyEvent.VK_DOWN);
        left = Game.gui.keyStrokes.contains(KeyEvent.VK_LEFT);
        right = Game.gui.keyStrokes.contains(KeyEvent.VK_RIGHT);



        int tempOffsetX = 0;
        int tempOffsetY = 0;
        if(( centerX) >((Game.world.room.width * Game.tilesetSize) - Game.GAME_WIDTH / 2)){
            tempOffsetX -= -(centerX - ((Game.world.room.width * Game.tilesetSize) - Game.GAME_WIDTH / 2));
        }
        if(((Game.GAME_WIDTH) - centerX) < Game.GAME_WIDTH / 2){
            tempOffsetX += -((Game.GAME_WIDTH / 2)  - (Game.GAME_WIDTH- centerX));
        }
        if(( centerY) >((Game.world.room.height * Game.tilesetSize) - Game.GAME_HEIGHT / 2)){
            tempOffsetY -= -(centerY - ((Game.world.room.height * Game.tilesetSize) - Game.GAME_HEIGHT / 2));
        }
        if(((Game.GAME_HEIGHT) - centerY) < Game.GAME_HEIGHT / 2){
            tempOffsetY += -((Game.GAME_HEIGHT / 2)  - (Game.GAME_HEIGHT- centerY));
        }
        Game.world.offsetX = tempOffsetX;
        Game.world.offsetY = tempOffsetY;

        if(!Game.world.isInteracting) {
            int movementKey = 0;

            for(int i : Game.gui.keyStrokes) {
                if(movementKey != 0) break;
                for(int movement : movementKeys){
                    if(i == movement){
                        movementKey = movement;
                        break;
                    }
                }
            }
            if(movementKey == KeyEvent.VK_LEFT) facing = Direction.LEFT;
            if(movementKey == KeyEvent.VK_RIGHT) facing = Direction.RIGHT;
            if(movementKey == KeyEvent.VK_UP) facing = Direction.UP;
            if(movementKey == KeyEvent.VK_DOWN) facing = Direction.DOWN;
            if (up && !Game.world.willCollide(bounds, 0, -speed)) {
                dy -= speed * delta;
            }
            if (down && !Game.world.willCollide(bounds, 0, speed)) {
                dy += speed * delta;
            }
            if (left && !Game.world.willCollide(bounds, -speed, 0)) {
                dx -= speed * delta;
            }
            if (right && !Game.world.willCollide(bounds, speed, 0)) {
                dx += speed * delta;
            }
            if((up || down || left || right) && (dx != 0 && dy != 0)){
                animationState = NPCAnimationState.WALKING;

            } else if(dx == 0 && dy == 0){

                animationState = NPCAnimationState.STANDING;
            } else {
                animationState = NPCAnimationState.WALKING;

            }
        }
    }

    @Override
    public void battleLevelEnd() {
        bullet = null;
    }

    public void onActionKey() {
        if (Game.world.battle == null) {
            if (facing == Direction.UP) {
                if (Game.world.willCollide(bounds, 0, -(speed * 4))) {
                    Object thing = Game.world.getNearestCollidingTileOrNPC(bounds, 0, -(speed * 4));
                    if (thing instanceof Tile) {
                        ((Tile) thing).onInteraction();
                    } else if (thing instanceof NPC) {
                        ((NPC) thing).onInteraction();
                    }

                }
            }
            if (facing == Direction.DOWN) {
                if (Game.world.willCollide(bounds, 0, (speed * 4))) {
                    Object thing = Game.world.getNearestCollidingTileOrNPC(bounds, 0, (speed * 4));
                    if (thing instanceof Tile) {
                        ((Tile) thing).onInteraction();
                    } else if (thing instanceof NPC) {
                        ((NPC) thing).onInteraction();
                    }
                }
            }
            if (facing == Direction.LEFT) {
                if (Game.world.willCollide(bounds, -(speed * 4), 0)) {
                    Object thing = Game.world.getNearestCollidingTileOrNPC(bounds, -(speed * 4), 0);
                    if (thing instanceof Tile) {
                        ((Tile) thing).onInteraction();
                    } else if (thing instanceof NPC) {
                        ((NPC) thing).onInteraction();
                    }

                }
            }
            if (facing == Direction.RIGHT) {
                if (Game.world.willCollide(bounds, (speed * 4), 0)) {
                    Object thing = Game.world.getNearestCollidingTileOrNPC(bounds, (speed * 4), 0);
                    if (thing instanceof Tile) {
                        ((Tile) thing).onInteraction();
                    } else if (thing instanceof NPC) {
                        ((NPC) thing).onInteraction();
                    }
                }
            }
        } else {

            if (!Game.world.battle.isBattleEnding) {
                if (!((GUIBattle) (Game.gui)).hasSelectedAction) {
                    Game.gui.actionPerfomed(Game.gui.getComponentById(-((GUIBattle) (Game.gui)).selectedBattleButton - 13370));
                } else {
                    if ((!((GUIBattle) (Game.gui)).hasSelectedCHARActer && Game.world.battle.selectedMenu != 2) || (!((GUIBattle) (Game.gui)).hasSelectedSubAction && Game.world.battle.selectedMenu == 2)) {
                        Game.gui.getWindowById(-13370).getComponentById(-313379).visible = false;


                        if (Game.world.battle.selectedMenu == 1) {
                            Game.world.battle.onFightStart();
                        } else if (Game.world.battle.selectedMenu == 2) {
                            Game.world.battle.battleDialogue.dialogueText = Game.world.battle.enemies.get(((GUIBattle) (Game.gui)).selectedSelectionId - 1).generateActionDialogue();
                        } else if (Game.world.battle.selectedMenu == 3) {

                            Game.world.inventory.inventory.get(((GUIBattle) (Game.gui)).selectedSelectionId - 1).onConsume();
                            Game.world.battle.onFightEnd();
                        } else if (Game.world.battle.selectedMenu == 4) {
                            Game.world.battle.enemies.get(((GUIBattle) (Game.gui)).selectedSelectionId - 1).spare();
                        }
                    } else {
                        if (Game.world.battle.fighting && !Game.world.battle.hasPressedFight) {
                            Game.world.battle.onFightPress();
                            timeOfAttackPress = System.currentTimeMillis();
                        } else if (Game.world.battle.canSeeEnemyDialogue) {
                            Game.world.battle.canSeeEnemyDialogue = false;
                            Game.gui.getComponentById(-696969).setVisible(false).setEnabled(false);
                        }
                    }
                }
            } else {
                Game.world.battle.onBattleEnd();
            }
        }
    }
}
