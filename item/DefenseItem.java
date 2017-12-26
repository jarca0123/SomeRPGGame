package item;


public class DefenseItem extends Item {
    private int DF;
    public DefenseItem(int id, String name, int df) {
        super(id, name);
        this.DF = df;
    }
}
