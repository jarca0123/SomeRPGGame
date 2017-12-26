package gui;

import game.Game;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class EnemyBattleInteractionText extends GUIComponent {

    private long timeSinceLastCharacter;
    private ArrayList<String> textLines = new ArrayList<String>();
    private ArrayList<String>  renderedTextLines = new ArrayList<String>();
    private int alreadyRendered = 0;

    public EnemyBattleInteractionText(int id, double x, double y, double width, double height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int leftMargin, int rightMargin, int upMargin, int downMargin) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown, leftMargin, rightMargin, upMargin, downMargin);
    }

    public void nextDialogue(String text){
        int stringwidth = 0;
        int textline = 0;
        renderedTextLines = new ArrayList<>();
        textLines.add("");
        for(String s : text.split(" ")){

            stringwidth += Game.gamePanel.getFontMetrics(new Font("Font Name", Font.BOLD, 12)).stringWidth(s);
            if (stringwidth > 100) {
                stringwidth = Game.gamePanel.getFontMetrics(new Font("Font Name", Font.BOLD, 12)).stringWidth(s);
                textLines.set(textline, textLines.get(textline).substring(0, textLines.get(textline).length() - 1));
                textline++;
                textLines.add("");
                textLines.set(textline, s + (s.equals(text.split(" ")[text.split(" ").length - 1]) ? "" : " "));

            } else {
                textLines.set(textline, s + (s.equals(text.split(" ")[text.split(" ").length - 1]) ? "" : " "));
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

        g2d.setColor(Color.WHITE);
        Path2D.Double triangle = new Path2D.Double();
        triangle.moveTo(actualX + 0, actualY + 30);
        triangle.lineTo(actualX + 30, actualY + 15);
        triangle.lineTo(actualX + 30, actualY + 45);
        triangle.closePath();
        g2d.fill(triangle);
        g2d.fillRect(actualX + 30, actualY, width, height);
        g2d.setFont(new Font("Font Name", Font.BOLD, 12));
        g2d.setColor(Color.BLACK);
        if(!renderedTextLines.get(alreadyRendered).equals(textLines.get(alreadyRendered))) {
            if (timeSinceLastCharacter + 25 < System.currentTimeMillis()) {
                timeSinceLastCharacter = System.currentTimeMillis();
                renderedTextLines.set(alreadyRendered, renderedTextLines.get(alreadyRendered) + textLines.get(alreadyRendered).charAt(renderedTextLines.get(alreadyRendered).length()));
            }
        } else if (renderedTextLines.get(alreadyRendered).equals(textLines.get(alreadyRendered)) && renderedTextLines.size() - 1 > alreadyRendered){
            alreadyRendered++;
        }
        g2d.setColor(Color.BLACK);
        for(int i = 0; i < renderedTextLines.size(); i++) {

            g2d.drawString(renderedTextLines.get(i), x + 40, y + 10 + (i * 20));
        }

    }
}
