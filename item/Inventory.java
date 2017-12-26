package item;

import game.Game;
import gui.GUIComponent;

import java.util.ArrayList;
import java.util.Base64;


public class Inventory {
    public ArrayList<Item> inventory = new ArrayList<>();
    public Inventory(){

    }

    public Item getItemFromInventory(int index){
        if(inventory.size() != 0) {
            return inventory.get(index);
        } else {
            return new Item(-1, "MissingNo.");
        }
    }

    public void addItem(Item item){

        inventory.add(item);
    }

    public static Inventory parseString(String roomDatum) {
        Inventory tempInv = new Inventory();
        Base64.Decoder decoder = Base64.getDecoder();
        String decodedString = new String(decoder.decode(roomDatum));
        String[] data = decodedString.split(";");
        for(int i = 0; i < data.length && i < 8; i++){
            tempInv.inventory.add(Game.world.getItemById(Integer.parseInt(data[i])));
        }
        return tempInv;
    }


}
