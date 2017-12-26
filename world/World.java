package world;

import animation.Direction;
import battle.Battle;
import game.Game;
import geometry.GameRect;
import gui.window.GameInteraction;
import item.Inventory;
import item.Item;
import npc.NPC;
import npc.player.Player;
import npc.player.PlayerStats;
import room.Room;
import tiles.Tile;

import java.awt.*;
import java.util.ArrayList;


public class World {

    public int roomId;
    public boolean isEditing;
    public ArrayList<NPC> npcsInWorld = new ArrayList<>();
    private ArrayList<NPC> npcList = new ArrayList<>();
    public ArrayList<Tile> tileList = new ArrayList<>();
    private ArrayList<Room> roomList = new ArrayList<>();
    public ArrayList<Item> itemList = new ArrayList<>();
    public int offsetX = 0;
    public int offsetY = 0;
    public static Room room;
    public static Tile selectedTile;
    public static boolean hasSelectedTile = false;
    public static Tile nothingTile;
    public boolean paused = false;
    public boolean isInteracting = false;
    public Player player;
    public Battle battle;
    public ArrayList<Decision> decisions = new ArrayList<Decision>();
    public PlayerStats playerStats;
    public Inventory inventory;
    public int decisionCount;
    public int mapCount;
    private int flashHeartTimes;
    private long flashHeartTime;
    public boolean isStartingBattle = false;
    public Battle tempBattle;
    public int playerX;
    public int playerY;

    public World(boolean isEditing) {
        this.isEditing = isEditing;
    }

    public void spawnNPCWithId(int id){
        if(npcList.size() > 0) {
            for (int i = 0; i < npcList.size(); i++) {
                if (npcList.get(i).id == id) {
                    if (npcList.get(i) instanceof Player){
                        if (Game.world.player != null) {

                            if(!npcsInWorld.contains(Game.world.player)){

                                npcsInWorld.add(Game.world.player);
                            }
                            continue;
                        } else {
                            Game.world.player = (Player) npcList.get(i);
                            if(Game.world.playerX == 0){
                                Game.world.playerX = 80;
                            }
                            if(Game.world.playerY == 0){
                                Game.world.playerY = 80;
                            }
                            Game.world.player.x = Game.world.playerX;
                            Game.world.player.y = Game.world.playerY;
                            npcsInWorld.add(Game.world.player);
                            Game.world.player.onCreate();
                            continue;
                        }
                    }
                    NPC tempNpc = (NPC) npcList.get(i).clone();
                    npcsInWorld.add(tempNpc);
                    tempNpc.onCreate();
                }
            }
        } else {

        }
    }

    public void spawnNPCWithIdAndCoord(int id, int x, int y){

        if(npcList.size() > 0) {
            for (int i = 0; i < npcList.size(); i++) {
                if (npcList.get(i).id == id) {
                    if (npcList.get(i) instanceof Player){
                        if (Game.world.player != null) {

                            continue;
                        } else {
                            Game.world.player = (Player) npcList.get(i);
                            Game.world.player.x = x;
                            Game.world.player.y = y;
                            npcsInWorld.add(Game.world.player);
                            Game.world.player.onCreate();
                            continue;
                        }
                    }
                    NPC tempNpc = (NPC) npcList.get(i).clone();
                    tempNpc.x = x;
                    tempNpc.y = y;
                    npcsInWorld.add(tempNpc);
                    tempNpc.onCreate();
                }
            }
        } else {

        }
    }

    public void spawnTileWithId(int id, int x, int y){
        if(tileList.size() > 0) {
            for (int i = 0; i < tileList.size(); i++) {
                if (tileList.get(i).id == id) {
                    Tile tempNpc = (Tile) tileList.get(i).clone();
                    Game.world.room.roomTiles[x][y] = tempNpc;
                    Game.world.room.roomTiles[x][y].x = x * Game.tilesetSize;
                    Game.world.room.roomTiles[x][y].y = y * Game.tilesetSize;
                    Game.world.room.roomTiles[x][y].bounds = new GameRect(x * Game.tilesetSize, y * Game.tilesetSize, Game.tilesetSize, Game.tilesetSize);
                }
            }
        }
    }

