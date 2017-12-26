package gui.window;

import game.Game;
import gui.ImageComponent;
import gui.InteractionText;
import gui.SelectionMenu;
import item.AttackItem;
import item.DefenseItem;
import item.Item;

import java.awt.*;


public class InventoryWindow extends Window{
    public SelectionMenu selectionMenu;
    public SelectionMenu selectMenu;
    public boolean hasSelected = false;
    public Item selectedItem = null;
    public InventoryWindow(int id, boolean actionBarEnabled) {
        super(id, actionBarEnabled);
    }

    public InventoryWindow(int id, int x, int y, int width, int height, boolean actionBarEnabled) {
        super(id, x, y, width, height, actionBarEnabled);
        selectionMenu = new SelectionMenu(-1731711, 0, 0, width, height - 100, null, null, null, null, 0, 0, 0, 0, 8, 1);
        selectMenu = new SelectionMenu(-1731712, 40, height - 75, 0.33d, height - 50, null, null, null, null, 0, 0, 0, 0, 2, 1);
        addComponent(selectionMenu);
        for(int i = 0; i < 8 && i < Game.world.inventory.inventory.size(); i++) {
            selectionMenu.addSelection(new InteractionText(-(13371 + i), 40, 10 + (i * 40), width, 40, null, null, null, null, 0, 0, 0, 0, Game.world.inventory.getItemFromInventory(i).name, false));
        }
        addComponent(selectMenu);
        selectMenu.addSelection(new InteractionText(-313371, 0, 0, 50, 50, null, null, null, null, 0, 0, 0, 0, "Equip", false));
        selectMenu.addSelection(new InteractionText(-313372, 30 + ((double).33 * (double)width), 0, 50, 50, null, null, null, null, 0, 0, 0, 0, "Info", false));
        addComponent(new ImageComponent(-313379, 10, 50, Game.sprites.get(12).getWidth(), Game.sprites.get(12).getHeight(), Game.sprites.get(12), null, false));
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        getComponentById(-313379).actualX = hasSelected? selectMenu.selections.get(selectMenu.selectedId - 1).actualX - 30 : 10;
        getComponentById(-313379).actualY = hasSelected? selectMenu.selections.get(selectMenu.selectedId - 1).actualY - 10 :selectionMenu.selections.get(selectionMenu.selectedId - 1).actualY - 10;
    }

    @Override
    public void onActionKey() {
        super.onActionKey();
        if(hasSelected) {
            if(selectMenu.selectedId == 1) {

                Item itemToBeEquiped = Game.world.inventory.getItemFromInventory(selectionMenu.selectedId - 1);
                boolean usedItem = false;
                if (itemToBeEquiped instanceof AttackItem || itemToBeEquiped instanceof DefenseItem) {
                    Item itemToBeSwapped = null;
                    if (itemToBeEquiped instanceof AttackItem) {
                        itemToBeSwapped = Game.world.player.stats.equipedAT;
                    } else if (itemToBeEquiped instanceof DefenseItem) {
                        itemToBeSwapped = Game.world.player.stats.equipedDF;
                    }
                    Game.world.inventory.addItem(itemToBeSwapped);
                    Game.world.inventory.inventory.remove(itemToBeEquiped);
                    if (itemToBeEquiped instanceof AttackItem) {
                        Game.world.player.stats.equipedAT = (AttackItem) itemToBeEquiped;
                    } else if (itemToBeEquiped instanceof DefenseItem) {
                        Game.world.player.stats.equipedDF = (DefenseItem) itemToBeEquiped;
                    }
                } else {
                    itemToBeEquiped.onConsume();
                    usedItem = true;
                }
                onWindowClose();
                Game.world.player.interactionInstance = new GameInteraction(-20000 - id, 0, Game.GAME_HEIGHT - 120, Game.GAME_WIDTH, 120, false, usedItem ? "Consumed " + itemToBeEquiped.name + "." : "Equiped " + itemToBeEquiped.name + ".");
                Game.world.startInteraction(Game.world.player.interactionInstance);
            } else if(selectMenu.selectedId == 2){
                onWindowClose();
                Game.world.player.interactionInstance = new GameInteraction(-20000 - id, 0, Game.GAME_HEIGHT - 120, Game.GAME_WIDTH, 120, false, Game.world.inventory.getItemFromInventory(selectionMenu.selectedId - 1));
                Game.world.startInteraction(Game.world.player.interactionInstance);
            }
        } else {
            hasSelected = true;
        }

    }
}
