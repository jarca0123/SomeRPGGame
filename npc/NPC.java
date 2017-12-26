package npc;




import animation.Direction;
import animation.NPCAnimationState;
import game.Game;
import graphics.JGraphics2D;
import gui.GUIBattle;
import gui.window.*;
import gui.window.Window;
import npc.bullet.Bullet;
import npc.dialogue.BattleDialogue;
import npc.dialogue.Dialogue;
import sun.awt.image.ToolkitImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NPC implements Cloneable{

    public ArrayList<Bullet> bullets = new ArrayList<>();
    public String name;
    public int id;
    public String imageSrc;
    public int x;
    public int y;


    protected int centerX;
    protected int centerY;
    protected int dx;
    protected int dy;
    public BufferedImage image;
    public Rectangle bounds;
    public boolean isColliding = false;
    protected NPCAnimationState animationState;
    private int subImageRows;
    private int subImageCols;
    private BufferedImage[][] subImages;
    private long animationDelay;
    protected Direction facing;
    private boolean isMakingStep = false;
    protected boolean up = false;
    protected boolean down = false;
    protected boolean left = false;
    protected boolean right = false;
    public Dialogue npcDialogue;
    private boolean isLeft = false;
    public BattleDialogue npcBattleDialogue;

    protected int width;
    protected int height;
    public boolean isFighting = false;
    public long timeBattleLevel = 0l;
    public int constantHP;
    public int health = 50;
    public int DF = 10;
    public ArrayList<BufferedImage> dialogueImages = new ArrayList<>();
    public boolean canBeSpared = false;
    public int AT = 10;

    protected NPC(int id, String name, String imageSrc, int x, int y, int tileSetWidth, int tileSetHeight, int width, int height, boolean useTileset){
        this.name = name;
        this.id = id;
        try {
            this.image = ImageIO.read(getClass().getResource(imageSrc));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bounds = new Rectangle(x, y, 40, 25);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.constantHP = health;



        this.centerX = x + (tileSetWidth / 2);
        this.centerY = y + (tileSetHeight / 2);
        if(useTileset){
            if(width == 0){
                this.width = this.image.getWidth();
            }
            if(height == 0){
                this.height = this.image.getHeight();
            }
            splitTileset(tileSetWidth, tileSetHeight);
            image = subImages[1][0];

        }
        this.npcDialogue = new Dialogue("example text\n;img1;next example text\n...\n\b");
        this.npcBattleDialogue = new BattleDialogue("You are fighting with something...");
    }

    private void splitTileset(int tileSetWidth, int tileSetHeight) {


        subImageRows = image.getWidth() / tileSetWidth;
        subImageCols = image.getHeight() / tileSetHeight;
        int chunks = subImageRows * subImageCols;
        subImages = new BufferedImage[subImageRows][subImageCols];
        int count = 0;

        for (int x = 0; x < subImageRows; x++) {
            for (int y = 0; y < subImageCols; y++) {





                subImages[x][y] = Game.toBufferedImage(new ImageIcon(image.getSubimage(tileSetWidth * x, tileSetHeight * y, tileSetWidth, tileSetHeight).getScaledInstance(width, height, Image.SCALE_FAST)).getImage());



            }
        }

    }

    protected NPC(int id, String name, String imageSrc, int x, int y){
        this(id, name, imageSrc, x, y, 0, 0, 0, 0, false);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new Error("Something impossible just happened");
        }
    }

    public void draw(Graphics2D g2d){

    }



    private void updateAnimation(double delta){
        long currentTimeMs = System.currentTimeMillis();
        if(animationState == NPCAnimationState.STANDING){
            animationDelay = 0;
            if(facing == Direction.UP) {
                image = subImages[0][0];
            }
            else if(facing == Direction.DOWN) {
                image = subImages[1][0];
            }
            else if(facing == Direction.LEFT) {
                image = subImages[2][0];
            }
            else if(facing == Direction.RIGHT) {
                image = subImages[3][0];
            }
        }
        if(animationState == NPCAnimationState.WALKING){
            if(currentTimeMs - animationDelay > 175){
                isMakingStep = !isMakingStep;
                if(isMakingStep) {
                    isLeft = !isLeft;
                }
                animationDelay = System.currentTimeMillis();

            }
            if(facing == Direction.UP) {
                image = subImages[0][0];


                if(isMakingStep){
                    if(isLeft) {

                        image = subImages[0][2];
                    } else {

                        image = subImages[0][1];
                    }

                }
            }
            else if(facing == Direction.DOWN) {
                image = subImages[1][0];


                if(isMakingStep) {
                    if(isLeft) {

                        image = subImages[1][2];
                    } else {

                        image = subImages[1][1];
                    }
                }
            }
            else if(facing == Direction.LEFT) {
                image = subImages[2][0];


                if(isMakingStep)image = subImages[2][1];
            }
            else if(facing == Direction.RIGHT) {
                image = subImages[3][0];


                if(isMakingStep)image = subImages[3][1];
            }
        }
    }

    public void update(double delta){
        if(Game.world.battle != null) {
            if (Game.world.battle.enemies.contains(this) && Game.world.battle.enemyFighting) {
                battleLevelUpdate();
            }
        }
        updateAnimation(delta);
        x = x + dx;
        y = y + dy;
        centerX += dx;
        centerY += dy;
        bounds.x = x;
        bounds.y = y + (height - 24);
        dx = 0;
        dy = 0;
    }

    public void onActionSelection(int id){

    }

    public void onCreate(){}


    public void onInteraction() {
        if(!Game.world.isInteracting) {
            Game.world.player.interactionInstance = new GameInteraction(-20000 - id, 25, Game.GAME_HEIGHT - 130, Game.GAME_WIDTH - 50, 120, false, this);
            Window.addGUIWindow(Game.world.player.interactionInstance);
            Game.world.isInteracting = true;
        } else {

        }
    }

    public void onEnemyAttackStart() {

    }

    public void battleLevelUpdate() {
        if(System.currentTimeMillis() - 20000 > timeBattleLevel){
            battleLevelEnd();
        }
        for(Bullet bullet : bullets){
            bullet.update();
        }
    }

    public void battleLevelInit(){
        isFighting = true;
    }

    public void battleLevelEnd(){
        isFighting = false;
        bullets = new ArrayList<>();
    }

    public void onEnemyAttackEnd() {
    }

    public void battleUpdate() {
    }

    public void onBattleEnd(boolean isDead) {

        if(!isDead) {
            Game.world.battle.enemiesToRemove.add(this);
            Game.world.battle.onFightEnd();
        }
    }

    public void spare() {
        if(canBeSpared){
            ((GUIBattle)Game.gui).battleInteraction.makeButtonsInvisible();
            onBattleEnd(false);
        } else {
            Game.world.battle.onFightEnd();
        }
    }

    public ArrayList<String> generateActionDialogue() {
        ArrayList<String> result = new ArrayList<>();

        return null;
    }
}

