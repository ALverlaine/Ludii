package game.equipment.container.deck;

import annotations.Name;
import game.equipment.component.Card;
import game.equipment.container.Container;
import game.types.play.RoleType;
import main.Constants;

public class Deck extends Container {

    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------

    /**
     * Creating a deck.
     *
     * @param  role is query type to perform.
     *
     * @example (is Solved)
     */

    public Deck
    (
            @Name String label
    )
    {
        super("Deck", Constants.UNDEFINED, RoleType.Neutral);
    }

    public static Boolean construct(final Card isType) throws IllegalArgumentException {
        throw new IllegalArgumentException("Deck(): A CardType is not implemented.");
    }

    @Override
    public void createTopology(int beginIndex, int numEdges) {

    }

    public static Boolean construct(final SortingType isType) {
        switch (isType)

        {
            case Shuffle:
                return false;
            default:
                break;
        }
        throw new IllegalArgumentException("Deck(): A CardType is not implemented.");
    }

}
