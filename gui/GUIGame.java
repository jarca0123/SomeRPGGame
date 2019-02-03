package gui;

import game.Game;
import gui.window.InventoryWindow;
import gui.window.SaveDialog;

import java.awt.*;
import java.awt.event.KeyEvent;


public class GUIGame extends GUI {
    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);

    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        for(gui.window.Window w : Game.gui.windows){

            if(w instanceof SaveDialog){
                if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                    ((SaveDialog) w).selectionMenu.selectedId--;
                }
                if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    ((SaveDialog) w).selectionMenu.selectedId++;
                }
                if(2 <((SaveDialog) w).selectionMenu.selectedId){
                    ((SaveDialog) w).selectionMenu.selectedId = 1;
                } else if (((SaveDialog) w).selectionMenu.selectedId <= 0){
                    ((SaveDialog) w).selectionMenu.selectedId = 2;
                }
            } else if(w instanceof InventoryWindow){
                int inventorySize = Game.world.inventory.inventory.size();
                if(!((InventoryWindow) w).hasSelected) {
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        ((InventoryWindow) w).selectionMenu.selectedId--;
                        System.out.println(((InventoryWindow) w).selectionMenu.selectedId);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        ((InventoryWindow) w).selectionMenu.selectedId++;
                        System.out.println(((InventoryWindow) w).selectionMenu.selectedId);
                    }
                    if (Game.world.inventory.inventory.size() < ((InventoryWindow) w).selectionMenu.selectedId) {
                        ((InventoryWindow) w).selectionMenu.selectedId = 1;

                    } else if (((InventoryWindow) w).selectionMenu.selectedId < 1) {
                        ((InventoryWindow) w).selectionMenu.selectedId = inventorySize;

                    }
                } else {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        ((InventoryWindow) w).selectMenu.selectedId--;

                    }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        ((InventoryWindow) w).selectMenu.selectedId++;

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
