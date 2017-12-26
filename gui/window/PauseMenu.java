package gui.window;

import gui.InteractionText;
import gui.SelectionMenu;


class PauseMenu extends Window {
    public PauseMenu(int id, boolean actionBarEnabled) {
        super(id, actionBarEnabled);
    }
    private SelectionMenu selectionMenu;
    public PauseMenu(int id, int x, int y, int width, int height, boolean actionBarEnabled) {
        super(id, x, y, width, height, actionBarEnabled);
        selectionMenu = new SelectionMenu(-1731711, 0, 0, width, height - 100, null, null, null, null, 0, 0, 0, 0, 8, 1);
        addComponent(selectionMenu);

    }

}
