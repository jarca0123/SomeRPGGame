package file;

import game.Game;
import item.Inventory;
import item.Item;
import tiles.SaveTile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class FileSaver {

    public static void saveFile(String fileName, SaveTile tile) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(Game.world.roomId + ";");

        sb.append(Game.world.player.x + ";");
        sb.append(Game.world.player.y + ";");
        StringBuilder inventoryBuilder = new StringBuilder();
        for(Item i : Game.world.inventory.inventory){
            inventoryBuilder.append(i.id + ";");
        }

            inventoryBuilder.append(";");

        Base64.Encoder encoder = Base64.getEncoder();
        if(!inventoryBuilder.toString().equalsIgnoreCase("")) sb.append(new String(encoder.encode(inventoryBuilder.toString().getBytes(StandardCharsets.UTF_8))));
        sb.append(";");
        StringBuilder statsBuilder = new StringBuilder();
        statsBuilder.append(Game.world.player.stats.AT + ";");
        statsBuilder.append(Game.world.player.stats.DF + ";");
        statsBuilder.append(Game.world.player.stats.EXP + ";");
        if(Game.world.player.stats.equipedAT != null) {
            statsBuilder.append(Game.world.player.stats.equipedAT.id + ";");
        } else {
            statsBuilder.append("0;");
        }
        if(Game.world.player.stats.equipedDF != null) {
            statsBuilder.append(Game.world.player.stats.equipedDF.id + ";");
        } else {
            statsBuilder.append("0;");
        }
        sb.append(new String(encoder.encode(statsBuilder.toString().getBytes(StandardCharsets.UTF_8))) + ";");
        StringBuilder decisionsBuilder = new StringBuilder();
        for(int i = 0; i < Game.world.decisionCount; i++){
            decisionsBuilder.append(Game.world.decisions.get(i).getString() + ";");
        }
        sb.append(new String(encoder.encode(decisionsBuilder.toString().getBytes(StandardCharsets.UTF_8))));
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        fileOutputStream.write(sb.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
