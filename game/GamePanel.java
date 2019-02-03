package game;

import battle.Battle;
import file.RoomLoader;
import file.RoomSaver;
import geometry.GameRect;
import graphics.JGraphics2D;
import graphics.fx.Effect;
import graphics.fx.FadeEffect;
import npc.NPC;
import tiles.Tile;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;


public class GamePanel extends JPanel {

    public int mau5X;
    public int mau5Y;
    public boolean fadeIn = false;
    public boolean fadeOut = false;
    public int speed = 10;
    public static String consoleLog = "";
    public boolean nextFrame = true;
    private boolean drawn = false;
    private int fadeFrame = 0;
    private int fadeFrames = 0;
    private float fadeAmount = 0f;
    private int flashHeartTimes;
    private long flashHeartTime;
    public boolean flashing = false;
    private boolean actuallyFlashing = false;
    public boolean movingHeart = false;
    public double moveHeartX = 0;
    public double moveHeartY = 0;
    public double movingHeartX = 0;
    public double movingHeartY = 0;
    private double moveHeartangle = 0d;
    private double moveHeartdeltaX = 0d;
    private double moveHeartdeltaY = 0d;
    private int moveHeartSpeed = 0;
    public ArrayList<Effect> effects = new ArrayList<>();
    public ArrayList<Effect> effectsToRemove = new ArrayList<>();

    public GamePanel(){
        this.setSize(640, 480);

    }

    public void addEffect(Effect e){
        effects.add(e);
    }
    public void removeEffect(Effect e){
        effects.remove(e);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2d.setFont(new Font("Determination Mono", Font.BOLD, 10));

        if(Game.world != null) {
            if(Game.world.room != null) {
                if(Game.world.battle == null) {
                    if (Game.world.room.highlightedTile != null) {
                        g2d.setColor(Color.red);
                        JGraphics2D.drawGameRect(g2d, Game.world.room.highlightedTile.x, Game.world.room.highlightedTile.y, Game.tilesetSize, Game.tilesetSize);
                    }
                    drawLand(g2d);
                    drawNPCs(g2d);
                    if (Game.world.isEditing) {
                        drawLevelEditorGui();
                    }
                }
            }
        }
        drawGUI(g2d);
        drawWindows(g2d);
        for(Effect e :effects) {
            if (!e.isStopping){
                e.draw(g2d);
            } else {
                effectsToRemove.add(e);
            }
        }
        for(Effect e :effectsToRemove){
            effects.remove(e);
        }
        effectsToRemove = new ArrayList<Effect>();
        if(movingHeart){

            if(movingHeartX < moveHeartX) {
                if(movingHeartX + (moveHeartSpeed * moveHeartdeltaX) > moveHeartX){
                    movingHeartX = moveHeartX;
                } else {
                    movingHeartX += moveHeartSpeed * moveHeartdeltaX;
                }
            } else {
                if(movingHeartX + (moveHeartSpeed * moveHeartdeltaX) < moveHeartX){
                    movingHeartX = moveHeartX;
                } else {
                    movingHeartX -= moveHeartSpeed * moveHeartdeltaX;
                }
            }
            if(movingHeartY < moveHeartY) {
                if(movingHeartY + (moveHeartSpeed * moveHeartdeltaY) > moveHeartY){
                    movingHeartY = moveHeartY;
                } else {
                    movingHeartY -= moveHeartSpeed * moveHeartdeltaY;
                }
            } else {
                if(movingHeartY + (moveHeartSpeed * moveHeartdeltaY) < moveHeartY){
                    movingHeartY = moveHeartY;
                } else {
                    movingHeartY += moveHeartSpeed * moveHeartdeltaY;
                }
            }
            if(movingHeartX == moveHeartX && movingHeartY == moveHeartY){



                movingHeart = false;
            }


            g2d.setColor(Color.black);
            g2d.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            g2d.drawImage(Game.game.sprites.get(12), (int)movingHeartX, (int)movingHeartY, null);
        }
        if(flashing){
            if(flashHeartTimes < 0){
                flashing = false;
            } else {
                if (actuallyFlashing) {
                    if (flashHeartTime + 100 < System.currentTimeMillis()) {
                        actuallyFlashing = false;
                        flashHeartTime = System.currentTimeMillis();
                    }
                    g2d.setColor(Color.black);
                    g2d.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
                    JGraphics2D.drawGameImage(g2d, Game.game.sprites.get(12), Game.world.player.x, Game.world.player.y);
                } else {
                    if (flashHeartTime + 100 < System.currentTimeMillis()) {
                        actuallyFlashing = true;
                        flashHeartTimes--;
                        flashHeartTime = System.currentTimeMillis();
                    }
                }
            }
        }



        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        g2d.drawString(Integer.toString(realTimeFPS), 20, 20);
        drawn = true;
    }

