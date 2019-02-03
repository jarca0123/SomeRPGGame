package gui;

import file.RoomSaver;
import game.Game;
import geometry.GameRect;
import graphics.JGraphics2D;
import gui.window.*;
import gui.window.Dialog;
import gui.window.Window;
import tiles.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;


public class GUILevelEditor extends GUI {
    int entries;
    int entryPage = 1;
    int entryPages;
    int additionalTiles;
    int tilesInToolbar = 8;
    int originalOffsetX = 0;
    int originalOffsetY = 0;
    public HashSet<Tile> selectedTiles = new HashSet<>();
    public int toolId = 1;
    public int lastToolId;
    public int originalDragX;
    public int originalDragY;
    private ArrayList<GUIComponent> mainMenuComponents = new ArrayList<>();
    private ArrayList<GUIComponent> selectMenuComponents = new ArrayList<>();
    private ArrayList<GUIComponent> optionsMenuComponents = new ArrayList<>();

    public GUILevelEditor(){
    }

    @Override
    public void onStart() {
        super.onStart();
        entries = Game.world.tileList.size();
        if(entries % tilesInToolbar == 0){
            entryPages = entries / tilesInToolbar;
            additionalTiles = 0;
        } else {
            entryPages = (entries / tilesInToolbar) + 1;
            additionalTiles = entries % tilesInToolbar;
        }

        for(int i = 0; i < Game.world.tileList.size(); i++){
            GUIComponent tempComponent = new ImageNText(i, 64 + ((i % tilesInToolbar) * 64), Game.GAME_HEIGHT - 64, 64, 64, Game.world.tileList.get(i).image, Game.world.tileList.get(i).name).setVisible(false).setEnabled(false);
            if(entryPages == 1) {
                if(entries == tilesInToolbar){
                    if (i < tilesInToolbar) {
                        tempComponent.setVisible(true).setEnabled(true);
                    }
                } else {
                    if (i < additionalTiles) {
                        tempComponent.setVisible(true).setEnabled(true);
                    }
                }
            } else {
                if (i < tilesInToolbar) {
                    tempComponent.setVisible(true).setEnabled(true);
                }
            }
            addComponent(tempComponent);
        }
        /*if(entryPages == 1) {
            if(entries == tilesInToolbar){
                for (int j = 0; j < tilesInToolbar; j++) {
                    this.guiComponents.get(j).enabled = true;
                    this.guiComponents.get(j).visible = true;
                }
            } else {
                for (int j = 0; j < additionalTiles; j++) {
                    this.guiComponents.get(j).enabled = true;
                    this.guiComponents.get(j).visible = true;
                }
            }
        } else {
            for (int j = 0; j < tilesInToolbar; j++) {
                this.guiComponents.get(j).enabled = true;
                this.guiComponents.get(j).visible = true;
            }
        }*/
        addComponent(new SimpleButton(-1, 0, Game.GAME_HEIGHT - 64, 64, 64, "<<"));
        addComponent(new SimpleButton(-2, Game.GAME_WIDTH - 64, Game.GAME_HEIGHT - 64, 64, 64, ">>"));

        mainMenuComponents.add(new SimpleButton(-3, 0, 32, 64, 64, "Place"));
        mainMenuComponents.add(new SimpleButton(-4, 0, 96, 64, 64, "Edit Data"));
        mainMenuComponents.add(new SimpleButton(-5, 0, 160, 64, 64, "Select"));
        mainMenuComponents.add(new SimpleButton(-6, 0, 224, 64, 64, "..."));
        mainMenuComponents.add(new SimpleButton(-7, 0, 288, 64, 64, "Options"));
        addComponent(mainMenuComponents);
        selectMenuComponents.add(new SimpleButton(-13, 0, 32, 64, 64, "Add"));
        selectMenuComponents.add(new SimpleButton(-14, 0, 96, 64, 64, "Remove"));
        selectMenuComponents.add(new SimpleButton(-15, 0, 160, 64, 64, "Edit Data"));
        selectMenuComponents.add(new SimpleButton(-16, 0, 224, 64, 64, "..."));
        selectMenuComponents.add(new SimpleButton(-17, 0, 288, 64, 64, "..."));
        optionsMenuComponents.add(new SimpleButton(-23, 0, 32, 64, 64, "Save room"));
        optionsMenuComponents.add(new SimpleButton(-24, 0, 96, 64, 64, "Load room"));
        optionsMenuComponents.add(new SimpleButton(-25, 0, 160, 64, 64, "Set room size"));
        optionsMenuComponents.add(new SimpleButton(-26, 0, 224, 64, 64, "..."));
        optionsMenuComponents.add(new SimpleButton(-27, 0, 288, 64, 64, "..."));
    }