    public boolean willCollide(Rectangle rekt, int dx, int dy){
        rekt.x += dx;
        rekt.y += dy;
        for(int roomX = 0; roomX < Game.world.room.roomTiles.length; roomX++) {
            for (int roomY = 0; roomY < Game.world.room.roomTiles[roomX].length; roomY++) {
                if (Game.world.room.roomTiles[roomX][roomY] != null) {
                    if (Game.world.room.roomTiles[roomX][roomY].bounds.intersects(rekt) && Game.world.room.roomTiles[roomX][roomY].solid) {
                        rekt.x -= dx;
                        rekt.y -= dy;
                        return true;
                    }
                }
            }
        }
        for(NPC npc :npcsInWorld){
            if(npc.bounds.intersects(rekt) && npc.bounds != rekt){

                rekt.x -= dx;
                rekt.y -= dy;
                return true;
            }
        }
        rekt.x -= dx;
        rekt.y -= dy;
        return false;
    }



    public NPC getNearestCollidingNPC(Rectangle rekt, int dx, int dy){
        rekt.x += dx;
        rekt.y += dy;
        NPC nearestNPC = null;
        for(NPC npc :npcsInWorld){
            if(npc.bounds.intersects(rekt) && npc.bounds != rekt){
                if(nearestNPC != null) {
                    if (Math.abs(nearestNPC.x - rekt.x) + Math.abs(nearestNPC.y - rekt.y) > Math.abs(npc.x - rekt.x) + Math.abs(npc.y - rekt.y))
                    {
                        nearestNPC = npc;
                    }
                } else {
                    nearestNPC = npc;
                }
            }
        }
        rekt.x -= dx;
        rekt.y -= dy;
        return nearestNPC;
    }

    public Object getNearestCollidingTileOrNPC(Rectangle rekt, int dx, int dy){
        rekt.x += dx;
        rekt.y += dy;
        NPC nearestNPC = null;
        Tile nearestTile = null;
        for(NPC npc :npcsInWorld){
            if(npc.bounds.intersects(rekt) && npc.bounds != rekt){
                if(nearestNPC != null) {
                    if (Math.abs(nearestNPC.x - rekt.x) + Math.abs(nearestNPC.y - rekt.y) > Math.abs(npc.x - rekt.x) + Math.abs(npc.y - rekt.y))
                    {
                        nearestNPC = npc;
                    }
                } else {
                    nearestNPC = npc;
                }
            }
        }
        for(int tempX = 0; tempX < room.roomTiles.length; tempX++){
            for(int tempY = 0; tempY < room.roomTiles[tempX].length; tempY++) {
                if (room.roomTiles[tempX][tempY].bounds.intersects(rekt) && room.roomTiles[tempX][tempY].bounds != rekt) {
                    if (nearestNPC != null) {
                        if (Math.abs(nearestNPC.x - rekt.x) + Math.abs(nearestNPC.y - rekt.y) > Math.abs((room.roomTiles[tempX][tempY].x * Game.tilesetSize) - rekt.x) + Math.abs((room.roomTiles[tempX][tempY].y * Game.tilesetSize) - rekt.y))
                        {
                            nearestTile = room.roomTiles[tempX][tempY];
                        }
                    } else {
                        nearestTile  = room.roomTiles[tempX][tempY];
                    }
                }
            }
        }
        rekt.x -= dx;
        rekt.y -= dy;
        if(nearestNPC != null && nearestTile != null) {
            if (Math.abs(nearestNPC.x - rekt.x) + Math.abs(nearestNPC.y - rekt.y) > Math.abs(nearestTile.x - rekt.x) + Math.abs(nearestTile.y - rekt.y)) {
                return nearestTile;
            } else if (Math.abs(nearestNPC.x - rekt.x) + Math.abs(nearestNPC.y - rekt.y) < Math.abs(nearestTile.x - rekt.x) + Math.abs(nearestTile.y - rekt.y)) {
                return nearestNPC;
            }
        } else {
            if(nearestNPC == null && nearestTile != null){
                return nearestTile;
            } else {
                return nearestNPC;
            }
        }
        return null;
    }

