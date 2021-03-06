package gui;

import game.Game;
import gui.window.GameInteraction;
import gui.window.InventoryWindow;
import gui.window.SaveDialog;
import gui.window.Window;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class GUI implements Cloneable, KeyListener, MouseMotionListener, MouseListener {

    public ArrayList<GUIComponent> guiComponents = new ArrayList<>();
    public ArrayList<gui.window.Window> windows = new ArrayList<gui.window.Window>();
    public boolean visible = true;
    public int windowDragOffsetX;
    public int windowDragOffsetY;
    public int scrollBarOffsetX = 0;
    public int scrollBarOffsetY = 0;
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;
    public boolean holding = false;
    public Thread mouseHoldThread;
    public int boundsType;
    public int lastMouseX;
    public int lastMouseY;
    public int lastY;
    public int currentY;
    public boolean dragging = false;
    public Rectangle dragBounds;
    public int dragMouseX;
    public int dragMouseY;
    public gui.window.Window selectedWindow;
    public GUIComponent selectedComponent;
    public boolean clickedOnSomething = false;
    public ArrayList<Integer> keyStrokes = new ArrayList<>();
    public ArrayList<Integer> keyStrokesHolding = new ArrayList<>();
    public ArrayList<Window> windowsToRemove = new ArrayList<>();
    public ArrayList<Window> windowsToAdded = new ArrayList<>();
    private ArrayList<GUIComponent> componentsToRemove = new ArrayList<>();
    private ArrayList<GUIComponent> componentsToAdded = new ArrayList<>();

    public GUI(){

    }
    public int mouseX;
    public int mouseY;

    public GUIComponent addComponent(GUIComponent guiComponent){
        componentsToAdded.add(guiComponent);
        return guiComponent;
    }

    public ArrayList<GUIComponent>  addComponent(ArrayList<GUIComponent> components){
        for(GUIComponent guiComponent : components){
            componentsToAdded.add(guiComponent);
        }
        return components;
    }

    public GUIComponent  addComponent(GUIComponent guiComponent, int x, int y, int width, int height){
        guiComponent.x = x;
        guiComponent.y = y;
        guiComponent.width = width;
        guiComponent.height = height;
        componentsToAdded.add(guiComponent);
        return guiComponent;
    }

    public GUIComponent getComponentById(int id){
        for(GUIComponent component : guiComponents){
            if(id == component.id){
                return component;
            }
        }
        for(GUIComponent component : componentsToAdded){
            if(id == component.id){
                return component;
            }
        }
        System.out.println(id + " not found");
        return null;
    }

    public gui.window.Window getWindowById(int id){
        for(gui.window.Window component : windows){
            if(id == component.id){
                return component;
            }
        }
        return null;
    }

    public void replaceComponent(int id, GUIComponent newComponent){
        guiComponents.set(guiComponents.indexOf(getComponentById(id)), newComponent);
    }

    public void replaceComponents(int from, int to, ArrayList<GUIComponent> newComponents){
        if(to < from){
            int tempTo = to;
            to = from;
            from = tempTo;
        }
        if(Math.abs(to - from) + 1 != newComponents.size()){

        } else {
            for(int i = 0; i < newComponents.size(); i++) {
                guiComponents.set(guiComponents.indexOf(getComponentById(i + from)), newComponents.get(i));
            }
        }
    }

    public void addGUIWindow(gui.window.Window window){
        windows.add(window);
    }

    public void removeGUIWindow(gui.window.Window window){
        window.onWindowClose();
        windowsToRemove.add(window);

    }

    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {

        FontMetrics metrics = g.getFontMetrics(font);

        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;

        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g.setFont(font);

        g.drawString(text, x, y);
    }

    public void actionPerfomed(GUIComponent guiComponent){}

    public void draw(Graphics2D g2d) {
        for(Window w: windowsToRemove){
            windows.remove(w);
            windows.trimToSize();
        }
        windowsToRemove = new ArrayList<>();
        for(Window w: windowsToAdded){
            windows.add(w);
        }
        windowsToAdded = new ArrayList<>();
        for(GUIComponent w: componentsToRemove){
            guiComponents.remove(w);
            guiComponents.trimToSize();
        }
        componentsToRemove = new ArrayList<>();
        for(GUIComponent w: componentsToAdded){
            guiComponents.add(w);
        }
        componentsToAdded = new ArrayList<>();
        up = Game.gui.keyStrokes.contains(KeyEvent.VK_UP);
        down = Game.gui.keyStrokes.contains(KeyEvent.VK_DOWN);
        left = Game.gui.keyStrokes.contains(KeyEvent.VK_LEFT);
        right = Game.gui.keyStrokes.contains(KeyEvent.VK_RIGHT);
            for (GUIComponent guiComponent : guiComponents) {
                if (guiComponent.visible) {
                    guiComponent.draw(g2d);
                }
                if (guiComponent.enabled) {


                    g2d.setColor(Color.BLACK);
                }
            }
    }

    public void onMenuKey() {



        boolean closedInventory = false;
        for(Window w : Game.gui.windows) {
            if (w instanceof InventoryWindow) {
                Game.gui.removeGUIWindow(w);
                closedInventory = true;

            }
        }
        if(!closedInventory) Window.addGUIWindow(new InventoryWindow(-17317101, 50, 50, 300, 400, false));
    }

    public void onActionKey() {
        gui.window.Window tempWindow = null;




        GameInteraction gameInteractionWindow = null;
        for(gui.window.Window w: Game.gui.windows){
            w.onActionKey();
            if(w instanceof GameInteraction){
                gameInteractionWindow = (GameInteraction) w;
            }
            if(w instanceof SaveDialog){
                tempWindow = w;

            } else if(w instanceof InventoryWindow){


            }
        }

        if(gameInteractionWindow != null) {
            if(Game.world.isInteracting) {
                gameInteractionWindow.nextDialogue();
            } else {
                Game.world.isInteracting = true;
                Window.addGUIWindow(gameInteractionWindow);
            }
        }
        if(Game.world.player != null) {
            Game.world.player.onActionKey();
        }
    }

    public void onBackKey(){
        for(gui.window.Window w: Game.gui.windows){
            if(w instanceof InventoryWindow){
                ((InventoryWindow)w).hasSelected = false;
            }
        }
        if(Game.gui instanceof GUIBattle) {

        }

    }

    public void onStart(){

    }

    public void initializeProperties(){

    }

    public void enterPerfomed() {
    }

    public void keyPerformed() {
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (selectedComponent != null) {
            selectedComponent.onKey(e);
        } else {
            if (!keyStrokes.contains((Integer) e.getKeyCode()) && !keyStrokesHolding.contains((Integer) e.getKeyCode())) keyStrokes.add(e.getKeyCode());
            keyStrokesHolding.add(e.getKeyCode());


            if(e.getKeyCode() == KeyEvent.VK_C ||e.getKeyCode() == KeyEvent.VK_CONTROL){
                onMenuKey();

            }
            if(e.getKeyCode() == KeyEvent.VK_X ||e.getKeyCode() == KeyEvent.VK_SHIFT){
                onBackKey();
            }
            if(e.getKeyCode() == KeyEvent.VK_Y ||e.getKeyCode() == KeyEvent.VK_Z || e.getKeyCode() == KeyEvent.VK_ENTER){
                onActionKey();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (selectedComponent == null) {
            keyStrokes.remove((Integer) e.getKeyCode());
            while(keyStrokesHolding.remove((Integer) e.getKeyCode()));
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                up = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                down = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                left = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                right = false;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new Error("Something impossible just happened");
        }
    }

    @Override
    @Deprecated
    public void mousePressed(MouseEvent e) {


        int mouseX = e.getX();
        int mouseY = e.getY();
        clickedOnSomething = false;
        ArrayList<GUIComponent> clickedGUIComponents = new ArrayList<>();
        for (Window window: Game.gui.windows) {
            if(!clickedOnSomething) {
                for (GUIComponent guiComponent : window.guiComponents) {
                    if (guiComponent.bounds.intersects(new Rectangle(mouseX, mouseY, 1, 1))) {
                        if (guiComponent instanceof TextBox || guiComponent instanceof gui.TextArea) {
                            window.selectedComponent = guiComponent;
                            selectedComponent = guiComponent;
                        }
                        selectedWindow = window;

                        clickedGUIComponents.add(guiComponent);
                        clickedOnSomething = true;
                    }
                }
            }
        }
        for(GUIComponent guiComponent : clickedGUIComponents){
            guiComponent.onClick();
        }

        if(selectedWindow != null) {
            if (selectedWindow.deletThis) {
                Game.gui.windows.remove(selectedWindow);
                Game.gui.windows.trimToSize();
                selectedWindow = null;
            }
        }
        if (clickedOnSomething) return;
        if (!clickedOnSomething) selectedComponent = null;
        for (Window window: Game.gui.windows) {
            for (GUIComponent guiComponent: window.guiComponents) {
                if (guiComponent.contentHeight > guiComponent.height) {
                    if (guiComponent.scrollBarBounds.intersects(new Rectangle(mouseX, mouseY, 1, 1))) {

                        clickedOnSomething = false;
                    }
                }
            }
        }
        if (clickedOnSomething) return;
        for (GUIComponent guiComponent: Game.gui.guiComponents) {
            if (guiComponent.bounds.intersects(new Rectangle(mouseX, mouseY, 1, 1))) {
                clickedGUIComponents.add(guiComponent);
                clickedOnSomething = true;
            }
        }
        for(GUIComponent guiComponent : clickedGUIComponents) {
            guiComponent.onClick();
        }
        if (clickedOnSomething) return;
        if (holding == false) {
            boundsType = 0;
            for (Window window: Game.gui.windows) {
                if (window.frameBounds.intersects(new Rectangle(mouseX, mouseY, 1, 1))) {
                    boundsType = 1;
                    selectedWindow = window;
                    windowDragOffsetX = mouseX - (int) window.frameBounds.getX();
                    windowDragOffsetY = mouseY - (int) window.frameBounds.getY();
                    holding = true;
                    clickedOnSomething = true;
                }
                if (window.leftFrameBounds.intersects(new Rectangle(mouseX, mouseY, 1, 1))) {
                    boundsType += 2;
                    selectedWindow = window;
                    windowDragOffsetX = mouseX - (int) window.frameBounds.getX();
                    windowDragOffsetY = mouseY - (int) window.frameBounds.getY();
                    holding = true;
                    clickedOnSomething = true;
                }
                if (window.rightFrameBounds.intersects(new Rectangle(mouseX, mouseY, 1, 1))) {
                    boundsType += 3;
                    selectedWindow = window;
                    windowDragOffsetX = mouseX - (int) window.frameBounds.getX();
                    windowDragOffsetY = mouseY - (int) window.frameBounds.getY();
                    holding = true;
                    clickedOnSomething = true;
                }
                if (window.downFrameBounds.intersects(new Rectangle(mouseX, mouseY, 1, 1))) {
                    boundsType += 4;
                    selectedWindow = window;
                    windowDragOffsetX = mouseX - (int) window.frameBounds.getX();
                    windowDragOffsetY = mouseY - (int) window.frameBounds.getY();
                    holding = true;
                    clickedOnSomething = true;
                }

            }
            if (holding) {
                Game.gui.windows.remove(selectedWindow);
                Game.gui.windows.add(0, selectedWindow);
                return;
            }
            for (Window window: Game.gui.windows) {
                for (GUIComponent guiComponent: window.guiComponents) {
                    if (guiComponent.contentHeight > guiComponent.height) {
                        if (guiComponent.scrollBarBounds.intersects(new Rectangle(mouseX, mouseY, 1, 1))) {
                            boundsType = 8;
                            selectedWindow = window;
                            selectedComponent = guiComponent;
                            scrollBarOffsetX = mouseX - (int) guiComponent.scrollBarBounds.getX();
                            scrollBarOffsetY = mouseY - (int) guiComponent.scrollBarBounds.getY();
                            holding = true;
                            clickedOnSomething = true;
                        }
                    }
                }
            }
            if (holding) {
                Game.gui.windows.remove(selectedWindow);
                Game.gui.windows.add(0, selectedWindow);
                return;
            } else {
                selectedComponent = null;
                selectedWindow = null;
            }
        }
        if(!clickedOnSomething){
            onClick(e);
        }
    }

    public void onClick(MouseEvent e){

    }


    @Override
    public void mouseReleased(MouseEvent e) {
        holding = false;
        selectedWindow = null;
        dragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        currentY = e.getY();
        dragBounds = null;

        if (selectedWindow != null) {
            if (holding) {


                if (boundsType == 1) {
                    if (selectedWindow != null) {
                        selectedWindow.x = e.getX() - windowDragOffsetX;
                        selectedWindow.y = e.getY() - windowDragOffsetY;
                    }
                } else if (boundsType == 2) {
                    if (selectedWindow != null) {

                        if (selectedWindow.width < selectedWindow.minimalWidth) {
                            selectedWindow.width = selectedWindow.minimalWidth;

                        } else {
                            selectedWindow.width += lastMouseX - e.getX();

                            if (selectedWindow.width <= selectedWindow.minimalWidth) {
                                selectedWindow.width = selectedWindow.minimalWidth;
                            } else {
                                selectedWindow.x = e.getX() - windowDragOffsetX;
                            }
                        }
                        if (selectedWindow.height < selectedWindow.minimalHeight) {
                            selectedWindow.height = selectedWindow.minimalHeight;
                        }

                    }
                } else if (boundsType == 3) {
                    if (selectedWindow != null) {
                        selectedWindow.width -= lastMouseX - e.getX();
                        if (selectedWindow.width < selectedWindow.minimalWidth) {
                            selectedWindow.width = selectedWindow.minimalWidth;
                        }
                        if (selectedWindow.height < selectedWindow.minimalHeight) {
                            selectedWindow.height = selectedWindow.minimalHeight;
                        }
                    }
                } else if (boundsType == 4) {
                    if (selectedWindow != null) {

                        selectedWindow.height -= lastMouseY - e.getY();
                        if (selectedWindow.width < selectedWindow.minimalWidth) {
                            selectedWindow.width = selectedWindow.minimalWidth;
                        }
                        if (selectedWindow.height < selectedWindow.minimalHeight) {
                            selectedWindow.height = selectedWindow.minimalHeight;
                        }
                    }
                } else if (boundsType == 6) {
                    if (selectedWindow != null) {
                        selectedWindow.x = e.getX() - windowDragOffsetX;
                        selectedWindow.width += lastMouseX - e.getX();
                        selectedWindow.height -= lastMouseY - e.getY();
                        if (selectedWindow.width < selectedWindow.minimalWidth) {
                            selectedWindow.width = selectedWindow.minimalWidth;
                        }
                        if (selectedWindow.height < selectedWindow.minimalHeight) {
                            selectedWindow.height = selectedWindow.minimalHeight;
                        }
                    }
                } else if (boundsType == 7) {
                    if (selectedWindow != null) {
                        selectedWindow.width -= lastMouseX - e.getX();
                        selectedWindow.height -= lastMouseY - e.getY();
                        if (selectedWindow.width < selectedWindow.minimalWidth) {
                            selectedWindow.width = selectedWindow.minimalWidth;
                        }
                        if (selectedWindow.height < selectedWindow.minimalHeight) {
                            selectedWindow.height = selectedWindow.minimalHeight;
                        }
                    }
                }

            }

        }
        if (selectedComponent != null) {
            if (boundsType == 8) {
                if (selectedComponent != null) {
                    selectedComponent.scrollBarY = e.getY() - scrollBarOffsetY;
                    if (selectedComponent.scrollBarY + selectedComponent.scrollBarHeight >= selectedComponent.y + selectedComponent.height) {
                        selectedComponent.scrollBarY -= (selectedComponent.scrollBarY + selectedComponent.scrollBarHeight) - (selectedComponent.y + selectedComponent.height);
                    }
                    if (selectedComponent.scrollBarY <= selectedComponent.y) {
                        selectedComponent.scrollBarY -= (selectedComponent.scrollBarY) - (selectedComponent.y);
                    }
                    selectedComponent.scrollableHeight = selectedComponent.height - selectedComponent.scrollBarHeight;
                    selectedComponent.contentOffsetY = -((selectedComponent.contentHeight / selectedComponent.scrollableHeight) * (selectedComponent.scrollBarY - selectedComponent.y));
                }
            }
        }
        lastMouseX = e.getX();
        lastMouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {


        lastMouseX = e.getX();
        lastMouseY = e.getY();
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void onDestroy() {
        this.guiComponents = new ArrayList<>();
        this.windows = new ArrayList<>();
    }
}
