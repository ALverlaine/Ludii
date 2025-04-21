package game.functions.card;

import java.util.BitSet;

import game.Game;
import game.equipment.component.Component;
import game.functions.ints.BaseIntFunction;
import other.concept.Concept;
import other.context.Context;

/**
 * Returns the current trump suit.
 * @author  Alexandre Verlaine
 */

public final class trump extends BaseIntFunction
{
    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------

    /**
     * @example (trumpSuit)
     */
    public trump()
    {
        // Nothing to do.
    }

    //-------------------------------------------------------------------------

    @Override
    public int eval(final Context context)
    {
        return context.state().trump();
    }

    //-------------------------------------------------------------------------

    @Override
    public boolean isStatic()
    {
        return false;
    }

    @Override
    public long gameFlags(final Game game)
    {
        return 0;
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

    @Override
    public void preprocess(final Game game)
    {
        // Nothing to do.
    }

    @Override
    public boolean missingRequirement(final Game game)
    {
        boolean missingRequirement = false;
        boolean gameHasCard = false;
        for (int i = 1; i < game.equipment().components().length; i++)
        {
            final Component component = game.equipment().components()[i];
            if (component.isCard())
            {
                gameHasCard = true;
                break;
            }

        }

        if (!gameHasCard)
        {
            game.addRequirementToReport("The ludeme (card TrumpSuit ...) is used but the equipment has no cards.");
            missingRequirement = true;
        }
        return missingRequirement;
    }

    @Override
    public String toEnglish(final Game game)
    {
        return "the current trump suit";
    }
}
