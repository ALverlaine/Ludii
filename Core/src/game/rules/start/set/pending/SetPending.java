package game.rules.start.set.pending;

import java.util.BitSet;

import annotations.Hide;
import annotations.Opt;
import annotations.Or;
import game.Game;
import game.functions.ints.IntFunction;
import game.functions.region.RegionFunction;
import game.rules.play.moves.BaseMoves;
import game.rules.play.moves.Moves;
import game.rules.play.moves.nonDecision.effect.Effect;
import game.rules.play.moves.nonDecision.effect.Then;
import game.rules.start.StartRule;
import game.types.state.GameType;
import main.Constants;
import other.action.state.ActionSetPending;
import other.concept.Concept;
import other.context.Context;
import other.move.Move;

//-----------------------------------------------------------------------------

/**
 * Returns the set of moves that set the "pending" value in the state.
 *
 * @author Eric.Piette and cambolbro and Dennis Soemers
 *
 */
@Hide
public final class SetPending extends StartRule
{
    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------

    /** Single value (typically site) to set as pending */
    private final IntFunction value;

    /** Region to set as pending */
    private final RegionFunction region;

    //-------------------------------------------------------------------------

    /**
     * @param value  The value to refer to the pending state [1].
     * @param region The set of locations to put in pending.
     *
     */
    public SetPending
    (
            @Opt @Or final IntFunction		value,
            @Opt @Or final RegionFunction 	region
    )
    {
        super();
        this.value = value;
        this.region = region;
    }

    //-------------------------------------------------------------------------

    @Override
    public void eval(final Context context)
    {


        if (region == null)
        {
            final Move move;
            final ActionSetPending actionPending = (value == null) ? new ActionSetPending(Constants.UNDEFINED)
                    : new ActionSetPending(value.eval(context));
            System.out.println("value: " + value);
            move = new Move(actionPending);
            context.trial().addMove(move);
            context.trial().addInitPlacement();
        }
        else
        {
            final int[] sites = region.eval(context).sites();

            if (sites.length != 0)
            {
                final ActionSetPending actionPending = new ActionSetPending(sites[0]);
                final Move move = new Move(actionPending);

                for (int i = 1; i < sites.length; i++)
                {
                    final ActionSetPending actionToadd = new ActionSetPending(sites[i]);
                    move.actions().add(actionToadd);
                }

                context.trial().addMove(move);
                context.trial().addInitPlacement();
            }
        }
    }



    //-------------------------------------------------------------------------

    @Override
    public long gameFlags(final Game game)
    {
        long gameFlags = GameType.PendingValues;
        if (value != null)
            gameFlags |= value.gameFlags(game);
        if (region != null)
            gameFlags |= region.gameFlags(game);

        return gameFlags;
    }

    @Override
    public BitSet concepts(final Game game)
    {
        final BitSet concepts = new BitSet();
        concepts.or(super.concepts(game));
        concepts.set(Concept.SetPending.id(), true);
        if (value != null)
            concepts.or(value.concepts(game));
        if (region != null)
            concepts.or(region.concepts(game));
        return concepts;
    }

    @Override
    public BitSet writesEvalContextRecursive()
    {
        final BitSet writeEvalContext = new BitSet();
        writeEvalContext.or(super.writesEvalContextRecursive());
        if (value != null)
            writeEvalContext.or(value.writesEvalContextRecursive());
        if (region != null)
            writeEvalContext.or(region.writesEvalContextRecursive());

        return writeEvalContext;
    }

    @Override
    public BitSet readsEvalContextRecursive()
    {
        final BitSet readEvalContext = new BitSet();
        readEvalContext.or(super.readsEvalContextRecursive());
        if (value != null)
            readEvalContext.or(value.readsEvalContextRecursive());
        if (region != null)
            readEvalContext.or(region.readsEvalContextRecursive());

        return readEvalContext;
    }

    @Override
    public boolean missingRequirement(final Game game)
    {
        boolean missingRequirement = false;
        missingRequirement |= super.missingRequirement(game);
        if (value != null)
            missingRequirement |= value.missingRequirement(game);
        if (region != null)
            missingRequirement |= region.missingRequirement(game);

        return missingRequirement;
    }

    @Override
    public boolean willCrash(final Game game)
    {
        boolean willCrash = false;
        willCrash |= super.willCrash(game);
        if (value != null)
            willCrash |= value.willCrash(game);
        if (region != null)
            willCrash |= region.willCrash(game);

        return willCrash;
    }

    @Override
    public boolean isStatic()
    {
        return false;
    }

    @Override
    public void preprocess(final Game game)
    {
        if (value != null)
            value.preprocess(game);
        if (region != null)
            region.preprocess(game);
    }

    @Override
    public String toEnglish(final Game game)
    {
        String englishString = "";

        if (value != null)
            englishString = "set the site " + value.toEnglish(game) + " to pending";
        else if (region != null)
            englishString = "set the region " + region.toEnglish(game) + " to pending";
        else
            englishString = "set pending";

        String thenString = "";

        return englishString + thenString;
    }

}
