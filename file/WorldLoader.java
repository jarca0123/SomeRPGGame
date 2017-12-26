package file;

import game.Game;
import item.Inventory;
import npc.SomeBoss;
import npc.player.Player;
import npc.player.PlayerStats;
import room.Room;
import tiles.NPCSpawner;
import tiles.RoomEntrance;
import tiles.Tile;
import world.Decision;
import world.World;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;


public class WorldLoader {

    public ArrayList<Decision> parseStringToDecisions(String s, int decisions){
        Inventory tempInv = new Inventory();
        Base64.Decoder decoder = Base64.getDecoder();
        String decodedString = new String(decoder.decode(s));
        String[] data = decodedString.split(";");

        for(int i = 0; i < decisions; i++){

        }
        return null;
    }
    public static void loadWorld(boolean isEditing) throws IOException {
        Game.world = new World(isEditing);
        World w = Game.world;



        Game.game.initializeTiles();
        Game.game.initializeNPCs();
        Game.game.initializeItEMs();
        String theWorld = null;
        theWorld = Game.convertStreamToString(Game.game.getClass().getClassLoader().getResourceAsStream("world.dat"));
        String[] roomData = theWorld.split(";");
        w.mapCount = Integer.parseInt(roomData[0]);
        w.decisionCount = Integer.parseInt(roomData[1]);
        for(int i = 0; i < w.decisionCount; i++)
        {
            w.decisions.add(new Decision(""));

        }
        for(int i = 0; i < w.mapCount; i++) {
            w.addRoom(new Room(i, roomData[2 + i]));
        }
    }
}
