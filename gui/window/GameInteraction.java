package gui.window;

import battle.Battle;
import game.Game;
import gui.ImageComponent;
import item.Item;
import npc.NPC;
import tiles.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;


public class GameInteraction extends Window {

    public Object target;
    public Tile targetTile;
    public ArrayList dialogueText;
    public int number;
    int dialogueId = 0;
    int charaDelay = 200;
    private String text = "";
    private String renderedText = "";
    public long timeSinceLastCharacter;
    private int dialogueImageId = 0;
    public WindowCallback callback;
    public ArrayList<String> textLines = new ArrayList<String>();
    public ArrayList<String>  renderedTextLines = new ArrayList<String>();
    public int alreadyRendered = 0;

    public GameInteraction(int id, boolean actionBarEnabled, Object target) {
        super(id, actionBarEnabled);
        this.target = target;
    }

    public GameInteraction(int id, int x, int y, int width, int height, boolean actionBarEnabled, Object target) {
        super(id, x, y, width, height, actionBarEnabled);
        this.target = target;
        if(target instanceof NPC) {
            number = 0;
            NPC targetNPC = (NPC) target;
            this.text = targetNPC.npcDialogue.dialogueText.get(dialogueId);
            this.dialogueText = targetNPC.npcDialogue.dialogueText;
            this.dialogueImageId = 0;
            if (targetNPC.npcDialogue.dialogueText.get(dialogueId).contains("\b")) {

                Game.world.startBattle(new Battle(new ArrayList<NPC>() {{
                    add(targetNPC);
                }}));
                renderedText = "";
                Game.gui.removeGUIWindow(this);
            }
            if (targetNPC.npcDialogue.dialogueText.get(dialogueId).contains(";img")) {
                dialogueImageId = Integer.parseInt(targetNPC.npcDialogue.dialogueText.get(dialogueId).split(";")[1].replace(";img", "").replace(";", ""));

                ((ImageComponent) getComponentById(-337)).image = targetNPC.dialogueImages.get(dialogueImageId);
            }

            addComponent(new ImageComponent(-337, 0, -15, (int) (56 * 2), (int) (46 * 2), targetNPC.dialogueImages.get(dialogueImageId), null, false));
        } else if (target instanceof Item){
            number = 2;
            Item targetItem = (Item) target;
            this.text = targetItem.description;
            this.dialogueText = new ArrayList<String>(){{add(text);}};
        } else if (target instanceof String){
            number = 3;
            this.text = (String)target;
            this.dialogueText = new ArrayList<String>(){{add(text);}};
        }
        int stringwidth = 0;
        int textline = 0;
        textLines.add("");
        for(String s : text.split(" ")){

            stringwidth += Game.gamePanel.getFontMetrics(new Font("Determination Mono", Font.BOLD, 25)).stringWidth(s);
            if (stringwidth > 300) {
                stringwidth = Game.gamePanel.getFontMetrics(new Font("Determination Mono", Font.BOLD, 25)).stringWidth(s);
                textLines.set(textline, textLines.get(textline).substring(0, textLines.get(textline).length() - 1));
                textline++;
                textLines.add("");
                textLines.set(textline, s + (s.equals(text.split(" ")[text.split(" ").length - 1]) ? "" : " "));

            } else {
                textLines.set(textline, textLines.get(textline) + s + (s.equals(text.split(" ")[text.split(" ").length - 1]) ? "" : " "));
            }

        }
        for(int i = 0; i < textLines.size(); i++){
            renderedTextLines.add("");
        }
    }

