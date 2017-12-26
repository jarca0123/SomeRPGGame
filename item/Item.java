package item;

import game.Game;


public class Item {
    public int id;
    public String name;
    public String description = "This doesn't have a description...";
    private boolean consumable = false;

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void onConsume(){
        if(consumable){
            Game.world.inventory.inventory.remove(this);
        } else {
            if(this instanceof DefenseItem){
                Game.world.player.stats.equipedDF = (DefenseItem) this;
            }
            if(this instanceof AttackItem){
                Game.world.player.stats.equipedAT = (AttackItem) this;
            }
        }
    }
}