    @Override
    public void actionPerfomed(GUIComponent guiComponent) {
        super.actionPerfomed(guiComponent);
        if(guiComponent.id > -1)
        Game.world.selectedTile = Game.world.tileList.get(guiComponent.id);
        if(guiComponent.id == -1){
            if(entryPage > 1) {
                if(entryPage == entryPages) {
                    for (int j = 0; j < additionalTiles; j++) {
                        this.guiComponents.get(j + (tilesInToolbar * (entryPage - 1))).enabled = false;
                        this.guiComponents.get(j + (tilesInToolbar * (entryPage - 1))).visible = false;
                    }
                } else {
                    for (int j = 0; j < tilesInToolbar; j++) {
                        this.guiComponents.get(j + (tilesInToolbar * (entryPage - 1))).enabled = false;
                        this.guiComponents.get(j + (tilesInToolbar * (entryPage - 1))).visible = false;
                    }
                }
                entryPage--;
                for (int j = 0; j < tilesInToolbar; j++) {
                    this.guiComponents.get(j + (tilesInToolbar * (entryPage - 1))).enabled = true;
                    this.guiComponents.get(j + (tilesInToolbar * (entryPage - 1))).visible = true;
                }
            }
        }
        if(guiComponent.id == -2){
            if(entryPage < entryPages) {
                for (int j = 0; j < tilesInToolbar; j++) {
                    this.guiComponents.get(j + (tilesInToolbar * (entryPage - 1))).enabled = false;
                    this.guiComponents.get(j + (tilesInToolbar * (entryPage - 1))).visible = false;
                }
                entryPage++;
                if(entryPage == entryPages) {
                    for (int j = 0; j < additionalTiles; j++) {
                        this.guiComponents.get(j + (tilesInToolbar * (entryPage - 1))).enabled = true;
                        this.guiComponents.get(j + (tilesInToolbar * (entryPage - 1))).visible = true;
                    }
                } else {
                    for (int j = 0; j < tilesInToolbar; j++) {
                        this.guiComponents.get(j + (tilesInToolbar * (entryPage - 1))).enabled = true;
                        this.guiComponents.get(j + (tilesInToolbar * (entryPage - 1))).visible = true;
                    }
                }
            }
        }
        if(guiComponent.id == -3){
            changeTool(1);
        }
        if(guiComponent.id == -4){
            changeTool(2);
        }
        if(guiComponent.id == -5){
            replaceComponents(-3, -7, selectMenuComponents);
            changeTool(3);
        }
        if(guiComponent.id == -6){
            changeTool(4);
        }
        if(guiComponent.id == -7){

            replaceComponents(-3, -7, optionsMenuComponents);
        }
        if(guiComponent.id == -13){
            for (int roomX = 0; roomX < Game.world.room.roomTiles.length; roomX++) {
                for (int roomY = 0; roomY < Game.world.room.roomTiles[roomX].length; roomY++) {
                    for(Tile t : selectedTiles) {
                        if (Game.world.room.roomTiles[roomX][roomY] == t){
                            Tile tempTile = (Tile) Game.world.selectedTile.clone();
                            Game.world.room.roomTiles[roomX][roomY] = tempTile;
                            Game.world.room.roomTiles[roomX][roomY].x = roomX * Game.tilesetSize;
                            Game.world.room.roomTiles[roomX][roomY].y = roomY * Game.tilesetSize;
                            Game.world.room.roomTiles[roomX][roomY].bounds = new GameRect(roomX * Game.tilesetSize, roomY * Game.tilesetSize, Game.tilesetSize, Game.tilesetSize);
                            Game.world.room.roomTiles[roomX][roomY].properties = new Properties();
                            Game.world.room.roomTiles[roomX][roomY].readData();
                        }
                    }
                }
            }
            selectedTiles = new HashSet<Tile>();
            changeTool(lastToolId);
            replaceComponents(-13, -17, mainMenuComponents);
        }
        if(guiComponent.id == -14){
            for (int roomX = 0; roomX < Game.world.room.roomTiles.length; roomX++) {
                for (int roomY = 0; roomY < Game.world.room.roomTiles[roomX].length; roomY++) {
                    for(Tile t : selectedTiles) {
                        if (Game.world.room.roomTiles[roomX][roomY] == t){
                            Tile tempTile = (Tile) Game.world.nothingTile.clone();
                            Game.world.room.roomTiles[roomX][roomY] = tempTile;
                            Game.world.room.roomTiles[roomX][roomY].x = roomX * Game.tilesetSize;
                            Game.world.room.roomTiles[roomX][roomY].y = roomY * Game.tilesetSize;
                            Game.world.room.roomTiles[roomX][roomY].bounds = new GameRect(roomX * Game.tilesetSize, roomY * Game.tilesetSize, Game.tilesetSize, Game.tilesetSize);
                            Game.world.room.roomTiles[roomX][roomY].properties = new Properties();
                            Game.world.room.roomTiles[roomX][roomY].readData();
                        }
                    }
                }
            }
            selectedTiles = new HashSet<Tile>();
            changeTool(lastToolId);
            replaceComponents(-13, -17, mainMenuComponents);
        }

        if(guiComponent.id == -15){
            addGUIWindow(new DataEdit(Game.gui.windows.size() + 1, (Game.GAME_WIDTH / 2) - 50, (Game.GAME_HEIGHT / 2) - 50, 50, 100, selectedTiles));
        }
        if(guiComponent.id == -23){
            try {
                RoomSaver.saveRoom();
            } catch (IOException e) {
                e.printStackTrace();
            }
            replaceComponents(-23, -27, mainMenuComponents);
        }
        if(guiComponent.id == -24){
            addGUIWindow(new Dialog(0, Game.GAME_WIDTH / 2, Game.GAME_HEIGHT / 2, 200, 100, "Room name: ", true, new WindowCallback(){
                @Override
                public void onEvent(String... args) {
                    Game.game.start(true, args[0]);
                    Game.game.setGUI(new GUILevelEditor());

                }
            }));
        }
    }

