package npc.action;

import npc.NPC;

public class Action {
    public int id;
    public NPC parentNpc;

    public Action(NPC parentNpc){
        this.parentNpc = parentNpc;
    }
}
