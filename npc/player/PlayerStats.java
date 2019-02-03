package npc.player;

import game.Game;
import item.AttackItem;
import item.DefenseItem;
import item.Item;

import java.util.Base64;


public class PlayerStats {
    public int AT;
    public int DF;
    public int EXP;
    public AttackItem equipedAT;
    public DefenseItem equipedDF;


    public PlayerStats(int AT, int DF, int EXP, Item equipedAT, Item equipedDF) {
        this.AT = AT;
        this.DF = DF;
        this.EXP = EXP;
        this.equipedAT = (AttackItem) equipedAT;
        if(this.equipedAT == null){
            this.equipedAT = (AttackItem) Game.world.itemList.get(0);
        }
        this.equipedDF = (DefenseItem) equipedDF;
        if(this.equipedDF == null){
            this.equipedDF = (DefenseItem) Game.world.itemList.get(1);
        }
    }

    public static PlayerStats parseString(String roomDatum) {
        Base64.Decoder decoder = Base64.getDecoder();
        String decodedString = new String(decoder.decode(roomDatum));
        String[] data = decodedString.split(";");
        return new PlayerStats(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]),  Game.world.getItemById(Integer.parseInt(data[3])), Game.world.getItemById(Integer.parseInt(data[4])));
    }
}
