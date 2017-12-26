package npc.dialogue;

import java.util.ArrayList;
import java.util.Arrays;


public class Dialogue {
    public ArrayList<String> dialogueText = new ArrayList<>();

    public Dialogue(String rawDialogue){
        parseDialogue(rawDialogue);
    }

    private void parseDialogue(String rawDialogue) {
        dialogueText = new ArrayList<>(Arrays.asList(rawDialogue.split("\n")));
    }
}
