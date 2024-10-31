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
 * Defines a 54-card French deck (52 cards + 2 Jokers).
 *
 * @example (cards54)
 */
public class Cards54 extends Item
{
    /** The suits in a French deck. */
    private static final String[] SUITS = { "Hearts", "Diamonds", "Clubs", "Spades" };

    /** The ranks in a French deck from Ace to King. */
    private static final String[] RANKS = { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };

    /**
     * Definition of a set of 54 French cards.
     *
     * @param jokerCount The number of Jokers to include [2].
     * @example (cards54)
     */
    public Cards54(@Opt @Name final Integer jokerCount)
    {
        super(null, Constants.UNDEFINED, RoleType.Shared);

        int jokers = (jokerCount == null) ? 2 : jokerCount.intValue();

        if (jokers < 0 || jokers > 2)
            throw new IllegalArgumentException("You can have a maximum of 2 Jokers.");

        setType(ItemType.Cards54);
    }

    /***
     * @return A list of all the cards in this set (including Jokers).
     */
    public ArrayList<CardType> generateCards()
    {
        final ArrayList<CardType> cards = new ArrayList<>();

        // Generate 52 cards (13 ranks for each of the 4 suits)
        for (final String suit : SUITS)
        {
            for (final String rank : RANKS)
            {
                final CardType card = new CardType(
                        rank + " of " + suit, new String[]{"Rank", "Suits"}, new String[]{rank, suit}
                );
                cards.add(card);
            }
        }

        // Add Jokers
        cards.add(new CardType("Joker 1", new String[]{"Rank", "Suits"}, new String[]{"Joker", "Joker"}));
        cards.add(new CardType("Joker 2", new String[]{"Rank", "Suits"}, new String[]{"Joker", "Joker"}));

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
