package game.equipment.container.deck;

import annotations.Name;
import game.equipment.component.CardType;
import game.equipment.container.Container;
import game.types.play.RoleType;
import main.Constants;

public class Deck extends Container {

    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------
    private final String label;
    private final int numCards;
    /**
     * Creating a deck.
     *
     * @param  Name is the name of the label
     * @param  Number is to number of cards that can be stored in the deck
     *
     * @example (is Solved)
     */

    public Deck
    (
            @Name   String label,
                    int numCards
    )
    {
        super(label, Constants.UNDEFINED, RoleType.Neutral);
        this.label = label;
        this.numCards = numCards;

    }


    @Override
    public void createTopology(int beginIndex, int numEdges) {
        // TODO Auto-generated method stub

    }
}
