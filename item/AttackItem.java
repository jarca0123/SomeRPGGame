package item;


public class AttackItem extends Item{
    public int AT;
    public AttackItem(int id, String name, int at) {
        super(id, name);
        this.AT = at;
    }
}
