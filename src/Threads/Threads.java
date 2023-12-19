package Threads;

import BattleField.BattleField;
import Cards.Card;
import Cards.DeckManager;
import Cards.Doors.Classes.ClassCard;
import Cards.Doors.Curses.CurseCard;
import Cards.Doors.DoorCard;
import Cards.Doors.Monster.Monster;
import Cards.Doors.Races.RaceCard;
import Cards.Treasures.OneTimePlayCards.OneTimeTreasureCard;
import Cards.Treasures.TreasureCard;
import Cards.Treasures.WearableCards.WearableTreasureCard;
import Game.Player;
import Loggers.Log;
import Person.Person;

import java.util.ArrayList;
import java.util.List;

import static Cards.Card.ShirtType.DOOR;
import static Cards.Doors.DoorCard.Type.*;
import static Cards.Treasures.TreasureCard.Type.ONE_TIME;
import static Cards.Treasures.TreasureCard.Type.WEARABLE;

public class Threads implements Runnable {

    private final Long timeGo;  //в секундах
    private final Long doptimeGo;  //в секундах
    private final Player player;
    private BattleField battleField = new BattleField();
    private final List<DoorCard> leaveDoor = new ArrayList<>();
    private List<OneTimeTreasureCard> treasureCards = new ArrayList<>();

    public Threads(Player pers) {
        this.player = pers;
        this.timeGo = (long) (1 * 1000);
        this.doptimeGo = (long) (1 * 1000);
    }

    public void setBattleField (BattleField battle) {this.battleField = battle;}

    public void run() {
        long startTime = System.currentTimeMillis();

        if (Thread.currentThread().getName().equals("btg")) {
            while (System.currentTimeMillis() < startTime + timeGo) {
                int i = -1;                  // TODO: карта, которую выбрал игрок
                if (i >= 0 && i <= this.player.getCards().size()) {
                    var card = this.player.getCard(i);

                    switch (card.getShirtType()) {
                        case DOOR -> {
                            var card_door = (DoorCard) card;

                            switch (card_door.getType()) {
                                case CLASS -> {
                                    var Class = (ClassCard) card_door;
                                    Class.Play(this.player.getPerson());
                                    player.putOn(i);
                                }
                                case RACE -> {
                                    var rase = (RaceCard) card_door;
                                    rase.Play(this.player.getPerson());
                                    player.putOn(i);
                                }
                            }
                        }
                        case TREASURE -> {
                            var card_treasure = (TreasureCard) card;

                            if (card_treasure.getType() == WEARABLE) {
                                var armor = (WearableTreasureCard) card_treasure;
                                armor.Play(this.player.getPerson());
                                player.putOn(i);
                            }
                            if (card_treasure.getType() == ONE_TIME) {
                                var armor = (OneTimeTreasureCard) card_treasure;
                                armor.Play(this.player.getPerson());
                                treasureCards.add(armor);
                            }
                        }
                    }
                }
            }
        }
        if (Thread.currentThread().getName().equals("dudos")) {
            while (System.currentTimeMillis() < startTime + timeGo) {
                boolean help = false;           // TODO: помощь или дудос
                int i = -1;                  // TODO: карта, которую выбрал игрок
                if (i >= 0 && i <= this.player.getCards().size()) {
                    var card = this.player.getCard(i);

                    if (!help) {
                        if (card.getShirtType() == DOOR) {
                            var doorCard = (DoorCard) card;
                            if (doorCard.getType() == MONSTER) {
                                Monster monster = (Monster) card;
                                battleField.MonsterSide.Participants.add(monster);
                                monster.Play(player.getPerson());
                                leaveDoor.add(monster);
                            } else if (doorCard.getType() == CURSE) {
                                Log.fmtGlobLog("ПРИМЕНЕНО ПРОКЛЯЕТЕ НА ИГРОКА\n");
                                var curse = (CurseCard) doorCard;
                                curse.Play(player.getPerson());
                                player.PlayCurseCard(curse);
                            }
                        }

                    } else {
                        battleField.PlayerSide.Participants.add(player.getPerson());
                    }
                }
            }
        }
        if (Thread.currentThread().getName().equals("endDudos")) {
            for (DoorCard l : leaveDoor) {
                l.Leave(player.getPerson());
                Monster monster = (Monster) l;
                monster.ClearBonuses();
            }
        }
        if (Thread.currentThread().getName().equals("cardReset")) {
            for (int i = 0; i < treasureCards.size(); ++i)
                treasureCards.get(i).Leave(player.getPerson());
            while (System.currentTimeMillis() < startTime + timeGo) {
                reset();
            }
            if (player.getCards().size() > 5 && player.getPerson().getRace() != Person.Race.dwarf) {//доп время для затупков
                while (System.currentTimeMillis() < startTime + doptimeGo) {
                    reset();
                }
            }
            if (player.getCards().size() > 6 && player.getPerson().getRace() == Person.Race.dwarf) {
                reset();
            }
        }
    }

    public void reset () {
        boolean shmot = true;       //TODO: что скинуть? шмот или карты с руки?
        int i = -1; // TODO: карта, которую выбрал игрок

        if (shmot) {

            if (i >= 0 && i <= this.player.getShmots().size()) {
                Card card = player.getShmot(i);
                var treasureCard = (TreasureCard) card;

                if (treasureCard.getType() == WEARABLE) {
                    var armor = (WearableTreasureCard) treasureCard;
                    armor.Leave(this.player.getPerson());
                    player.takeOff(i);
                }
            }

        } else {
            if (i >= 0 && i <= this.player.getCards().size())
                player.dropCard(player.getCard(i));
        }
    }

}