    public Tile getNearestCollidingTile(Rectangle rekt, int dx, int dy){
        rekt.x += dx;
        rekt.y += dy;
        Tile nearestNPC = null;
        for(int tempX = 0; tempX < room.roomTiles.length; tempX++){
            for(int tempY = 0; tempY < room.roomTiles[tempX].length; tempY++) {
                if (room.roomTiles[tempX][tempY].bounds.intersects(rekt) && room.roomTiles[tempX][tempY].bounds != rekt) {
                    if (nearestNPC != null) {
                        if (Math.abs(nearestNPC.x - rekt.x) + Math.abs(nearestNPC.y - rekt.y) > Math.abs((room.roomTiles[tempX][tempY].x * Game.tilesetSize) - rekt.x) + Math.abs((room.roomTiles[tempX][tempY].y * Game.tilesetSize) - rekt.y))
                        {
                            nearestNPC = room.roomTiles[tempX][tempY];
                        }
                    } else {
                        nearestNPC = room.roomTiles[tempX][tempY];
                    }
                }
            }
        }
        rekt.x -= dx;
        rekt.y -= dy;
        return nearestNPC;
    }

    public void addNPC(NPC npc){

        boolean okForAdding = true;
        boolean isEql = false;
        if(!(npc instanceof NPC)){

            okForAdding = false;
        }
        for(NPC listNpc : npcList){
            if(npc.id == listNpc.id){
                okForAdding = false;

            }
        }
        if(okForAdding) npcList.add(npc);
    }

    public void addTile(Tile npc){

        boolean okForAdding = true;
        boolean isEql = false;
        if(!(npc instanceof Tile)){

            okForAdding = false;
        }
        for(Tile listNpc : tileList){
            if(npc.id == listNpc.id){
                okForAdding = false;

            }
        }
        if(okForAdding) tileList.add(npc);
        if(npc.id == 0) nothingTile = npc;
    }

    public void addRoom(Room npc){

        boolean okForAdding = true;
        boolean isEql = false;
        if(!(npc instanceof Room)){

            okForAdding = false;
        }
        for(Room listNpc : roomList){
            if(npc.id == listNpc.id){
                okForAdding = false;

            }
        }
        if(okForAdding) roomList.add(npc);
    }

    public void addItem(Item npc){

        boolean okForAdding = true;
        boolean isEql = false;
        if(!(npc instanceof Item)){

            okForAdding = false;
        }
        for(Item listNpc : itemList){
            if(npc.id == listNpc.id){
                okForAdding = false;

            }
        }
        if(okForAdding) {itemList.add(npc); System.out.println("Adding " + npc.name);}
    }

    public void goToRoomWithId(int id){

        for(Room place : roomList){
            if(place.id == id){
                room = place;
                room.onEnter();
            }
        }
    }

    public void onPostStart(String roomName) {
        selectedTile = tileList.get(1);
        boolean hasFound = false;
        for(Room r : roomList){
            if(r.name.equalsIgnoreCase(roomName)){
                room = r;
                hasFound = true;
                break;
            }
        }
        if(!hasFound) {
            addRoom(new Room(roomList.size() + 1, roomName));
            room = roomList.get(roomList.size() - 1);
        }
        if(!isEditing){
            spawnNPCWithId(1);
            room.onEnter();
        }
    }

    public void onPostStart() {
        selectedTile = tileList.get(1);
        room = roomList.get(roomId);

        if(!isEditing){
            spawnNPCWithId(1);
            room.onEnter();
        } else {
        }
    }

    public Item getItemById(int id){
        for(Item component : itemList){
            if(id == component.id){
                return component;
            }
        }
        return null;
    }

    public void onPostGUIStart() {

    }

    public void startBattle(Battle battle) {
        isStartingBattle = true;
        tempBattle = battle;




    }



    public void startInteraction(GameInteraction interaction){

    }



}