    public void closeDataEditWindow(){
        selectedTiles = new HashSet<Tile>();
        changeTool(lastToolId);
        replaceComponents(-13, -17, mainMenuComponents);
    }


    public void changeTool(int id){
        lastToolId = toolId;
        toolId = id;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(MouseEvent e) {
        super.onClick(e);
        for (int roomX = 0; roomX < Game.world.room.roomTiles.length; roomX++) {
            for (int roomY = 0; roomY < Game.world.room.roomTiles[roomX].length; roomY++) {
                if (Game.world.room.roomTiles[roomX][roomY].id != 0) {
                    if (Game.world.room.roomTiles[roomX][roomY].bounds.intersects(new Rectangle(mouseX - Game.world.offsetX, mouseY - Game.world.offsetY, 1, 1))) {
                        if (!SwingUtilities.isRightMouseButton(e)) {
                            if (((GUILevelEditor) Game.gui).toolId == 1) {

                                try {
                                    int subImageCombined = (Game.world.room.roomTiles[roomX][roomY].subImageY + (Game.world.room.roomTiles[roomX][roomY].subImageX * 3)) + 1;
                                    if (subImageCombined > 8) {
                                        subImageCombined = 0;
                                    }
                                    int tempSubImageX = subImageCombined / 3;
                                    int tempSubImageY = subImageCombined % 3;
                                    Game.world.room.roomTiles[roomX][roomY].writeData("subImage", tempSubImageX + ";" + tempSubImageY);
                                    Game.world.room.roomTiles[roomX][roomY].readData();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            } else if (((GUILevelEditor) Game.gui).toolId == 2) {
                                boolean addWindow = true;
                                for(Window w : windows){
                                    if(w instanceof DataEdit){
                                        addWindow = false;
                                    }
                                }
                                if(addWindow)gui.window.Window.addGUIWindow(new DataEdit(Game.gui.windows.size() + 1, (Game.GAME_WIDTH / 2) - 50, (Game.GAME_HEIGHT / 2) - 50, 50, 100, Game.world.room.roomTiles[roomX][roomY]));
                            }
                        } else {
                            if (((GUILevelEditor) Game.gui).toolId == 1) {
                                if (Game.world.room.roomTiles[roomX][roomY].bounds.intersects(new Rectangle(mouseX - Game.world.offsetX, mouseY - Game.world.offsetY, 1, 1))) {
                                    Tile tempTile = (Tile) Game.world.nothingTile.clone();
                                    Game.world.room.roomTiles[roomX][roomY] = tempTile;
                                    Game.world.room.roomTiles[roomX][roomY].x = roomX * Game.tilesetSize;
                                    Game.world.room.roomTiles[roomX][roomY].y = roomY * Game.tilesetSize;
                                    Game.world.room.roomTiles[roomX][roomY].bounds = new GameRect(roomX * Game.tilesetSize, roomY * Game.tilesetSize, Game.tilesetSize, Game.tilesetSize);
                                    Game.world.room.roomTiles[roomX][roomY].properties = new Properties();
                                    Game.world.room.roomTiles[roomX][roomY].readData();
                                }
                            }
                        }
                    }
                } else {
                    if (((GUILevelEditor) Game.gui).toolId == 1) {
                        if (Game.world.room.roomTiles[roomX][roomY].bounds.intersects(new GameRect(mouseX - Game.world.offsetX, mouseY - Game.world.offsetY, 1, 1))) {
                            if (!SwingUtilities.isRightMouseButton(e)) {
                                Tile tempTile = (Tile) Game.world.selectedTile.clone();
                                Game.world.room.roomTiles[roomX][roomY] = tempTile;
                                Game.world.room.roomTiles[roomX][roomY].x = roomX * Game.tilesetSize;
                                Game.world.room.roomTiles[roomX][roomY].y = roomY * Game.tilesetSize;
                                Game.world.room.roomTiles[roomX][roomY].bounds = new GameRect(roomX * Game.tilesetSize, roomY * Game.tilesetSize, Game.tilesetSize, Game.tilesetSize);
                                Game.world.room.roomTiles[roomX][roomY].properties = new Properties();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        dragBounds = null;
        dragMouseX = e.getX();
        dragMouseY = e.getY();
        if (!dragging && toolId == 3) {
            originalDragX = e.getX() - Game.world.offsetX;
            originalDragY = e.getY() - Game.world.offsetY;
            originalOffsetX = Game.world.offsetX;
            originalOffsetY = Game.world.offsetY;
            dragMouseX = e.getX();
            dragMouseY = e.getY();
                if (dragBounds == null) {
                    dragging = true;
                } else if (dragBounds != null) {
                    if (dragBounds.intersects(new Rectangle(mouseX, mouseY, 1, 1)) && !clickedOnSomething) {
                        selectedTiles = null;
                        dragging = true;
                    }
                }
        }

        if (selectedComponent == null && selectedWindow == null && dragging) {
            dragBounds = generateRectangleThatIDontNeedToRewriteEverytime();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
    }

    @Override
    public void draw(Graphics2D g2d) {
        if(keyStrokes.contains(KeyEvent.VK_UP)){
            Game.world.offsetY += Game.gamePanel.speed;
            if(dragging) dragBounds = generateRectangleThatIDontNeedToRewriteEverytime();
        }
        if(keyStrokes.contains(KeyEvent.VK_DOWN)){
            Game.world.offsetY -= Game.gamePanel.speed;
            if(dragging) dragBounds = generateRectangleThatIDontNeedToRewriteEverytime();
        }
        if(keyStrokes.contains(KeyEvent.VK_LEFT)){
            Game.world.offsetX += Game.gamePanel.speed;
            if(dragging) dragBounds = generateRectangleThatIDontNeedToRewriteEverytime();
        }
        if(keyStrokes.contains(KeyEvent.VK_RIGHT)){
            Game.world.offsetX -= Game.gamePanel.speed;
            if(dragging) dragBounds = generateRectangleThatIDontNeedToRewriteEverytime();
        }
        g2d.setColor(Color.decode("#333333"));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        if(selectedTiles != null) {
            for (Tile t : selectedTiles) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                JGraphics2D.fillGameRect(g2d, t.x, t.y, Game.tilesetSize, Game.tilesetSize);
            }
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        if(dragBounds != null) g2d.fillRect((int)dragBounds.getX(), (int)dragBounds.getY(), (int)dragBounds.getWidth(), (int)dragBounds.getHeight());
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.setColor(Color.decode("#333333"));
        g2d.drawImage(Game.world.selectedTile.image, ((mouseX - Game.world.offsetX) / Game.tilesetSize) * Game.tilesetSize + Game.world.offsetX, ((mouseY- Game.world.offsetY) / Game.tilesetSize) * Game.tilesetSize + Game.world.offsetY, Game.tilesetSize, Game.tilesetSize,null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2d.fillRect(0, Game.GAME_HEIGHT - 64, Game.GAME_WIDTH, 64);
        g2d.fillRect(0, 32, 64, 320);
        super.draw(g2d);
    }


   public Rectangle generateRectangleThatIDontNeedToRewriteEverytime() {

           return new Rectangle(mouseX - (Math.max(0, mouseX - dragMouseX) + Math.min(0, mouseX - dragMouseX)), mouseY - (Math.max(0, mouseY - dragMouseY) + Math.min(0, mouseY - dragMouseY)), Math.max(0, mouseX - dragMouseX) + Math.min(0, mouseX - dragMouseX), Math.max(0, mouseY - dragMouseY) + Math.min(0, mouseY - dragMouseY));

   }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        dragging = false;
        if(dragBounds !=null) {
            if(dragBounds.width < 0){
                dragBounds.width = Math.abs(dragBounds.width);
                dragBounds.x -= dragBounds.width;
            }
            if(dragBounds.height < 0){
                dragBounds.height = Math.abs(dragBounds.height);
                dragBounds.y -= dragBounds.height;
            }

            for (int roomX = 0; roomX < Game.world.room.roomTiles.length; roomX++) {
                for (int roomY = 0; roomY < Game.world.room.roomTiles[roomX].length; roomY++) {
                    if (Game.world.room.roomTiles[roomX][roomY].bounds.intersects(dragBounds)) {
                        selectedTiles.add(Game.world.room.roomTiles[roomX][roomY]);
                    } else if (!(Game.world.room.roomTiles[roomX][roomY].bounds.intersects(dragBounds)) && selectedTiles.contains(Game.world.room.roomTiles[roomX][roomY])) {
                        selectedTiles.remove(Game.world.room.roomTiles[roomX][roomY]);
                    }
                }
            }
        }
        dragBounds = null;
    }
}
