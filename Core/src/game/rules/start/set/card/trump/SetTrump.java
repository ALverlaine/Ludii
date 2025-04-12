package game.rules.start.set.card.trump;

import java.util.BitSet;

import annotations.Hide;
import annotations.Opt;
import annotations.Or;
import game.Game;
import game.functions.ints.IntFunction;
import game.rules.play.moves.BaseMoves;
import game.rules.play.moves.Moves;
import game.rules.play.moves.nonDecision.effect.Note;
import game.rules.start.StartRule;
import game.types.play.RoleType;
import game.types.state.GameType;
import main.Constants;
import other.action.cards.ActionSetTrump;
import other.action.others.ActionNote;
import other.concept.Concept;
import other.context.Context;
import other.move.Move;

/**
 * Stores a Trump in the state in the variable "trump".
 *
 * @author Eric.Piette
 */
@Hide
public final class SetTrump  extends StartRule
{
    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------

    /** The trump to set. */
    private final String trump;
    private final IntFunction trum;


    /**
     * @param trump  The name of the trump.
     * @param trum  The value of the trump
     */
    public SetTrump
    (
            @Or final String      trump,
            @Or final IntFunction trum

    )
    {
        super();
        this.trump = trump;
        this.trum = trum;
    }

    //-------------------------------------------------------------------------

    @Override
    public void eval(final Context context)
    {
        final Move move;
        if(trump != null) {
            ActionSetTrump actionSetTrump = new ActionSetTrump(trump);
            Note note = new Note(null, null, "Trump is " + trump, null , null, null, null, null, null, null ,null ,null ,null);
            move = new Move(actionSetTrump);
            context.trial().addMove(move);
            final Move nmove = new Move(new ActionNote("Trump is " + trump, Constants.NOBODY));
            context.trial().addMove(nmove);
            context.trial().addInitPlacement();
        }
        else{
            ActionSetTrump actionSetTrump = new ActionSetTrump(String.valueOf(trum));
            move = new Move(actionSetTrump);
            final Move nmove = new Move(new ActionNote("Trump is " + trum, Constants.NOBODY));
            context.trial().addMove(nmove);
            context.trial().addMove(move);
            context.trial().addInitPlacement();
        }


    }


    //-------------------------------------------------------------------------

    @Override
    public long gameFlags(final Game game)
    {
        long gameFlags = GameType.MapValue;


        return gameFlags;
    }

    @Override
    public BitSet concepts(final Game game)
    {
        final BitSet concepts = new BitSet();
        concepts.or(super.concepts(game));

        return concepts;
    }

    @Override
    public BitSet writesEvalContextRecursive()
    {
        final BitSet writeEvalContext = new BitSet();
        writeEvalContext.or(super.writesEvalContextRecursive());

        return writeEvalContext;
    }

    @Override
    public BitSet readsEvalContextRecursive()
    {
        final BitSet readEvalContext = new BitSet();
        readEvalContext.or(super.readsEvalContextRecursive());


        return readEvalContext;
    }

    @Override
    public boolean missingRequirement(final Game game)
    {
        boolean missingRequirement = false;
        missingRequirement |= super.missingRequirement(game);


        return missingRequirement;
    }

    @Override
    public boolean willCrash(final Game game)
    {
        boolean willCrash = false;
        willCrash |= super.willCrash(game);


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
        if(trum!=null)
            trum.preprocess(game);
    }

    //-------------------------------------------------------------------------

    @Override
    public String toEnglish(final Game game)
    {

        return "set the trump to " + trump;
    }

    //-------------------------------------------------------------------------

}
