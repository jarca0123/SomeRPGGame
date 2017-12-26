package npc.action;

import npc.NPC;

class Action {
    public int id;
    public String action;
    private NPC parentNpc;

    public Action(NPC parentNpc){
        this.parentNpc = parentNpc;
    }
}
