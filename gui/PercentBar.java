package gui;

import java.awt.*;


public class PercentBar extends GUIComponent {
    private Color backgroundColor;
    private Color foregroundColor;
    public int percent = 100;

    public PercentBar(int id) {
        super(id);
    }

    public PercentBar(int id, double x, double y, double width, double height) {
        super(id, x, y, width, height);
    }

    public PercentBar(int id, double x, double y, double width, double height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown);
    }

    public PercentBar(int id, double x, double y, double width, double height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int leftMargin, int rightMargin, int upMargin, int downMargin, Color backgroundColor, Color foregroundColor) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown, leftMargin, rightMargin, upMargin, downMargin);
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        g2d.setColor(Color.white);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(x, y, width, height, 10, 10);
        g2d.setColor(backgroundColor);
        g2d.fillRect(x, y, width, height);
        g2d.setColor(foregroundColor);
        g2d.fillRect(x, y, (int)(float)((float)percent / (float)100 * (float)width), height);
        g2d.setStroke(new BasicStroke(1));
    }
}
