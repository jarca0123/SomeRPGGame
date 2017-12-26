package file;

import game.Game;
import tiles.Tile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Scanner;
import java.util.Base64;


public class RoomSaver {
    public static void saveRoom() throws IOException {
        Tile[][] room = Game.world.room.roomTiles;
        int roomX = Game.world.room.width;
        int roomY = Game.world.room.height;
        String fileContents = "";
        fileContents += "ROOM" + Game.world.room.id + ";" + roomX + ";" + roomY + ";";
        Base64.Encoder encoder = Base64.getEncoder();
        for(int i = 0; i < roomX; i++) {
            if(room[i] == null) {
                for(int lel = 0; lel < roomY; lel++){
                    fileContents += "0;0;";
                }

            }
            for (int j = 0; j < roomY; j++) {
                if(room[i][j] != null) {
                    String temp = Integer.toString(room[i][j].id);

                    fileContents += new String(encoder.encode(Integer.toString(room[i][j].id).getBytes(StandardCharsets.UTF_8))) + ";";

                    if(room[i][j].data != "") {
                        fileContents += new String(encoder.encode(room[i][j].data.getBytes(StandardCharsets.UTF_8))) + ";";

                    } else {
                        fileContents += "0;";

                    }
                } else {
                    fileContents += "0;0;";

                }
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(Game.world.room.name);
        fileOutputStream.write(fileContents.getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();

    }
}
