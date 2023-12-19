package Game;

import BattleField.BattleField;
import Cards.Card;
import Cards.DeckManager;
import Cards.Doors.Curses.CurseCard;
import Cards.Doors.DoorCard;
import Cards.Doors.Monster.Monster;
import Loggers.Log;
import Threads.Threads;

import java.util.ArrayList;
import java.util.List;

import static Cards.Doors.DoorCard.Type.CURSE;
import static Cards.Doors.DoorCard.Type.MONSTER;

public class Game {
    private List<Player> people;

    private ThreadGroup getDressedBeforeTheGame;

    private ThreadGroup helpOrFolder;

    private Thread cardReset;

    public Game (List <Player> peop) {this.people = peop;}

    public List<Player> getPeople () {return this.people;}

    public void goGetDressedBeforeTheGame () {
        getDressedBeforeTheGame = new ThreadGroup("before_the_game");
        List <Thread> beforeTheGameThreads = new ArrayList<>(people.size());
        for (Player p : people) {
            beforeTheGameThreads.add(new Thread(getDressedBeforeTheGame, new Threads(p), "btg"));
        }

        for (Thread t : beforeTheGameThreads)
            t.start();

        while (getDressedBeforeTheGame.activeCount() != 0) {
            Thread.yield();
        }
    }

    public void goHelpOrFolder(int index, BattleField battleField) {
        helpOrFolder = new ThreadGroup("before_the_game");
        List <Thread> beforeTheGameThreads = new ArrayList<>(people.size() - 1);
        for (int i = 0; i < people.size(); ++i) {
            if (i != index) {
                Threads t = new Threads(people.get(i));
                t.setBattleField(battleField);
                beforeTheGameThreads.add(new Thread(helpOrFolder, t, "dudos"));
            }
        }

        for (Thread t : beforeTheGameThreads)
            t.start();

        while (helpOrFolder.activeCount() != 0) {
            Thread.yield();
        }
    }

    public void endDudos(int index, BattleField battleField) {
        helpOrFolder = new ThreadGroup("endDudos");
        List <Thread> beforeTheGameThreads = new ArrayList<>(people.size() - 1);
        for (int i = 0; i < people.size(); ++i) {
            if (i != index) {
                Threads t = new Threads(people.get(i));
                t.setBattleField(battleField);
                beforeTheGameThreads.add(new Thread(helpOrFolder, t, "endDudos"));
            }
        }

        for (Thread t : beforeTheGameThreads)
            t.start();

        while (helpOrFolder.activeCount() != 0) {
            Thread.yield();
        }
    }

    public void goCardReset (int index) {
        cardReset = new Thread(new Threads(people.get(index)), "cardReset");
        cardReset.start();
        while (cardReset.isAlive()) {
            Thread.yield();
        }
    }

    public void openTheDoor (Card c, BattleField battleField, int index) {
        var card = (DoorCard) c;
        if (card.getType() == MONSTER) {
            battleField.PlayerSide.Participants.add(people.get(index).getPerson());
            Monster monster = (Monster) card;
            battleField.MonsterSide.Participants.add(monster);
            if (battleField.MonsterSide.Participants.size() != 0)
                Log.fmtGlobLog("МОНСТР НА ПОЛЕ\n");
            else
                Log.fmtGlobLog("МОНСТР НЕ ВЫШЕЛ\n");
            monster.Play(people.get(index).getPerson());
            Log.fmtGlobLog("БОЙ НАЧАЛСЯ\n");
            //тут или накидываем, либо помогаем
            this.goHelpOrFolder(index, battleField);

            Log.fmtGlobLog("ОБЩАЯ СИЛА СТОРОНЫ МОНСТРА: " + battleField.MonsterSide.GetPower() + "\n");
            Log.fmtGlobLog("ОБЩАЯ СИЛА СТОРОНЫ ИГРОКА: " + battleField.PlayerSide.GetPower() + "\n");
            if (battleField.MonsterSide.GetPower() > battleField.PlayerSide.GetPower()) {
                Log.fmtGlobLog("ИГРОКА " + index + " ПОБЕДИЛ МОНСТР\n");
                monster.Leave(people.get(index).getPerson());
                this.endDudos(index, battleField);
                monster.Catch(people.get(index).getPerson());
                monster.ClearBonuses();
            }
            else {
                Log.fmtGlobLog("ПОБЕДИЛ ИГРОК: " + index + "\n");
                monster.Leave(people.get(index).getPerson());
                this.endDudos(index, battleField);
                int count = monster.getTreasuresCount();
                people.get(index).takeTheReward(count);
                monster.ClearBonuses();
            }

        } else if (card.getType() == CURSE) {
            Log.fmtGlobLog("ПРИМЕНЕНО ПРОКЛЯЕТЕ НА ИГРОКА: " + index + "\n");
            var curse = (CurseCard) c;
            curse.Play(people.get(index).getPerson());
            people.get(index).PlayCurseCard(curse);
        } else {
            Log.fmtGlobLog("КАРТА ВЗЯТА В РУКУ. ИГРОК: " + index + "\n");
            people.get(index).addCard(c);
        }
    }

    public void lookingForTrouble (BattleField battleField, int index) {
        boolean draw_a_card = true; // TODO: вытягиваем карту или играем с руки?

        if (draw_a_card) {
            DeckManager deckManager = new DeckManager();
            var doorDeck = deckManager.getDoorDeck();
            if (doorDeck.isEmpty()) {
                Log.fmtGlobLog("КОЛОДА ПУСТА\n");
            } else {


                if (doorDeck.isEmpty()) {
                    Log.fmtGlobLog("КОЛОДА ПУСТА\n");
                } else {
                    Log.fmtGlobLog("БЕРЕМ КАРТУ ИЗ КОЛОДЫ ДВЕРЕЙ\n");
                    var card = doorDeck.pullCard(); // TODO: вытянуть одну карту
                    this.openTheDoor(card, battleField, index);
                }

            }
        } else {
            //деремся с монстром из руки
            int i = -1;                  // TODO: карта, которую выбрал игрок
            if (i >= 0 && i <= people.get(index).getCards().size()) {
                var card = people.get(index).getCard(i);
                var c = (DoorCard) card;
                if (c.getType() == MONSTER) {
                    Log.fmtGlobLog("БОРЕМСЯ С МОНСТРОМ ИЗ РУКИ\n");
                    this.openTheDoor(card, battleField, index);
                }
            }
        }
    }
}