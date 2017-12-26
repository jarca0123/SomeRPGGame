package gui;

import java.awt.*;


public class CloseButton extends GUIComponent {
    private String text;
    public boolean isSelected = false;
    public CloseButton(int id) {
        super(id);
        this.text = text;
    }

    public CloseButton(int id, int x, int y, int width, int height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int leftMargin, int rightMargin, int upMargin, int downMargin) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown, leftMargin, rightMargin, upMargin, downMargin);
    }

    public CloseButton(int id, double x, double y, double width, double height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int leftMargin, int rightMargin, int upMargin, int downMargin) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown, leftMargin, rightMargin, upMargin, downMargin);
    }

    public CloseButton(int id, int x, int y, int width, int height) {
        super(id, x, y, width, height);
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);


        if(windowParent != null && id != -1)g2d.setClip(windowParent.windowBounds);
        g2d.setColor(Color.white);
        g2d.fillRect(x, y, width, height);
        g2d.setColor(Color.black);
        drawCenteredString(g2d, "X", new Rectangle(x, y, width, height), g2d.getFont());
    }
}