    private void drawWindows(Graphics2D g2d) {
        for(int i = Game.gui.windows.size() - 1; i > -1; i--){
            Game.gui.windows.get(i).draw(g2d);
        }
    }

    private void drawLevelEditorGui() {

    }

    private void drawGUI(Graphics2D g2d) {
        if(Game.gui != null) {
            if (Game.gui.visible) {
                Game.gui.draw(g2d);
            }
        }
    }


    long lastLoopTime = System.nanoTime();
    final int TARGET_FPS = 60;
    final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
    int lastFpsTime;
    int fps;
    int realTimeFPS;
    public void gameLoop()
    {
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;


        while (Game.gameRunning)
        {



            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            realTimeFPS = 1000000000 / (int)updateLength;
            lastLoopTime = now;
            double delta = updateLength / ((double)OPTIMAL_TIME);


            lastFpsTime += updateLength;
            fps++;



            if (lastFpsTime >= 1000000000)
            {

                lastFpsTime = 0;
                fps = 0;
            }
            draw();

            if(Game.world != null) {

                gameUpdate(delta);
            }









            if((lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 > 0){
                try {
                    Thread.sleep(Math.max(0, (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000));
                } catch (InterruptedException e) {


                    e.printStackTrace();
                }
            }
        }

    }

    public static void processCommands(String command){
        String actualCommand = command.split(" ")[0];
        ArrayList<String> args = new ArrayList<>(Arrays.asList(command.replaceFirst(actualCommand, "").replace("^[^ ]* ", "").split(" ")));
        if(args.get(0).equalsIgnoreCase("")){
            args.remove(0);
        }
        if(actualCommand.equalsIgnoreCase("saveRoom")){
            try {
                RoomSaver.saveRoom();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(actualCommand.equalsIgnoreCase("loadRoom") && args.size() == 1){
            try {
                Game.world.room = RoomLoader.loadRoom(new File(args.get(0)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(actualCommand.equalsIgnoreCase("highlightTile") && args.size() == 2){

            if(Game.world.room.roomTiles[Integer.parseInt(args.get(0))][Integer.parseInt(args.get(1))] != null) {
                Game.world.room.highlightedTile = Game.world.room.roomTiles[Integer.parseInt(args.get(0))][Integer.parseInt(args.get(1))];
                Game.world.room.highlightedTileX = Integer.parseInt(args.get(0));
                Game.world.room.highlightedTileY = Integer.parseInt(args.get(1));
            } else {
                Game.world.room.highlightedTile = new Tile(0, "Debug", "", "", false, false, Integer.parseInt(args.get(0)) * Game.tilesetSize, Integer.parseInt(args.get(1)) * Game.tilesetSize);
                Game.world.room.highlightedTile.bounds = new GameRect((Integer.parseInt(args.get(0)) * Game.tilesetSize) + Game.world.offsetX, (Integer.parseInt(args.get(1)) * Game.tilesetSize) + Game.world.offsetX, Game.tilesetSize, Game.tilesetSize);
                Game.world.room.roomTiles[Integer.parseInt(args.get(0))][Integer.parseInt(args.get(1))] = Game.world.room.highlightedTile;
                Game.world.room.highlightedTileX = Integer.parseInt(args.get(0));
                Game.world.room.highlightedTileY = Integer.parseInt(args.get(1));
            }
        }
        if(actualCommand.equalsIgnoreCase("addItem") && args.size() == 1){

            if(Game.world.inventory.inventory.size() < 8){
                for(int i = 0; i < Game.world.itemList.size(); i++) {
                    if (Game.world.itemList.get(i).id == Integer.parseInt(args.get(0))) {
                        Game.world.inventory.addItem(Game.world.itemList.get(i));
                        System.out.println("Added " + Game.world.itemList.get(i).name);
                    }
                }
            }
        }
        if(actualCommand.equalsIgnoreCase("removeItem") && args.size() == 1){


            Game.world.inventory.inventory.remove(Integer.parseInt(args.get(0)));
            Game.world.inventory.inventory.trimToSize();
        }
        if(actualCommand.equalsIgnoreCase("addTile") && args.size() == 1){
            if(Game.world.room.roomTiles[Game.world.room.highlightedTileX][Game.world.room.highlightedTileY] != null) {

                Game.world.spawnTileWithId(Integer.parseInt(args.get(0)), Game.world.room.highlightedTileX, Game.world.room.highlightedTileY);
            } else {
            }
        }
        if(actualCommand.equalsIgnoreCase("editTile") && args.size() == 2){
            if(Game.world.room.roomTiles[Game.world.room.highlightedTileX][Game.world.room.highlightedTileY] != null) {
                try {
                    Game.world.room.roomTiles[Game.world.room.highlightedTileX][Game.world.room.highlightedTileY].writeData(args.get(0), args.get(1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Game.world.room.roomTiles[Game.world.room.highlightedTileX][Game.world.room.highlightedTileY].readData();
            } else {

            }
        }
        if(actualCommand.equalsIgnoreCase("selectTile") && args.size() == 1){
            for(Tile listNpc : Game.world.tileList){
                if(Integer.parseInt(args.get(0)) == listNpc.id){
                    Game.world.selectedTile = listNpc;
                    Game.world.hasSelectedTile = true;
                }
            }

        }
        if(actualCommand.equalsIgnoreCase("unselectTile")){
            Game.world.hasSelectedTile = false;
        }
        if(actualCommand.equalsIgnoreCase("changeMapSize") && args.size() == 2){
            Tile[][] tempRoomTiles = Game.world.room.roomTiles;
            int tempRoomWidth = Game.world.room.width;
            int tempRoomHeight = Game.world.room.height;
            Game.world.room.width = Integer.parseInt(args.get(0));
            Game.world.room.height = Integer.parseInt(args.get(1));
            Game.world.room.roomTiles = new Tile[Integer.parseInt(args.get(0))][Integer.parseInt(args.get(1))];
            if(Game.world.room.width < tempRoomWidth || Game.world.room.height < tempRoomHeight){
                for (int x = 0; x < Game.world.room.width; x++) {
                    for (int y = 0; y < Game.world.room.height; y++) {
                        Game.world.room.roomTiles[x][y] = tempRoomTiles[x][y];
                    }
                }
            } else {
                for (int x = 0; x < tempRoomWidth; x++) {
                    for (int y = 0; y < tempRoomHeight; y++) {
                        Game.world.room.roomTiles[x][y] = tempRoomTiles[x][y];
                    }
                }
            }
            for(int x = 0; x < Game.world.room.width; x++){
                for(int y = 0; y < Game.world.room.height; y++){
                    if(Game.world.room.roomTiles[x][y] == null) {
                        Tile tempTile = (Tile) Game.world.nothingTile.clone();
                        Game.world.room.roomTiles[x][y] = tempTile;
                        Game.world.room.roomTiles[x][y].x = x * Game.tilesetSize;
                        Game.world.room.roomTiles[x][y].y = y * Game.tilesetSize;
                        Game.world.room.roomTiles[x][y].bounds =new GameRect(x * Game.tilesetSize, y * Game.tilesetSize, Game.tilesetSize, Game.tilesetSize);
                        Game.world.room.roomTiles[x][y].properties = new Properties();
                        Game.world.room.roomTiles[x][y].readData();
                    }
                }
            }
        }
    }

    public void consoleLoop(){
        Scanner scan = new Scanner(System.in);
        while (Game.gameRunning)
        {
            String command = scan.nextLine();
            processCommands(command);
        }
    }

    public void gameUpdate(double delta){
        if(Game.world.battle == null) {
            if(Game.world.isStartingBattle){
                Thread battleStartThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startBattleThread(Game.world.tempBattle);
                    }
                });
                battleStartThread.start();
            }
            for (NPC npc : Game.world.npcsInWorld) {
                npc.update(delta);
                for (int roomX = 0; roomX < Game.world.room.roomTiles.length; roomX++) {
                    for (int roomY = 0; roomY < Game.world.room.roomTiles[roomX].length; roomY++) {
                        if (Game.world.room.roomTiles[roomX][roomY] != null) {
                            Game.world.room.roomTiles[roomX][roomY].update();
                            if (Game.world.room.roomTiles[roomX][roomY].bounds.intersects(npc.bounds)) {
                                Game.world.room.roomTiles[roomX][roomY].onEntrance(npc);
                            }
                        }
                    }
                }
            }
            if(Game.world.room != null) {
                if(Game.world.room.roomTiles != null) {
                    for (int roomX = 0; roomX < Game.world.room.roomTiles.length; roomX++) {
                        for (int roomY = 0; roomY < Game.world.room.roomTiles[roomX].length; roomY++) {
                            if (Game.world.room.roomTiles[roomX][roomY] != null) {
                                Game.world.room.roomTiles[roomX][roomY].update();
                            }
                        }
                    }
                }
            }
        } else {
            if(Game.world.battle.enemyFighting || Game.world.battle.enemyPreFighting) {
                Game.world.battle.update();
            }
            if(Game.world.battle != null) {
                if(Game.world.battle.hasBattleStarted) {
                    Game.world.player.battleUpdate();
                    if (Game.world.battle != null) {
                        for (NPC npc : Game.world.battle.enemies) {
                            npc.battleUpdate();
                        }
                    }
                }
            }
        }
        nextFrame = true;
    }

    public void draw(){

        repaint();
    }

    private void drawLand(Graphics2D g2d) {
        for(int roomX = 0; roomX < Game.world.room.roomTiles.length; roomX++) {
            for (int roomY = 0; roomY < Game.world.room.roomTiles[roomX].length; roomY++) {

                JGraphics2D.drawGameImage(g2d, Game.world.tileList.get(0).image, roomX * Game.tilesetSize, roomY * Game.tilesetSize, Game.tilesetSize, Game.tilesetSize);

                if(Game.world.room.roomTiles[roomX][roomY] != null) {
                    JGraphics2D.drawGameImage(g2d, Game.world.room.roomTiles[roomX][roomY].image, roomX * Game.tilesetSize, roomY * Game.tilesetSize, Game.tilesetSize, Game.tilesetSize);

                }
            }
        }
    }

    protected void drawNPCs(Graphics2D g2d){
        for(NPC npc : Game.world.npcsInWorld){

            npc.draw(g2d);
            JGraphics2D.drawGameImage(g2d, npc.image, npc.x, npc.y);

        }
    }

    public void startBattleThread(Battle battle){
        Game.world.isStartingBattle = false;
        Game.gamePanel.flashHeart(3);

        while(Game.gamePanel.flashing){
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {

            }
        }

        Game.gamePanel.moveHeart(30, Game.GAME_HEIGHT - 40, 15);
        while(Game.gamePanel.movingHeart){
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {

            }
        }
        Game.world.battle = battle;
        Game.world.battle.onBattleStart();
    }

    public void fadeIn(int seconds){
        addEffect(new FadeEffect(true, seconds));

    }

    public void fadeOut(int seconds){
        addEffect(new FadeEffect(false, seconds));

    }

    public void flashHeart(int times) {
        flashHeartTimes = times;
        flashHeartTime = System.currentTimeMillis();
        flashing = true;
    }

    public void moveHeart(int x, int y, int speed) {
        moveHeartX = x;
        moveHeartY = y;
        movingHeartX = Game.world.player.x;
        movingHeartY = Game.world.player.y;

        double moveAngleX = 0;
        double moveAngleY = 0;
        if(moveHeartX > movingHeartX){
            moveAngleX = moveHeartX - movingHeartX;
        } else {
            moveAngleX = movingHeartX - moveHeartX;
        }
        if(moveHeartY > movingHeartY){
            moveAngleY = moveHeartY - movingHeartY;
        } else {
            moveAngleY = movingHeartY - moveHeartY;
        }
        moveHeartangle = Math.atan2(-moveAngleY, moveAngleX);
        moveHeartdeltaX = Math.cos(moveHeartangle);
        moveHeartdeltaY = Math.sin(moveHeartangle);

        moveHeartSpeed = speed;
        movingHeart = true;
    }
}
