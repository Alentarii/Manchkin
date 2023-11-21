package Person;

import Gear.*;
import Common.Selectable;

import java.util.ArrayList;

public class Person implements Selectable {
    public enum Race {    //раса персонажа
        elf,            //эльф
        halfling,       //халфлинг
        dwarf,          //дварф
        half_breed,     //полукровка
        human           //человек
        ;
    }

    public enum Class {  //класс персонажа
        cleric,         //клирик
        wizard,         //волшебкик
        thief,          //вор
        warrior,        //воин
        none            //пусто
        ;
    }

    @Override
    public void Select() {}

    @Override
    public void ChangePower(int power_changes) {
        power += power_changes;
    }

    public Race getRace() {
        return race;
    }

    public int getLevel() {
        return level;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Class getCur_class() {
        return cur_class;
    }

    public void setCur_class(Class cur_class) {
        this.cur_class = cur_class;
    }

    public void Calculate_Total_Damage() { //высчитывает общий урон
        int temp = 0;
        temp += this.level;

        for (var w : this.weapons)
            temp += w.getPower();
        for (var o : this.others)
            temp += o.getPower();

        temp += helmet != null ? helmet.getPower() : 0;
        temp += body != null ? body.getPower() : 0;
        temp += legs != null ? legs.getPower() : 0;

        temp += this.power;

        this.total_damage = temp;
    }

    public void decreaseLevel(int points) {
        // понизить уровень на заданный points, проверить чтобы левел не был ниже начального(1)
        if (level > 1) {
            --level;
        }
    }

    public void increaseLevel(int points) {
        // повысить уровень на заданный points, проверить чтобы левел не был выше 10
        level += points;
    }

    public void resetLevel() {
        // сбросить уровень до начальноого
    }

    @Override
    public Integer GetPower() {
        return total_damage;
    }

    public ArrayList<WeaponGear> weapons = new ArrayList<>(); //оружие в руке
    public int hand_size = 0;
    public ArmorGear helmet;    //шлем
    public ArmorGear body;        //тело
    public ArmorGear legs;        //ноги
    public ArrayList<WearableGear> others = new ArrayList<>(); //другое снаряжение
    Race race;          // текущая раса
    Class cur_class;    // текущий класс
    int level;          // текущий уровень
    private int total_damage;
    private int power;
}