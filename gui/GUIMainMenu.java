package gui;

import game.Game;
import gui.window.Dialog;
import gui.window.WindowCallback;

import java.awt.*;
import java.awt.geom.AffineTransform;


public class GUIMainMenu extends GUI {
    public GUIMainMenu(){

    }

    private float rotation = 0;

    @Override
    public void onStart() {
        super.onStart();
        addComponent(new Button("Play", 1, Game.GAME_WIDTH / 2 - 100, 100, 100, 100));
        addComponent(new Button("Level Editor", 2, Game.GAME_WIDTH / 2 - 100, 100 + 150, 100, 100));
    }

    @Override
    public void actionPerfomed(GUIComponent guiComponent) {
        super.actionPerfomed(guiComponent);
        if(guiComponent.id == 1){
            Game.game.start(false, "ROOM1.map");
            Game.game.setGUI(new GUIGame());
            visible = false;
        }
        if(guiComponent.id == 2){
            addGUIWindow(new Dialog(0, Game.GAME_WIDTH / 2, Game.GAME_HEIGHT / 2, 200, 100, "Room name: ", true, new WindowCallback(){
                @Override
                public void onEvent(GUIComponent component, String... args) {
                    Game.game.start(true, args[0]);
                    Game.game.setGUI(new GUILevelEditor());
                    visible = false;
                }
            }));
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        rotation+= 0.1f;
        if(rotation > 360){
            rotation = 0;
        }
        AffineTransform old = g2d.getTransform();
        g2d.translate(200, 200);
        g2d.rotate(rotation);
        g2d.translate(-200, -200);
        Color c = g2d.getColor();
        g2d.setColor(Color.black);
        g2d.fillRect(200, 200, 20, 20);
        g2d.rotate(-rotation);
        g2d.setColor(c);
        g2d.setTransform(old);
    }
}