    public GameInteraction(int id, int x, int y, int width, int height, boolean actionBarEnabled, Tile target, WindowCallback callback) {
        super(id, x, y, width, height, actionBarEnabled);
        number = 2;
        this.targetTile = target;
        this.text = targetTile.npcDialogue.dialogueText.get(dialogueId);
        this.dialogueText = targetTile.npcDialogue.dialogueText;
        this.dialogueImageId = 0;
        if(target.npcDialogue.dialogueText.get(dialogueId).contains(";img")){
            dialogueImageId = Integer.parseInt(target.npcDialogue.dialogueText.get(dialogueId).split(";")[1].replace(";img", "").replace(";", ""));

        }
        this.callback = callback;
        int stringwidth = 0;
        int textline = 0;
        textLines.add("");
        for(String s : text.split(" ")){

            stringwidth += Game.gamePanel.getFontMetrics(new Font("Determination Mono", Font.BOLD, 25)).stringWidth(s);
            if (stringwidth > 600) {
                stringwidth = Game.gamePanel.getFontMetrics(new Font("Determination Mono", Font.BOLD, 25)).stringWidth(s);
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

    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        if(!renderedTextLines.get(alreadyRendered).equals(textLines.get(alreadyRendered))) {
            if (timeSinceLastCharacter + 50 < System.currentTimeMillis()) {
                timeSinceLastCharacter = System.currentTimeMillis();
                renderedTextLines.set(alreadyRendered, renderedTextLines.get(alreadyRendered) + textLines.get(alreadyRendered).charAt(renderedTextLines.get(alreadyRendered).length()));
            }
        } else if (renderedTextLines.get(alreadyRendered).equals(textLines.get(alreadyRendered)) && renderedTextLines.size() - 1 > alreadyRendered){
            alreadyRendered++;
        }

        g2d.setFont(new Font(g2d.getFont().getName(), g2d.getFont().getStyle(), 25));
        for(int i = 0; i < renderedTextLines.size(); i++) {
            g2d.drawString(renderedTextLines.get(i), x + 150, y + 40 + (i * 50));
        }
        g2d.setFont(new Font(g2d.getFont().getName(), g2d.getFont().getStyle(), 12));
    }



    public void nextDialogue(){
        alreadyRendered =0;
        dialogueId++;
        renderedText = "";
        renderedTextLines = new ArrayList<>();
        textLines = new ArrayList<String>();
        if(number == 0) {
            NPC targetNPC = (NPC) target;
            if (targetNPC.npcDialogue.dialogueText.size() < dialogueId + 1) {
                Game.world.isInteracting = false;

                Game.gui.removeGUIWindow(this);
            } else {
                text = targetNPC.npcDialogue.dialogueText.get(dialogueId);

                if (targetNPC.npcDialogue.dialogueText.get(dialogueId).contains("\b")) {

                    Game.world.startBattle(new Battle(new ArrayList<NPC>() {{
                        add(targetNPC);
                    }}));
                    renderedText = "";
                    Game.gui.removeGUIWindow(this);
                }
                if (targetNPC.npcDialogue.dialogueText.get(dialogueId).contains(";img")) {
                    dialogueImageId = Integer.parseInt(targetNPC.npcDialogue.dialogueText.get(dialogueId).split(";")[1].replace("img", "").replace(";", ""));
                    text = text.replace(";img" + dialogueImageId + ";", "");

                    ((ImageComponent) getComponentById(-337)).image = targetNPC.dialogueImages.get(dialogueImageId);
                }
                int stringwidth = 0;
                int textline = 0;
                textLines.add("");
                ArrayList<String> hm = new ArrayList<String>(Arrays.asList(text.split(" ")));
                while(hm.remove(""));
                while(hm.remove(" "));
                for(String s : hm){

                    stringwidth += Game.gamePanel.getFontMetrics(new Font("Determination Mono", Font.BOLD, 25)).stringWidth(s);

                    if (stringwidth > 600) {
                        stringwidth = Game.gamePanel.getFontMetrics(new Font("Determination Mono", Font.BOLD, 25)).stringWidth(s);
                        textLines.set(textline, textLines.get(textline).substring(0, textLines.get(textline).length() - 1));
                        textline++;
                        textLines.add("");
                        textLines.set(textline, s + (s.equals(hm.get(hm.size() - 1)) ? "" : " "));

                    } else {
                        textLines.set(textline, textLines.get(textline) + s + (s.equals(hm.get(hm.size() - 1))  ? "" : " "));
                    }

                }
                for(int i = 0; i < textLines.size(); i++){
                    renderedTextLines.add("");
                }
            }
        } else if (number == 1){
            Tile target = targetTile;
            if (target.npcDialogue.dialogueText.size() < dialogueId + 1) {
                Game.world.isInteracting = false;

                Game.gui.removeGUIWindow(this);
                callback.onEvent("savePrompt");
            } else {
                text = target.npcDialogue.dialogueText.get(dialogueId);
            }
        } else if (number == 2 || number == 3){

            Game.world.isInteracting = false;

            Game.gui.removeGUIWindow(this);
        }
    }
}
