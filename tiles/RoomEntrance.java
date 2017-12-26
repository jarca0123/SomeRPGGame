package tiles;

import game.Game;
import npc.NPC;
import npc.player.Player;


public class RoomEntrance extends Tile{
    private int idOfRoom;
    public RoomEntrance(int id, String name, String imageSrc, String pixelCode, boolean solid, boolean useTileset) {
        super(id, name, imageSrc, pixelCode, solid, useTileset);
    }

    @Override
    public void onEntrance(NPC npc) {
        super.onEntrance(npc);

        if(npc instanceof Player){

            Game.world.goToRoomWithId(idOfRoom);
        }
    }

    @Override
    public void readData()  {
        super.readData();
        idOfRoom = Game.parseIntNoException(properties.getProperty("idOfRoom"));
    }
}
