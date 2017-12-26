package file;

import game.Game;
import geometry.GameRect;
import room.Room;
import sun.misc.IOUtils;
import tiles.Tile;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;
import java.util.Scanner;

import static java.nio.file.Files.readAllBytes;



public class RoomLoader {
    public static Tile[][] loadRoomTiles(String file) throws IOException, URISyntaxException {

        String roomFile = Game.convertStreamToString(Game.game.getClass().getClassLoader().getResourceAsStream(file));
        String[] roomData = roomFile.split(";");
        String noHeaders = roomFile.replace(roomData[0] + ";" + roomData[1] + ";" + roomData[2] + ";", "");
        String[] noHeadersRoomData = noHeaders.split(";");
        int width = Integer.parseInt(roomData[1]);
        int height = Integer.parseInt(roomData[2]);
        Tile[][] roomTiles = new Tile[width][height];
        ArrayList<String> idsList = new ArrayList<>();
        ArrayList<String> datasList = new ArrayList<>();
        for(int i = 0; i < noHeadersRoomData.length; i += 2){
            idsList.add(noHeadersRoomData[i]);
        }
        for(int i = 1; i < noHeadersRoomData.length; i += 2){
            datasList.add(noHeadersRoomData[i]);
        }
        String[][] ids = new String[width][height];
        String[][] datas = new String[width][height];
        Base64.Decoder decoder = Base64.getDecoder();
        for(int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(!idsList.get(y + (x * height)).equalsIgnoreCase("0")){
                    ids[x][y] = new String(decoder.decode(idsList.get(y + (x * height))));
                } else {
                    ids[x][y] = "0";
                }
                if(!datasList.get(y + (x * height)).equalsIgnoreCase("0")) {
                    datas[x][y] = new String(decoder.decode(datasList.get(y + (x * height))));
                } else {
                    datas[x][y] = "0";
                }
            }
        }
        for(int roomX = width - 1; roomX > -1; roomX--) {
            for (int roomY = height - 1; roomY > -1; roomY--) {
                for (Tile tile : Game.world.tileList) {
                    if (tile.id == Integer.parseInt(ids[roomX][roomY])) {
                        Tile tempTile = (Tile) tile.clone();
                        roomTiles[roomX][roomY] = tempTile;
                        roomTiles[roomX][roomY].x = roomX * Game.tilesetSize;
                        roomTiles[roomX][roomY].y = roomY * Game.tilesetSize;
                        roomTiles[roomX][roomY].roomX = roomX;
                        roomTiles[roomX][roomY].roomY = roomY;
                        roomTiles[roomX][roomY].bounds = new GameRect(roomX * Game.tilesetSize, roomY * Game.tilesetSize, Game.tilesetSize, Game.tilesetSize);
                        roomTiles[roomX][roomY].data = datas[roomX][roomY];
                        roomTiles[roomX][roomY].properties = new Properties();
                        roomTiles[roomX][roomY].readData();
                    }
                }
            }
        }
        return roomTiles;
    }
    public static Room loadRoom(File file) throws IOException {
        String roomFile = new String(Files.readAllBytes(file.toPath()));
        String[] roomData = roomFile.split(";");
        String noHeaders = roomFile.replace(roomData[0] + ";" + roomData[1] + ";" + roomData[2] + ";", "");
        String[] noHeadersRoomData = noHeaders.split(";");
        int width = Integer.parseInt(roomData[1]);
        int height = Integer.parseInt(roomData[2]);
        Tile[][] roomTiles = new Tile[width][height];
        ArrayList<String> idsList = new ArrayList<>();
        ArrayList<String> datasList = new ArrayList<>();
        for(int i = 0; i < noHeadersRoomData.length; i += 2){
            idsList.add(noHeadersRoomData[i]);
        }
        for(int i = 1; i < noHeadersRoomData.length; i += 2){
            datasList.add(noHeadersRoomData[i]);
        }
        String[][] ids = new String[width][height];
        String[][] datas = new String[width][height];
        for(int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                ids[x][y] = idsList.get(y + (x * width));
                datas[x][y] = datasList.get(y + (x * width));
            }
        }
        for(int roomX = 0; roomX < width; roomX++) {
            for (int roomY = 0; roomY < height; roomY++) {
                for (Tile tile : Game.world.tileList) {
                    if (tile.id == Integer.parseInt(ids[roomX][roomY])) {
                        Tile tempTile = (Tile) tile.clone();
                        roomTiles[roomX][roomY] = tempTile;
                        roomTiles[roomX][roomY].x = roomX * Game.tilesetSize;
                        roomTiles[roomX][roomY].y = roomY * Game.tilesetSize;
                        roomTiles[roomX][roomY].bounds = new GameRect(roomX * Game.tilesetSize, roomY * Game.tilesetSize, Game.tilesetSize, Game.tilesetSize);
                        roomTiles[roomX][roomY].data = datas[roomX][roomY];
                        roomTiles[roomX][roomY].properties = new Properties();
                        roomTiles[roomX][roomY].readData();
                    }
                }
            }
        }
        return new Room(Integer.parseInt(roomData[0].replaceFirst("ROOM", "")), file.getName(), width, height, roomTiles);
    }
}
