
import BattleField.BattleField;
import Cards.DeckManager;
import Cards.Doors.DoorCard;
import Game.Game;
import Game.Player;
import Loggers.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CheckedOutputStream;

import static Cards.Doors.DoorCard.Type.MONSTER;

public class Main {

    public static void main(String[] args) {

        try {
            FileInputStream fs = new FileInputStream("/home/nyanbanan/IdeaProjects/Manchkin/logging.properties");
            Log.readProperty(fs);
        } catch (FileNotFoundException exc) {
            Log.fmtGlobLog(exc.toString());
        }

        int colvo_person = 5; // TODO: получаем количество игроков
        Log.fmtGlobLog("КОЛИЧЕСТВО ИГРОКОВ: " + colvo_person + "\n");
        List <Player> people = new ArrayList<>(colvo_person);
        for (int i = 0; i < colvo_person; ++i) {
            people.add(new Player());
        }

        Game goGame = new Game(people);

        Log.fmtGlobLog("ИГРОКИ ОДЕВАЮТСЯ\n");
        //каждый одевается одновременно
        goGame.goGetDressedBeforeTheGame();

        //игровой процесс

        boolean victory = false;
        int index = (int) (Math.random() * people.size());
        while (!victory) { //игрок 1 открывает дверь -> соответсвующие действия с присутсвием возможности у других "Напихать"

            people.get(index).LeaveCurseCard(); // снятие все применившихся проклятий

            DeckManager deckManager = new DeckManager();
            var doorDeck = deckManager.getDoorDeck();
            if (doorDeck.isEmpty()) {
                Log.fmtGlobLog("КОЛОДА ПУСТА\n");
            } else {
                Log.fmtGlobLog("БЕРЕМ КАРТУ ИЗ КОЛОДЫ ДВЕРЕЙ\n");
                var card = doorDeck.pullCard(); // TODO: вытянуть одну карту

                BattleField battleField = new BattleField();
                goGame.openTheDoor(card, battleField, index);

                var doorCard = (DoorCard) card;
                if (doorCard.getType() == MONSTER)
                    goGame.lookingForTrouble(battleField, index);
                
                if (goGame.getPeople().get(index).getPerson().getLevel() == 10) {
                    victory = true;
                    Log.fmtGlobLog("ВЫИГРАЛ ИГРОК: " + index + "\n");
                } else
                    goGame.goCardReset(index);


                if (index == people.size() - 1)
                    index = 0;
                else
                    index++;
            }

        }

    }



}
