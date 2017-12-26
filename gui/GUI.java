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

    public ArrayList<gui.window.Window> windows = new ArrayList<gui.window.Window>();
    public boolean visible = true;
    public Thread mouseHoldThread;
    public int lastY;
    public ArrayList<Integer> keyStrokes = new ArrayList<>();
    ArrayList<GUIComponent> guiComponents = new ArrayList<>();
    boolean up;
    boolean down;
    boolean left;
    boolean right;
    boolean dragging = false;
    Rectangle dragBounds;
    int dragMouseX;
    int dragMouseY;
    gui.window.Window selectedWindow;
    GUIComponent selectedComponent;
    boolean clickedOnSomething = false;
    int mouseX;
    int mouseY;
    private int windowDragOffsetX;
    private int windowDragOffsetY;
    private int scrollBarOffsetX = 0;
    private int scrollBarOffsetY = 0;
    private boolean holding = false;
    private int boundsType;
    private int lastMouseX;
    private int lastMouseY;
    private int currentY;
    private ArrayList<Integer> keyStrokesHolding = new ArrayList<>();
    GUI() {

    }

    void addComponent(GUIComponent guiComponent) {
        guiComponents.add(guiComponent);
    }

    void addComponent(ArrayList<GUIComponent> components) {
        for (GUIComponent guiComponent : components) {
            guiComponents.add(guiComponent);
        }
    }

    public void addComponent(GUIComponent guiComponent, int x, int y, int width, int height) {
        guiComponent.x = x;
        guiComponent.y = y;
        guiComponent.width = width;
        guiComponent.height = height;
        guiComponents.add(guiComponent);
    }

    public GUIComponent getComponentById(int id) {
        for (GUIComponent component : guiComponents) {
            if (id == component.id) {
                return component;
            }
        }
        System.out.println(id + " not found");
        return null;
    }

    public gui.window.Window getWindowById(int id) {
        for (gui.window.Window component : windows) {
            if (id == component.id) {
                return component;
            }
        }
        return null;
    }

    public void replaceComponent(int id, GUIComponent newComponent) {
        guiComponents.set(guiComponents.indexOf(getComponentById(id)), newComponent);
    }

    void replaceComponents(int from, int to, ArrayList<GUIComponent> newComponents) {
        if (to < from) {
            int tempTo = to;
            to = from;
            from = tempTo;
        }
        if (Math.abs(to - from) + 1 != newComponents.size()) {

        } else {
            for (int i = 0; i < newComponents.size(); i++) {
                guiComponents.set(guiComponents.indexOf(getComponentById(i + from)), newComponents.get(i));
            }
        }
    }

    void addGUIWindow(gui.window.Window window) {
        windows.add(window);
    }

    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {

        FontMetrics metrics = g.getFontMetrics(font);

        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;

        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g.setFont(font);

        g.drawString(text, x, y);
    }

    public void actionPerfomed(GUIComponent guiComponent) {
    }

    public void draw(Graphics2D g2d) {
        up = Game.gui.keyStrokes.contains(KeyEvent.VK_UP);
        down = Game.gui.keyStrokes.contains(KeyEvent.VK_DOWN);
        left = Game.gui.keyStrokes.contains(KeyEvent.VK_LEFT);
        right = Game.gui.keyStrokes.contains(KeyEvent.VK_RIGHT);
        if (Game.gui.keyStrokes.contains(KeyEvent.VK_C) || Game.gui.keyStrokes.contains(KeyEvent.VK_CONTROL)) {
            onMenuKey();
            Game.gui.keyStrokes.remove((Integer) KeyEvent.VK_C);
            Game.gui.keyStrokes.remove((Integer) KeyEvent.VK_CONTROL);

        }
        if (Game.gui.keyStrokes.contains(KeyEvent.VK_X) || Game.gui.keyStrokes.contains(KeyEvent.VK_SHIFT)) {
            onBackKey();
            Game.gui.keyStrokes.remove((Integer) KeyEvent.VK_X);
            Game.gui.keyStrokes.remove((Integer) KeyEvent.VK_SHIFT);
        }
        if (Game.gui.keyStrokes.contains(KeyEvent.VK_Y) || Game.gui.keyStrokes.contains(KeyEvent.VK_Z) || Game.gui.keyStrokes.contains(KeyEvent.VK_ENTER)) {
            onActionKey();
            Game.gui.keyStrokes.remove((Integer) KeyEvent.VK_Y);
            Game.gui.keyStrokes.remove((Integer) KeyEvent.VK_Z);
            Game.gui.keyStrokes.remove((Integer) KeyEvent.VK_ENTER);
        }
        for (GUIComponent guiComponent : guiComponents) {
            if (guiComponent.visible) {
                guiComponent.draw(g2d);
            }
            if (guiComponent.enabled) {


                g2d.setColor(Color.BLACK);
            }
        }
    }

    private void onMenuKey() {


        boolean closedInventory = false;
        Window tempWindow = null;
        for (Window w : Game.gui.windows) {
            if (w instanceof InventoryWindow) {
                tempWindow = w;
                w.onWindowClose();
                closedInventory = true;
            }
        }
        if (tempWindow != null) {
            if (tempWindow.deletThis) {
                Game.gui.windows.remove(tempWindow);
                Game.gui.windows.trimToSize();
            }
        }
        if (!closedInventory) Window.addGUIWindow(new InventoryWindow(-17317101, 50, 50, 300, 400, false));
    }

    private void onActionKey() {
        gui.window.Window tempWindow = null;


        GameInteraction gameInteractionWindow = null;
        for (gui.window.Window w : Game.gui.windows) {
            w.onActionKey();
            if (w instanceof GameInteraction) {
                gameInteractionWindow = (GameInteraction) w;
            }
            if (w instanceof SaveDialog) {
                tempWindow = w;

            } else if (w instanceof InventoryWindow) {


            }
        }
        if (gameInteractionWindow != null) {
            if (Game.world.isInteracting) {
                gameInteractionWindow.nextDialogue();
            } else {
                Game.world.isInteracting = true;
                Window.addGUIWindow(gameInteractionWindow);
            }
        }
        if (tempWindow != null) {
            if (tempWindow.deletThis) {
                Game.gui.windows.remove(tempWindow);
                Game.gui.windows.trimToSize();
            }
        }
        if (Game.world.player != null) {
            Game.world.player.onActionKey();
        }
    }

    void onBackKey() {
        for (gui.window.Window w : Game.gui.windows) {
            if (w instanceof InventoryWindow) {
                ((InventoryWindow) w).hasSelected = false;
            }
        }
        if (Game.gui instanceof GUIBattle) {

        }

    }

    public void onStart() {

    }

    public void initializeProperties() {

    }

    public void enterPerfomed(GUIComponent guiComponent) {
    }

    public void keyPerformed(GUIComponent guiComponent, KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (selectedComponent != null) {
            selectedComponent.onKey(e);
        } else {
            if (!keyStrokes.contains((Integer) e.getKeyCode()) && !keyStrokesHolding.contains((Integer) e.getKeyCode()))
                keyStrokes.add(e.getKeyCode());
            keyStrokesHolding.add(e.getKeyCode());
            System.out.println("Key added");

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (selectedComponent == null) {
            keyStrokes.remove((Integer) e.getKeyCode());
            keyStrokesHolding.remove((Integer) e.getKeyCode());
            System.out.println("Key released");
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
        } catch (CloneNotSupportedException e) {
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
        for (Window window : Game.gui.windows) {
            if (!clickedOnSomething) {
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
        for (GUIComponent guiComponent : clickedGUIComponents) {
            guiComponent.onClick(e);
        }
        if (selectedWindow != null) {
            if (selectedWindow.deletThis) {
                Game.gui.windows.remove(selectedWindow);
                Game.gui.windows.trimToSize();
                selectedWindow = null;
            }
        }
        if (clickedOnSomething) return;
        if (!clickedOnSomething) selectedComponent = null;
        for (Window window : Game.gui.windows) {
            for (GUIComponent guiComponent : window.guiComponents) {
                if (guiComponent.contentHeight > guiComponent.height) {
                    if (guiComponent.scrollBarBounds.intersects(new Rectangle(mouseX, mouseY, 1, 1))) {

                        clickedOnSomething = false;
                    }
                }
            }
        }
        if (clickedOnSomething) return;
        for (GUIComponent guiComponent : Game.gui.guiComponents) {
            if (guiComponent.bounds.intersects(new Rectangle(mouseX, mouseY, 1, 1))) {
                clickedGUIComponents.add(guiComponent);
                clickedOnSomething = true;
            }
        }
        for (GUIComponent guiComponent : clickedGUIComponents) {
            guiComponent.onClick(e);
        }
        if (clickedOnSomething) return;
        if (!holding) {
            boundsType = 0;
            for (Window window : Game.gui.windows) {
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
            for (Window window : Game.gui.windows) {
                for (GUIComponent guiComponent : window.guiComponents) {
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
        if (!clickedOnSomething) {
            onClick(e);
        }
    }

    void onClick(MouseEvent e) {

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
