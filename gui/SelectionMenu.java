package gui;

import java.awt.*;
import java.util.ArrayList;

public class SelectionMenu extends GUIComponent {
    public int selectedId = 1;
    public ArrayList<InteractionText> selections = new ArrayList<>();
    private int numberOfSelections;
    private int pages;
    private int page = 1;

    public SelectionMenu(int id, int numberOfSelections, int pages) {
        super(id);
        this.numberOfSelections = numberOfSelections;
        this.pages = pages;
    }

    public SelectionMenu(int id, double x, double y, double width, double height, int numberOfSelections, int pages) {
        super(id, x, y, width, height);
        this.numberOfSelections = numberOfSelections;
        this.pages = pages;
    }

    public SelectionMenu(int id, double x, double y, double width, double height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int numberOfSelections, int pages) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown);
        this.numberOfSelections = numberOfSelections;
        this.pages = pages;
    }

    public SelectionMenu(int id, double x, double y, double width, double height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int leftMargin, int rightMargin, int upMargin, int downMargin, int numberOfSelections, int pages) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown, leftMargin, rightMargin, upMargin, downMargin);
        this.numberOfSelections = numberOfSelections;
        this.pages = pages;
    }


    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        if ((selectedId / (numberOfSelections / pages)) + 1 != page) {
            page = (selectedId / (numberOfSelections / pages)) + 1;
        }
        for (InteractionText text : selections) {
            if (selections.indexOf(text) / (numberOfSelections / pages) + 1 == page || pages == 1) {
                if (text.text != "")
                    text.draw(g2d);
            }
        }
    }

    public void addSelection(InteractionText text) {
        text.windowParent = windowParent;
        text.actualX += x;
        text.actualY += y;
        selections.add(text);
    }

    public void setTextOfSelections(ArrayList<String> list) {
        for (String s : list) {
            selections.get(list.indexOf(s)).text = s;
        }
    }
}
