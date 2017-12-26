package gui.window;

import game.Game;
import gui.*;
import tiles.Tile;

import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;


public class DataEdit extends Window {
    private ArrayList<Tile> tiles;
    private int entries = 0;
    private SimpleButton addButton;
    private SimpleButton removeButton;
    private boolean isSingleTile = false;


    public DataEdit(int id, int x, int y, int width, int height, Tile tiles) {
        super(id, x, y, width, height, true);
        this.tiles = new ArrayList<Tile>(){{add(tiles);}};
        this.isSingleTile = true;
        String name = "";
        int i = 2;
        for(Enumeration e = this.tiles.get(0).properties.propertyNames(); e.hasMoreElements();){
            name = (String) e.nextElement();
            addComponent(new TextBox(i, 0.5, 50 * (i - 2), 0.5, 50, null, null, null, null, 0, 0, 0, 0, this.tiles.get(0).properties.getProperty(name)));
            addComponent(new TextBox(-i, 0, 50 * (i - 2), 0.5, getComponentById(i).height, null, null, null, null, 0, 0, 0, 0, name));


            i++;

        }
        entries = i - 2;

        addButton = new SimpleButton(0, 0, (50 * entries), 50, 50, null, null, null, null, 0, 0, 10, 0, "+");
        removeButton = new SimpleButton(1, 60, (50 * entries), 50, 50, null, null, null, null, 0, 0, 10, 0, "-");

        addComponent(addButton);
        addComponent(removeButton);
    }

    public DataEdit(int id, int x, int y, int width, int height, HashSet<Tile> tiles) {
        super(id, x, y, width, height, true);
        this.tiles = new ArrayList(tiles);
        String name = "";
        int i = 2;


            addComponent(new TextBox(i, 0.5, 50 * (i - 2), 0.5, 50, null, null, null, null, 0, 0, 0, 0, ""));
            addComponent(new TextBox(-i, 0, 50 * (i - 2), 0.5, getComponentById(i).height, null, null, null, null, 0, 0, 0, 0, ""));


            i++;


        entries = i - 2;

        addButton = new SimpleButton(0, 0, (50 * entries), 50, 50, null, null, null, null, 0, 0, 10, 0, "+");
        removeButton = new SimpleButton(1, 60, (50 * entries), 50, 50, null, null, null, null, 0, 0, 10, 0, "-");

        addComponent(addButton);
        addComponent(removeButton);
    }

    @Override
    public void onWindowClose() {
        super.onWindowClose();
        String text = "";

        for(int i = 2; i < entries + 2; i++){

            if(!((TextBox)getComponentById(-i)).text.replace(" ", "").equals("") || ((TextBox)getComponentById(i)).text.replace(" ", "").equals("")) {

                text += ((TextBox) getComponentById(-i)).text;
                text += "=";
                text += ((TextBox) getComponentById(i)).text;
                text += "\n";
            }
        }

        for(Tile tile : tiles) {
            tile.data = text;
            tile.readData();
        }
        if(!isSingleTile) {
            ((GUILevelEditor) Game.gui).closeDataEditWindow();
        }
    }

    @Override
    public void actionPerformed(GUIComponent guiComponent) {
        super.actionPerformed(guiComponent);
        if(guiComponent.id == 0){

            addComponent(new TextBox(entries + 2, 0.5, 50 * (entries), 0.5, 50, null, null, null, null, 0, 0, 0, 0, ""));
            addComponent(new TextBox(-(entries + 2), 0, 50 * (entries), 0.5, getComponentById(entries + 1).height, null, null, null, null, 0, 0, 0, 0, ""));

            guiComponent.dy += 50;
            getComponentById(1).dy += 50;
            entries++;
            if(entries > 1){
                getComponentById(1).visible = true;
                getComponentById(1).enabled = true;
            }
        }
        if(guiComponent.id == 1){
            removeComponent(-(entries + 1));
            removeComponent((entries + 1));
            guiComponent.dy -= 50;
            getComponentById(0).dy -= 50;
            entries--;
            if(entries < 2){
                getComponentById(1).visible = false;
                getComponentById(1).enabled = false;
            }
        }
    }

    public Properties parsePropertiesString(String s) {


        final Properties p = new Properties();
        try {
            p.load(new StringReader(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }
}
