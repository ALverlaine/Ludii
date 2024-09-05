package game.equipment.container.deck;

import annotations.Name;
import game.equipment.container.Container;
import game.types.play.RoleType;
import main.Constants;

public class Deck extends Container {

    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------
    private String name;
    private final int numCards;
    /**
     * Creating a deck.
     *
     * @param  name is the name of the label
     * @param  numCards is to number of cards that can be stored in the deck
     *
     * @example (is Solved)
     */

    public Deck
    (
            @Name   String name,
                    int numCards
    )
    {
        super("Deck", Constants.UNDEFINED, RoleType.Neutral);
        this.name = name;
        this.numCards = numCards;

    }


    @Override
    public void createTopology(int beginIndex, int numEdges) {
        // TODO Auto-generated method stub

    }
}
