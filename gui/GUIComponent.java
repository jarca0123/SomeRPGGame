package gui;

import game.Game;

import java.awt.*;
import java.awt.event.KeyEvent;


public class GUIComponent {
    public GUIComponent componentToAlign;
    public int id;
    public int x;
    public double xPercentage;
    public int y;
    public double yPercentage;
    public int actualX;
    public int actualY;
    public int width;
    public double widthPercentage;
    public int height;
    public double heightPercentage;
    public int actualWidth;
    public int actualHeight;
    public Rectangle bounds;
    public boolean visible = true;
    public boolean enabled = true;
    public gui.window.Window windowParent;
    public Object alignLeft;
    public Object alignRight;
    public Object alignUp;
    public Object alignDown;
    public int leftMargin;
    public int rightMargin;
    public int upMargin;
    public int downMargin;
    public Rectangle marginBounds;
    public int contentHeight = 0;
    public int contentWidth = 0;
    public int scrollBarX = 0;
    public int scrollBarY = 0;
    public int scrollBarHeight = 0;
    public int scrollBarWidth = 0;
    public int contentOffsetX = 0;
    public int contentOffsetY = 0;
    public int scrollableHeight;
    public Rectangle scrollBarBounds;
    public int dx;
    public int dy;
    public boolean isInWindow;
    public int sideOffset = 0;
    public int frameOffset = 0;

    public GUIComponent(int id){
        this(id, Game.GAME_HEIGHT / 2 - (100 / 2), Game.GAME_WIDTH / 2 - (100 / 2), 100, 100);
    }

    public GUIComponent(int id, double x, double y, double width, double height){
        this(id, x, y, width, height, null, null, null, null);
    }

    public GUIComponent(int id, double  x, double  y, double  width, double  height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown){
        this(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown, 0, 0, 0, 0);
    }



    public GUIComponent(int id, double x, double y, double width, double height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int leftMargin, int rightMargin, int upMargin, int downMargin){
        this.id = id;
        if(x < 1 && 0 < x || y < 1 && 0 < y || width < 1 && 0 < width || height < 1 && 0 < height){
            if(x < 1 && 0 < x){
                this.xPercentage = x * 100;
            } else {
                this.x = (int)x;
            }
            if(y < 1 && 0 < y){
                this.yPercentage = y * 100;
            } else {
                this.y = (int)y;
            }
            if(width < 1 && 0 < width){
                this.widthPercentage = width * 100;
            } else {
                this.width = (int)width;
            }
            if(height < 1 && 0 < height){
                this.heightPercentage = height * 100;
            } else {
                this.height = (int)height;
            }
        } else {
            this.x = (int)x;
            this.y = (int)y;
            this.width = (int)width;
            this.height = (int)height;
        }
        this.actualX = this.x;
        this.actualY = this.y;
        this.bounds = new Rectangle(this.x, this.y, this.width, this.height);
        this.alignLeft = alignLeft;
        this.alignRight = alignRight;
        this.alignUp = alignUp;
        this.alignDown = alignDown;
        this.leftMargin = leftMargin;
        this.rightMargin = rightMargin;
        this.upMargin = upMargin;
        this.downMargin = downMargin;
        this.marginBounds = new Rectangle(this.x + leftMargin, this.y + upMargin, this.width - rightMargin + leftMargin, this.height - upMargin + downMargin);
    }



    public void initForWindow(){
        isInWindow = true;
        sideOffset = (int)windowParent.leftFrameBounds.getWidth();
        frameOffset = 20;


    }

    public GUIComponent setEnabled(boolean enabled){
        this.enabled = enabled;
        return this;
    }

    public GUIComponent setVisible(boolean visible){
        this.visible = visible;
        return this;
    }


