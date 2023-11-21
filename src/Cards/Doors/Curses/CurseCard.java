package Cards.Doors.Curses;

import Cards.Doors.DoorCard;
import Common.Selectable;
import Person.Person;

public class CurseCard extends DoorCard {
    public CurseCard(String name) {
        super(Type.CURSE);
        setName(name);
    }

    @Override
    public void Play(Selectable target) {
        if (target instanceof Person) {
            play.Play(this, (Person) target);
        } else {
            System.out.println("Target not a person");
        }
    }

    @Override
    public void Leave(Selectable target) {
        if (target instanceof Person) {
            play.Leave(this, (Person) target);
        } else {
            System.out.println("Target not a person");
        }
    }

    public void setPlay(CursePlay play) {
        this.play = play;
    }

    private CursePlay play;
}