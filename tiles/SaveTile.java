package tiles;

import game.Game;
import gui.GUIComponent;
import gui.window.GameInteraction;
import gui.window.SaveDialog;
import gui.window.Window;
import gui.window.WindowCallback;
import npc.dialogue.Dialogue;


public class SaveTile extends Tile {
    private GameInteraction interaction;
    private SaveTile thiz = this;
    private int saveTileId = 0;
    public SaveTile(int id, String name, String imageSrc, String pixelCode, boolean solid, boolean useTileset) {
        super(id, name, imageSrc, pixelCode, solid, useTileset);
        npcDialogue = new Dialogue("Something determination.");
    }

    @Override
    public void readData() {
        super.readData();
        if(properties.getProperty("saveTileId") != null){saveTileId = Integer.parseInt(properties.getProperty("saveTileId"));} else{saveTileId=0;}
    }

    @Override
    public void onInteraction() {
        super.onInteraction();

        if(!Game.world.isInteracting) {
            interaction = new GameInteraction(-20000 - id, 0, Game.GAME_HEIGHT - 120, Game.GAME_WIDTH, 120, false, this, new WindowCallback() {
                @Override
                public void onEvent(GUIComponent component, String... args) {
                    Window.addGUIWindow(new SaveDialog(-3130337, 120, 140, 400, 175, false, thiz));
                }
            });
            Window.addGUIWindow(interaction);
            Game.world.isInteracting = true;
        } else {
            interaction.nextDialogue();
        }
    }
}