    public void draw(Graphics2D g2d){
        g2d.setClip(null);
        if(xPercentage != 0){
            actualX = (int)((double)((double)((double)windowParent.windowBounds.getWidth() / (double)100) * (double)xPercentage));
            x = windowParent.x + sideOffset + actualX;
        }
        if(yPercentage != 0){
            actualY = (int)((double)((double)((double)windowParent.windowBounds.getHeight() / (double)100) * (double)yPercentage));
            y = windowParent.y + frameOffset + actualY;
        }
        if(widthPercentage != 0){
            width = (int)((double)((double)((double)windowParent.windowBounds.getWidth() / (double)100) * (double)widthPercentage));
        }
        if(heightPercentage != 0){
            height = (int)((double)((double)(windowParent.windowBounds.getHeight() / 100) * (double)heightPercentage));
        }
        if(windowParent != null) {
            int tempX = actualX + windowParent.x + sideOffset + leftMargin - rightMargin;
            int tempY = actualY + windowParent.y + frameOffset + upMargin - downMargin;
                if (alignLeft != null) {
                    if(alignLeft instanceof gui.window.Window){
                        tempX = windowParent.x + sideOffset + leftMargin;
                    } else {
                        tempX = windowParent.x + sideOffset + leftMargin + ((GUIComponent)alignLeft).actualX + ((GUIComponent)alignLeft).width;
                    }
                }
                if (alignRight != null) {
                    if (alignRight instanceof gui.window.Window) {
                        tempX = windowParent.x - sideOffset + (int)windowParent.windowBounds.getWidth() - width - rightMargin;
                    } else {
                        tempX = ((GUIComponent)alignRight).x - sideOffset + ((GUIComponent)alignRight).width - width - rightMargin;
                    }
                }
                if (alignLeft != null && alignRight != null) {
                    if(alignLeft instanceof GUIComponent && alignRight instanceof gui.window.Window){
                        tempX = ((GUIComponent) alignLeft).x + leftMargin - sideOffset;
                        width = (int)windowParent.windowBounds.getWidth() - ((GUIComponent) alignLeft).actualX - leftMargin - rightMargin;
                    }else if(alignLeft instanceof gui.window.Window && alignRight instanceof GUIComponent){
                        tempX = windowParent.x + sideOffset + leftMargin + actualX;
                        width = ((GUIComponent)alignRight).actualX ;

                    } else if(alignLeft instanceof gui.window.Window && alignRight instanceof gui.window.Window){
                        tempX = windowParent.x + sideOffset + leftMargin - rightMargin + actualX;
                        width = (int)windowParent.windowBounds.getWidth() - leftMargin + rightMargin - actualX;

                    } else if(alignLeft instanceof GUIComponent && alignRight instanceof GUIComponent){
                        tempX = windowParent.x + sideOffset + ((GUIComponent) alignLeft).actualX + leftMargin;
                        width = (int)windowParent.windowBounds.getWidth() - (sideOffset * 2) - ((GUIComponent) alignLeft).actualX - ((GUIComponent) alignRight).width - rightMargin;
                    }
                }
            if (alignUp != null) {
                if(alignUp instanceof gui.window.Window){
                    tempY = windowParent.y + frameOffset + upMargin;
                } else {
                    tempY = windowParent.y + frameOffset + upMargin + ((GUIComponent)alignUp).actualY + ((GUIComponent)alignUp).height;
                }
            }
            if (alignDown != null) {
                if (alignDown instanceof gui.window.Window) {
                    tempY = windowParent.y - frameOffset + (int)windowParent.windowBounds.getHeight() - height - downMargin;
                } else {
                    tempY = ((GUIComponent)alignDown).y - frameOffset + ((GUIComponent)alignDown).height - height - downMargin;
                }
            }
            if (alignUp != null && alignDown != null) {
                if(alignUp instanceof GUIComponent && alignDown instanceof gui.window.Window){
                    tempY = ((GUIComponent) alignUp).y + upMargin - frameOffset;
                    height = (int)windowParent.windowBounds.getHeight() - ((GUIComponent) alignUp).actualY - upMargin - downMargin;
                }else if(alignUp instanceof gui.window.Window && alignDown instanceof GUIComponent){
                    tempY = windowParent.y + frameOffset + upMargin + actualY;
                    height = ((GUIComponent)alignDown).actualY ;

                } else if(alignUp instanceof gui.window.Window && alignDown instanceof gui.window.Window){
                    tempY = windowParent.y + frameOffset + upMargin - downMargin + actualY;
                    height = (int)windowParent.windowBounds.getHeight() - upMargin + downMargin - actualY;
                } else if(alignUp instanceof GUIComponent && alignDown instanceof GUIComponent){
                    tempY = windowParent.y + frameOffset + ((GUIComponent) alignUp).actualY + upMargin;
                    height = (int)windowParent.windowBounds.getHeight() - (frameOffset * 2) - ((GUIComponent) alignUp).actualY - ((GUIComponent) alignDown).height - downMargin;
                }
            }
            if (alignUp != null && alignDown != null && alignLeft != null && alignRight != null) {
                tempX = windowParent.x + (windowParent.width / 2) - (width / 2);
                tempY = windowParent.y + (windowParent.height / 2) - (height / 2);
            }
            x = tempX + dx;
            y = tempY + dy;
            actualX += dx;
            actualY += dy;
            dx = 0;
            dy = 0;
            bounds = new Rectangle(x, y, width, height);

            g2d.setColor(Color.black);
            g2d.setClip(bounds);
        }
    }



    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {

        FontMetrics metrics = g.getFontMetrics(font);

        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;

        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g.setFont(font);

        g.drawString(text, x, y);
    }

    public void onClick() {
        if(windowParent != null && enabled){
            windowParent.actionPerformed(this);
            return;
        }
        if(enabled) Game.gui.actionPerfomed(this);
    }

    public void onEnter() {

        if(windowParent != null && enabled){
            windowParent.enterPerformed(this);
            return;
        }
        if(enabled) Game.gui.enterPerfomed();
    }

    public void onKey(KeyEvent e) {

        if(windowParent != null && enabled){
            windowParent.keyPerformed(this, e);
            return;
        }
        if(enabled) Game.gui.keyPerformed();
    }

    public void drawOver(Graphics2D g2d) {
        if(contentHeight > height){
            g2d.setClip(null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g2d.setColor(Color.MAGENTA);
            if((int)(double)((double)height * ((double)height / (double)contentHeight)) < 10){
                scrollBarHeight = 10;
                g2d.fillRect(windowParent.x + width - 10, scrollBarY, 20,  scrollBarHeight);
                scrollBarBounds = new Rectangle(windowParent.x + width - 10, scrollBarY, 20, scrollBarHeight);

            } else {
                scrollBarHeight = (int) (double) ((double) height * ((double) height / (double) contentHeight));
                g2d.fillRect(windowParent.x + width - 10, scrollBarY, 20,  scrollBarHeight);
                scrollBarBounds = new Rectangle(windowParent.x + width - 10, scrollBarY, 20, scrollBarHeight);
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }
}
