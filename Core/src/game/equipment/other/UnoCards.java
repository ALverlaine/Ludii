package game.equipment.other;

import java.util.ArrayList;
import java.util.BitSet;

import annotations.Name;
import annotations.Opt;
import game.Game;
import game.equipment.Item;
import game.equipment.component.Component;
import game.equipment.component.card.CardType;
import game.types.play.RoleType;
import main.Constants;
import other.ItemType;
import other.concept.Concept;

/**
 * Defines a set of Uno cards with 4 colors (Blue, Red, Green, Yellow) and cards numbered from 1 to 4.
 *
 * @example (unoCards)
 */
public class UnoCards extends Item
{
    /** The colors in Uno. */
    private static final String[] COLORS = { "Blue", "Red", "Green", "Yellow" };

    /** The numbers in Uno (1 to 4). */
    private static final String[] NUMBERS = { "1", "2", "3", "4" };

    /**
     * Definition of a set of Uno cards.
     *
     * @example (unoCards)
     */
    public UnoCards()
    {
        super(null, Constants.UNDEFINED, RoleType.Shared);
        setType(ItemType.CardsUno);
    }

    /***
     * @return A list of all the Uno cards in this set.
     */
    public ArrayList<CardType> generateCards()
    {
        final ArrayList<CardType> cards = new ArrayList<>();

        // Generate Uno cards (4 colors, 4 numbers for each color)
        for (final String color : COLORS)
        {
            for (final String number : NUMBERS)
            {
                final CardType card = new CardType(
                        number + " of " + color,new String[]{"Number", "Color"}, new String[]{number, color}, null, null
                );
                cards.add(card);
            }
        }

        return cards;
    }

    @Override
    public BitSet concepts(final Game game)
    {
        final BitSet concepts = new BitSet();
        concepts.set(Concept.Card.id(), true);
        return concepts;
    }

    @Override
    public BitSet writesEvalContextRecursive()
    {
        final BitSet writeEvalContext = new BitSet();
        return writeEvalContext;
    }

    @Override
    public BitSet readsEvalContextRecursive()
    {
        final BitSet readEvalContext = new BitSet();
        return readEvalContext;
    }
}
