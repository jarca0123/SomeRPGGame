package gui.window;

import file.FileSaver;
import game.Game;
import gui.ImageComponent;
import gui.InteractionText;
import gui.SelectionMenu;
import tiles.SaveTile;

import java.awt.*;
import java.io.IOException;

public class SaveDialog extends Window {
    public SelectionMenu selectionMenu;
    public SaveTile tile;
    public SaveDialog(int id, boolean actionBarEnabled) {
        super(id, actionBarEnabled);
    }
    public int selectedSelectionId = 1;

    public SaveDialog(int id, int x, int y, int width, int height, boolean actionBarEnabled, SaveTile tile) {
        super(id, x, y, width, height, actionBarEnabled);
        this.tile = tile;
        selectionMenu =  new SelectionMenu(-313371, 0, height - 50, width, 50, null, null, null, null, 2, 1);
        addComponent(selectionMenu);
        selectionMenu.addSelection(new InteractionText(-313371, 0.1, 0, 0.4, 0.5, null, null, null, null, 0, 0, 0, 0, "SAVE", false));
        selectionMenu.addSelection(new InteractionText(-313372, 0.6, 0, 0.5, 0.5, null, null, null, null, 0, 0, 0, 0, "RETURN", false));
        addComponent(new ImageComponent(-313379, 20, 10, Game.sprites.get(12).getWidth(), Game.sprites.get(12).getHeight(), Game.sprites.get(12), null, false));
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        getComponentById(-313379).actualX = ((SelectionMenu)getComponentById(-313371)).selections.get(selectedSelectionId - 1).actualX - 40;
        getComponentById(-313379).actualY = ((SelectionMenu)getComponentById(-313371)).selections.get(selectedSelectionId - 1).actualY - 8;
    }

    @Override
    public void onActionKey() {
        super.onActionKey();
        if(selectionMenu.selectedId == 1){

            try {
                FileSaver.saveFile("save.dat");
            } catch (IOException e) {
                e.printStackTrace();
            }
            onWindowClose();
        } else if(selectionMenu.selectedId == 2){
            onWindowClose();
        }
    }
}
