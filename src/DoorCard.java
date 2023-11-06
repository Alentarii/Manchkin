public abstract class DoorCard {
    public enum Type {
        CURSE,
        MONSTER,
        RACE,
        SPECIALIZATION, // класс персонажа
        BATTLE
    }
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    private Type type;
}
