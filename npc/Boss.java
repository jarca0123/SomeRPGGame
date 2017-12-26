package npc;

import game.Game;


public class Boss extends NPC {
    Boss(int id, String name, String imageSrc, int x, int y, int tileSetWidth, int tileSetHeight, int width, int height, boolean useTileset) {
        super(id, name, imageSrc, x, y, tileSetWidth, tileSetHeight, width, height, useTileset);
    }

    Boss(int id, String name, String imageSrc, int x, int y) {
        super(id, name, imageSrc, x, y);
    }

    @Override
    public void update(double delta) {
        super.update(delta);

    }
}
