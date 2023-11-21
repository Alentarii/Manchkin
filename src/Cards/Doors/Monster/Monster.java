package Cards.Doors.Monster;

import Cards.Doors.DoorCard;
import Common.Selectable;
import Person.Person;

public class Monster extends DoorCard implements Selectable {
    public Monster(Integer level, Integer treasures_count) {
        super(Type.MONSTER);

        this.level = level;
        this.bonus = 0;
        this.treasures_count = treasures_count;
    }

    @Override
    public void Play(Selectable target) {
        if (play != null) {
            play.Play(this, (Person) target);
        }
    }

    @Override
    public void Leave(Selectable target) {
        if (play != null) {
            play.Reverse(this, (Person) target);
        }
    }

    public void Catch(Selectable target) {
        if (catch_up != null) {
            catch_up.Catch((Person) target);
        }
    }

    @Override
    public Integer GetPower() {
        return level + bonus;
    }

    public Integer getTreasuresCount() {
        return treasures_count;
    }

    public void changeTreasureCount(int change) {
        treasures_count += change;
    }

    public void setPlay(MonsterPlay play) {
        this.play = play;
    }

    public void setCatchUp(MonsterCatch catch_up) {
        this.catch_up = catch_up;
    }

    @Override
    public void Select() {
    }

    @Override
    public void ChangePower(int power_changes) {
        AddBonus(power_changes);
    }

    public void AddBonus(Integer bonus) {
        this.bonus += bonus;
    }

    public void ClearBonuses() {
        this.bonus = 0;
    }

    private final Integer level;

    private Integer bonus;
    private Integer treasures_count;

    private MonsterPlay play = null;      // проверка условий (возможно что класс/раса персонажа дадут монстру бонус)

    private MonsterCatch catch_up = null; // непотребство
}
