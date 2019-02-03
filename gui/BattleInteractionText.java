package gui;

import game.Game;
import graphics.JGraphics2D;

import java.awt.*;
import java.util.ArrayList;


public class BattleInteractionText extends GUIComponent{
    public int fontSize;
    public String text;
    public boolean center;
    public Font font;

    public long timeSinceLastCharacter;
    public ArrayList<String> textLines = new ArrayList<String>();
    public ArrayList<String>  renderedTextLines = new ArrayList<String>();
    public int alreadyRendered = 0;

    public BattleInteractionText(int id, String text, boolean center, int fontSize) {
        super(id);
        this.text = text;
        this.center = center;
        this.fontSize = fontSize;
    }





    public BattleInteractionText(int id, double x, double y, double width, double height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int leftMargin, int rightMargin, int upMargin, int downMargin, String text, boolean center, int fontSize) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown, leftMargin, rightMargin, upMargin, downMargin);
        this.text = text;


        this.center = center;
        this.fontSize = fontSize;
        this.font = new Font("Determination Mono", Font.BOLD, fontSize);

    }


    @Override
    public void initForWindow() {
        int stringwidth = 0;
        int textline = 0;
        super.initForWindow();
        renderedTextLines = new ArrayList<>();
        textLines.add("");
        for(String s : text.split(" ")){

            stringwidth += Game.gamePanel.getFontMetrics(new Font("Determination Mono", Font.BOLD, fontSize)).stringWidth(s);
            if (stringwidth > 450) {
                stringwidth = Game.gamePanel.getFontMetrics(new Font("Determination Mono", Font.BOLD, fontSize)).stringWidth(s);
                textLines.set(textline, textLines.get(textline).substring(0, textLines.get(textline).length() - 1));
                textline++;
                textLines.add("");
                textLines.set(textline, textLines.get(textline) + s + (s.equals(text.split(" ")[text.split(" ").length - 1]) ? "" : " "));

            } else {
                textLines.set(textline, textLines.get(textline) + s + (s.equals(text.split(" ")[text.split(" ").length - 1]) ? "" : " "));
            }

        }
        for(int i = 0; i < textLines.size(); i++){
            renderedTextLines.add("");
        }
        for(String s : textLines) {
            System.out.println(s);
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);

        g2d.setColor(Color.white);



        g2d.setFont(new Font("Determination Mono", Font.BOLD, fontSize));
        g2d.setColor(Color.BLACK);
        if(!renderedTextLines.get(alreadyRendered).equals(textLines.get(alreadyRendered))) {
            if (timeSinceLastCharacter + 25 < System.currentTimeMillis()) {
                timeSinceLastCharacter = System.currentTimeMillis();
                renderedTextLines.set(alreadyRendered, renderedTextLines.get(alreadyRendered) + textLines.get(alreadyRendered).charAt(renderedTextLines.get(alreadyRendered).length()));
            }
        } else if (renderedTextLines.get(alreadyRendered).equals(textLines.get(alreadyRendered)) && renderedTextLines.size() - 1 > alreadyRendered){
            alreadyRendered++;
        }
        g2d.setColor(Color.WHITE);
        for(int i = 0; i < renderedTextLines.size(); i++) {
            g2d.drawString(renderedTextLines.get(i), x +10, y + 30 + (i * 50));



        }
    }
}
