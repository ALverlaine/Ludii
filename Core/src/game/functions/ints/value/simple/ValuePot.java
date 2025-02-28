package game.functions.ints.value.simple;

import java.util.BitSet;

import annotations.Hide;
import game.Game;
import game.functions.ints.BaseIntFunction;
import game.types.state.GameType;
import other.context.Context;

/**
 * Returns the pot value
 *
 * @author Alexandre Verlaine
 * @remarks To store a pot value in the state.
 */
@Hide
public final class ValuePot extends BaseIntFunction
{
    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------

    /**
     */
    public ValuePot()
    {
        // Nothing to do
    }

    //-------------------------------------------------------------------------

    @Override
    public int eval(final Context context)
    {
        // pendingValues should mathematically be a set, so if it contains
        // more than 1 value we don't know what to return and just return
        // the default of 0 instead
        return context.state().pot();
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
        return GameType.PotValues;
    }

    @Override
    public BitSet concepts(final Game game)
    {
        final BitSet concepts = new BitSet();
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
        // Nothing to do
    }

    //-------------------------------------------------------------------------

    @Override
    public String toEnglish(final Game game)
    {
        return "the pot value";
    }

    //-------------------------------------------------------------------------

}
