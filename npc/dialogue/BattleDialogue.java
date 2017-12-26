package npc.dialogue;

import game.Game;
import gui.JustText;

import java.util.ArrayList;
import java.util.Arrays;


public class BattleDialogue {
    public ArrayList<String> dialogueText = new ArrayList<>();
    public ArrayList<String> infoDialogueText = new ArrayList<>();
    public String enemyDialogueText = "";


    public BattleDialogue(String rawDialogue){
        parseDialogue(rawDialogue);
    }

    public void parseDialogue(String rawDialogue) {
        enemyDialogueText = "* " + rawDialogue;

    }


}
