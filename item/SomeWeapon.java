package item;


public class SomeWeapon extends AttackItem {

    public SomeWeapon(int id, String name) {
        super(id, name, 9001);
    }

    @Override
    public void onConsume() {
        super.onConsume();
    }
}
