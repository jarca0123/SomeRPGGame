package gui;

import java.awt.*;
import java.awt.image.BufferedImage;


public class ImageComponent extends GUIComponent {
    public BufferedImage image;
    private BufferedImage image1;
    public boolean isSelected;
    private boolean selectable = false;

    public ImageComponent(int id, int x, int y, int width, int height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int leftMargin, int rightMargin, int upMargin, int downMargin, BufferedImage image, BufferedImage image1) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown, leftMargin, rightMargin, upMargin, downMargin);
        this.image = image;
        this.image1 = image1;
    }

    public ImageComponent(int id, double x, double y, double width, double height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int leftMargin, int rightMargin, int upMargin, int downMargin, BufferedImage image, BufferedImage image1) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown, leftMargin, rightMargin, upMargin, downMargin);
        this.image = image;
        this.image1 = image1;
    }

    public ImageComponent(int id, int x, int y, int width, int height, BufferedImage image, BufferedImage image1, boolean selectable) {
        super(id, x, y, width, height);
        this.image = image;
        this.image1 = image1;
        this.selectable = selectable;
    }



    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);





        if(selectable) {
            if (isSelected) {
                g2d.drawImage(image1, x, y, width, height, null);
            } else {
                g2d.drawImage(image, x, y, width, height, null);
            }
        } else {
            g2d.drawImage(image, x, y, width, height, null);


        }
    }
}
