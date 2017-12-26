package file;

import game.Game;
import item.Inventory;
import npc.player.PlayerStats;
import world.Decision;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;


public class FileLoader {


    private static ArrayList<Decision> parseStringToDecisions(String s, int decisions){
        ArrayList<Decision> tempDecisions = new ArrayList<>();
        Base64.Decoder decoder = Base64.getDecoder();
        String decodedString = new String(decoder.decode(s));
        String[] data = decodedString.split(";");

        for(int i = 0; i < data.length; i++){
            tempDecisions.add(new Decision(data[i]));
        }
        for(int i = data.length; i < decisions; i++){
            tempDecisions.add(new Decision(""));
        }
        return tempDecisions;
    }
    public static void loadFile() throws IOException{
        String theSave = new String(Files.readAllBytes(new File("save.dat").toPath()));
        String[] roomData = theSave.split(";");
        Game.world.roomId = Integer.parseInt(roomData[0]);

        Game.world.playerX = Integer.parseInt(roomData[1]);
        Game.world.playerY = Integer.parseInt(roomData[2]);
        Game.world.inventory = Inventory.parseString(roomData[3]);
        Game.world.playerStats = PlayerStats.parseString(roomData[4]);
        Game.world.decisions = parseStringToDecisions(roomData[5], Game.world.decisionCount);
    }
}
