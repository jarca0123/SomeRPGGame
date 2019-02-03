package battle;

import game.Game;
import npc.NPC;


public class BattleLevel {

    public int width;
    public int height;

    public void battleUpdate(){
        for(NPC npc : Game.world.battle.enemies){
            npc.battleLevelUpdate();
        }
    }
}
