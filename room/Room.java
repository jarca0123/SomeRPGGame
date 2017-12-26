package room;

import file.RoomLoader;
import game.Game;
import geometry.GameRect;
import tiles.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class Room {
    public Tile[][] roomTiles;
    private ArrayList<ArrayList<Tile>> listRoomTiles = new ArrayList<>();
    public int id;
    public String name;
    public int width;
    public int height;
    public Tile highlightedTile;
    public int highlightedTileX;
    public int highlightedTileY;
    public Room(int id, String name){
        this.id = id;
        this.name = name;
        try {
            this.roomTiles = RoomLoader.loadRoomTiles(name);
            ArrayList<ArrayList<Tile>> result = new ArrayList<>();
            for(Tile[] array : roomTiles){
                result.add( new ArrayList(Arrays.asList(array)));
            }
            this.listRoomTiles = result;
        } catch (Exception e) {
            e.printStackTrace();
            scanRoomImage(name);
        }
        this.width = this.roomTiles.length;
        this.height = this.roomTiles[0].length;
    }

    public <T> List<Tile> twoDArrayToList(Tile[][] twoDArray) {
        List<Tile> list = new ArrayList<Tile>();
        for (Tile[] array : twoDArray) {
            list.addAll(Arrays.asList(array));
        }
        return list;
    }

    public Room(int id, String name, int width, int height, Tile[][] roomTiles){
        this.id = id;
        this.name = name;
        this.roomTiles = roomTiles;
        this.width = width;
        this.height = height;
    }

    private String betterHexString(int number){
        if(number < 15){
            return "0" + Integer.toHexString(number);
        } else {
            return Integer.toHexString(number);
        }
    }

    private void scanRoomImage(String name){
        BufferedImage roomImage = null;
        try {
            roomImage = ImageIO.read(new File(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int roomX = roomImage.getWidth();
        int roomY = roomImage.getHeight();
        width = roomX;
        height = roomY;
        roomTiles = new Tile[roomX][roomY];
        for(roomX = 0; roomX < roomImage.getWidth(); roomX++){
            for(roomY = 0; roomY < roomImage.getHeight(); roomY++){
                int rgb = roomImage.getRGB(roomX, roomY);
                int red = (rgb & 0x00ff0000) >> 16;
                int green = (rgb & 0x0000ff00) >> 8;
                int blue  =  rgb & 0x000000ff;
                String rgbString = betterHexString(red) + betterHexString(green) + betterHexString(blue);
                for(Tile tile : Game.world.tileList){
                    if(tile.pixelCode.equalsIgnoreCase(rgbString)){
                        Tile tempTile = (Tile) tile.clone();
                        roomTiles[roomX][roomY] = tempTile;
                        roomTiles[roomX][roomY].x = roomX * Game.tilesetSize;
                        roomTiles[roomX][roomY].y = roomY * Game.tilesetSize;
                        roomTiles[roomX][roomY].bounds = new GameRect(roomX * Game.tilesetSize, roomY * Game.tilesetSize, Game.tilesetSize, Game.tilesetSize);
                        roomTiles[roomX][roomY].properties = new Properties();
                        roomTiles[roomX][roomY].readData();
                    }
                }
            }
        }
    }

    public void onEnter() {
        for(int roomX = 0; roomX < roomTiles.length; roomX++) {
            for (int roomY = 0; roomY < roomTiles[roomX].length; roomY++) {
                roomTiles[roomX][roomY].onEnter();
            }
        }
    }
}
