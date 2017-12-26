package gui;

import game.Game;
import gui.window.InventoryWindow;
import gui.window.SaveDialog;

import java.awt.*;


public class GUIGame extends GUI {
    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        for(gui.window.Window w : Game.gui.windows){

            if(w instanceof SaveDialog){
                if(left) {
                    ((SaveDialog) w).selectionMenu.selectedId--;
                    left = false;
                }
                if(right){
                    ((SaveDialog) w).selectionMenu.selectedId++;
                    right = false;
                }
                if(2 <((SaveDialog) w).selectionMenu.selectedId){
                    ((SaveDialog) w).selectionMenu.selectedId = 1;
                } else if (((SaveDialog) w).selectionMenu.selectedId <= 0){
                    ((SaveDialog) w).selectionMenu.selectedId = 2;
                }
            } else if(w instanceof InventoryWindow){
                int inventorySize = Game.world.inventory.inventory.size();
                if(!((InventoryWindow) w).hasSelected) {
                    if (up) {
                        ((InventoryWindow) w).selectionMenu.selectedId--;
                        System.out.println(((InventoryWindow) w).selectionMenu.selectedId);
                        up = false;
                    }
                    if (down) {
                        ((InventoryWindow) w).selectionMenu.selectedId++;
                        System.out.println(((InventoryWindow) w).selectionMenu.selectedId);
                        down = false;
                    }
                    if (Game.world.inventory.inventory.size() < ((InventoryWindow) w).selectionMenu.selectedId) {
                        ((InventoryWindow) w).selectionMenu.selectedId = 1;
                    } else if (((InventoryWindow) w).selectionMenu.selectedId < 1) {
                        ((InventoryWindow) w).selectionMenu.selectedId = inventorySize;
                    }
                } else {
                    if (left) {
                        ((InventoryWindow) w).selectMenu.selectedId--;

                        left = false;
                    }
                    if (right) {
                        ((InventoryWindow) w).selectMenu.selectedId++;

                        right = false;
                    }
                    if(2 <((InventoryWindow) w).selectMenu.selectedId){
                        ((InventoryWindow) w).selectMenu.selectedId = 1;
                    } else if (((InventoryWindow) w).selectMenu.selectedId <= 0){
                        ((InventoryWindow) w).selectMenu.selectedId = 2;
                    }
                }
            }
        }
    }
}
