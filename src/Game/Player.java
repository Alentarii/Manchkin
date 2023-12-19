package Game;

import Cards.Card;
import Cards.DeckManager;
import Cards.Doors.Curses.CurseCard;
import Person.Person;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private final Person person;

    private List<Card> cards = new ArrayList<>();
    private List<Card> onMy = new ArrayList<>();
    private List<CurseCard> curseCards = new ArrayList<>();


    public Player () {
        this.person = new Person();
        DeckManager deckManager = new DeckManager();
        var door = deckManager.getDoorDeck();
        var treasure = deckManager.getTreasureDeck();
        for (int j = 0; j < 4; ++j) {
            this.cards.add(door.pullCard());
            this.cards.add(treasure.pullCard());
        }
    }

    public void takeTheReward (int count) {
        DeckManager deckManager = new DeckManager();
        var treasure = deckManager.getTreasureDeck();
        for (int j = 0; j < count; ++j)
            this.cards.add(treasure.pullCard());
    }

    public Person getPerson () {return this.person;}

    public List<Card> getShmots () {return this.onMy;}

    public List<Card> getCards () {return this.cards;}

    public void PlayCurseCard (CurseCard card) {
        curseCards.add(card);
    };

    public void LeaveCurseCard () {
        for (CurseCard c : curseCards)
            c.Leave(this.person);
    };

    public Card getCard (int i) {return this.cards.get(i);}

    public Card getShmot (int i) {return this.onMy.get(i);}

    public void addCard (Card card) {this.cards.add(card);}

    public void dropCard (Card card) {cards.remove(card);}


    public void putOn (int index) {
        onMy.add(cards.get(index));
        cards.remove(cards.get(index));
    }

    public void takeOff (int index) {
        cards.add(onMy.get(index));
        onMy.remove(onMy.get(index));
    }

}