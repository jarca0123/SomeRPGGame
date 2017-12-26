package tiles;

import game.Game;
import npc.NPC;

import java.util.ArrayList;


public class NPCSpawner extends Tile {
    private int npcToSpawn;
    private int npcY;
    private int npcX;
    private int decision;
    private boolean spawnAtTile = true;
    public NPCSpawner(int id, String name, String imageSrc, String pixelCode, boolean solid, boolean useTileset) {
        super(id, name, imageSrc, pixelCode, solid, useTileset);
    }

    public NPCSpawner(int id, String name, String imageSrc, String pixelCode, boolean solid, boolean useTileset, int x, int y) {
        super(id, name, imageSrc, pixelCode, solid, useTileset, x, y);
    }

    @Override
    public void readData() {
        super.readData();
        if(properties.getProperty("npcToSpawn") != null) npcToSpawn = Integer.parseInt(properties.getProperty("npcToSpawn"));
        if(properties.getProperty("spawnAtTile") != null) spawnAtTile = Boolean.parseBoolean(properties.getProperty("spawnAtTile"));
        if(properties.getProperty("npcX") != null)npcX = Integer.parseInt(properties.getProperty("npcX"));
        if(properties.getProperty("npcY") != null)npcY = Integer.parseInt(properties.getProperty("npcY"));
        if(properties.getProperty("decision") != null){decision = Integer.parseInt(properties.getProperty("decision"));} else { decision = -1;}
    }

    @Override
    public void onEnter() {
        super.onEnter();

        if(decision != -1) {
            if(!Game.world.decisions.get(decision).getBoolean()) {
                if(spawnAtTile){
                    Game.world.spawnNPCWithIdAndCoord(npcToSpawn, x + (Game.tilesetSize / 2), y + (Game.tilesetSize / 2));
                }else {
                    Game.world.spawnNPCWithIdAndCoord(npcToSpawn, npcX, npcY);
                }
            }
        } else {
            if(spawnAtTile){
                Game.world.spawnNPCWithIdAndCoord(npcToSpawn, x + (Game.tilesetSize / 2), y + (Game.tilesetSize / 2));
            }else {
                Game.world.spawnNPCWithIdAndCoord(npcToSpawn, npcX, npcY);
            }

        }
        Game.world.room.roomTiles[roomX][roomY] = Game.world.nothingTile;
    }
}
