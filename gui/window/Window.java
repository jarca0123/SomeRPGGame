package gui.window;

import com.sun.org.apache.regexp.internal.RE;
import game.Game;
import graphics.JGraphics2D;
import gui.CloseButton;
import gui.GUI;
import gui.GUIComponent;
import gui.SimpleButton;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;


public class Window  {
    public int id;
    public int x = 100;
    public int y = 100;
    public int width = 128;
    public int height = 128;
    public int newWidth = -1;
    public int newHeight = -1;
    public Rectangle entireWindowBounds;
    public Rectangle frameBounds;
    public Rectangle windowBounds;
    public Rectangle leftFrameBounds;
    public Rectangle rightFrameBounds;
    public Rectangle downFrameBounds;
    private Path2D.Double titleBarPath = new Path2D.Double();
    public ArrayList<GUIComponent> guiComponents = new ArrayList<>();
    public boolean enabled;
    public boolean visible;
    public GUIComponent selectedComponent;
    public int minimalWidth = 30;
    public int minimalHeight = 30;
    public boolean deletThis = false;
    private int animationSpeed;
    public boolean isResizing = false;
    public boolean actionBarEnabled;

    public Window(int id, boolean actionBarEnabled){
        this.id = id;
        this.actionBarEnabled = actionBarEnabled;
        this.entireWindowBounds = new Rectangle(x, y, width, height);
        this.frameBounds = new Rectangle(x + 10, y + 10, width - 20, height - 20);
        this.windowBounds = new Rectangle(x - 10, y - 10, width + 20, height + 20);
        this.leftFrameBounds = new Rectangle(x, y + 20, 10, height - 20);
        this.rightFrameBounds = new Rectangle(x + width - 10, y + 20, 10, height - 20);
        this.downFrameBounds = new Rectangle(x, y + height - 10, width, 10);
        enabled = true;
        visible = true;
        if(actionBarEnabled) addComponent(new CloseButton(-1, 0, 0, 20, 20, null, this, this, null, 0, 0, 0, 0));
    }

    public Window(int id, int x, int y, int width, int height, boolean actionBarEnabled){
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.actionBarEnabled = actionBarEnabled;
        this.entireWindowBounds = new Rectangle(x, y, width, height);

        this.frameBounds = new Rectangle(x, y, width, 20);

        this.windowBounds = new Rectangle(x + 10, y + 10, width - 20, height - 20);
        this.leftFrameBounds = new Rectangle(x, y + 20, 10, height - 20);
        this.rightFrameBounds = new Rectangle(x + width - 10, y + 20, 10, height - 20);
        this.downFrameBounds = new Rectangle(x, y + height - 10, width, 10);
        enabled = true;
        visible = true;
        if(actionBarEnabled) addComponentRelativeToWindow(new CloseButton(-1, 0, 0, 20, 20, null, this, this, null, 0, -20, 0, 0));
    }
    public GUIComponent addComponent(GUIComponent guiComponent){
        guiComponent.windowParent = this;
        guiComponent.initForWindow();
        guiComponents.add(guiComponent);
        guiComponent.scrollBarY = (int) frameBounds.getY() + 10;
        return guiComponent;
    }

    public void removeComponent(GUIComponent guiComponent){
        guiComponents.remove(guiComponent);
    }

    public void removeComponent(int id){

        guiComponents.remove(getComponentById(id));
    }

    public void addComponentRelativeToWindow(GUIComponent guiComponent){
        guiComponent.windowParent = this;
        guiComponents.add(guiComponent);
        guiComponent.scrollBarY = (int) frameBounds.getY() + 10;
    }

    public void addComponent(GUIComponent guiComponent, int x, int y, int width, int height){
        guiComponent.x = x;
        guiComponent.y = y;
        guiComponent.width = width;
        guiComponent.height = height;
        guiComponent.windowParent = this;
        guiComponents.add(guiComponent);
        guiComponent.scrollBarY = (int) frameBounds.getY() + 10;
    }


    public static void addGUIWindow(Window window){

        Game.gui.windowsToAdded.add(window);
    }

    public void draw(Graphics2D g2d) {
        if(visible) {



            this.entireWindowBounds = new Rectangle(x, y, width, height);

            this.frameBounds = new Rectangle(x, y, width, 20);

            this.windowBounds = new Rectangle(x + 10, y + 10, width - 20, height - 20);
            this.leftFrameBounds = new Rectangle(x, y + 20, 10, height - 20);
            this.rightFrameBounds = new Rectangle(x + width - 10, y + 20, 10, height - 20);
            this.downFrameBounds = new Rectangle(x, y + height - 10, width, 10);
            g2d.setClip(entireWindowBounds);
            g2d.setColor(Color.BLACK);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            long startTime = System.nanoTime();
            g2d.fillRect(x, y, width, height);
            long endTime = System.nanoTime();



            g2d.setColor(Color.WHITE);
            g2d.setClip(null);


            g2d.setStroke(new BasicStroke(5));
            g2d.drawRect(x, y, width, height);
            g2d.setStroke(new BasicStroke(1));
            g2d.setClip(windowBounds);



            for (GUIComponent guiComponent : guiComponents) {
                if (guiComponent.visible) {
                    g2d.setClip(windowBounds);
                    guiComponent.draw(g2d);
                    guiComponent.drawOver(g2d);
                }
                if (guiComponent.enabled) {

                }
            }
        }
        if(newWidth != -1 && newWidth != width){
            if(newWidth < width){
                if(width - (animationSpeed) < newWidth)
                {
                    width -= (width - newWidth);
                    x += (width - newWidth) / 2;
                } else {
                    width -= animationSpeed;
                    x += animationSpeed / 2;
                }
            } else {
                if(newWidth < width + (animationSpeed))
                {
                    width += (newWidth - width);
                    x -= (newWidth - width) / 2;
                } else {
                    width += animationSpeed;
                    x -= animationSpeed / 2;
                }
            }
        } else if(newHeight != -1 && newHeight != width){
            if(newHeight < height){
                if(height - (animationSpeed) < newHeight)
                {
                    height -= (height - newHeight);
                    y += (height - newHeight) / 2;
                } else {
                    height -= animationSpeed;
                    y += animationSpeed / 2;
                }
            } else {
                if(newHeight < height + (animationSpeed))
                {
                    height += (newHeight - height);
                    y -= (newHeight - height) / 2;
                } else {
                    height += animationSpeed;
                    y -= animationSpeed / 2;
                }
            }
        } else {
            newWidth = -1;
            newHeight = -1;
            animationSpeed = -1;
            isResizing = false;
        }
        g2d.setClip(null);
        g2d.setColor(Color.white);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void initializeProperties(){
        for(GUIComponent component : guiComponents){

        }
    }

    public GUIComponent getComponentById(int id){
        for(GUIComponent component : guiComponents){
            if(id == component.id){
                return component;
            }
        }
        System.out.println(id + " not found");
        return null;
    }

    public void actionPerformed(GUIComponent guiComponent) {
        if(enabled) {
            if (guiComponent.id == -1) {
                Game.game.gui.removeGUIWindow(this);
            }
        }
    }

    public void onWindowClose() {





    }

    public void enterPerformed(GUIComponent guiComponent) {

    }

    public void keyPerformed(GUIComponent guiComponent, KeyEvent e) {
    }

    public void resizeWindow(int width, int height, int speed) {
        newWidth = width;
        newHeight = height;
        animationSpeed = speed;
        isResizing = true;
    }

    public void onActionKey() {
    }
    public void onBackKey() {
    }
    public void onMenuKey() {
    }



}
