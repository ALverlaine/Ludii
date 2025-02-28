package game.rules.start.set.var;

import java.util.BitSet;

import annotations.Hide;
import annotations.Opt;
import game.Game;
import game.functions.ints.IntConstant;
import game.functions.ints.IntFunction;
import game.rules.play.moves.BaseMoves;
import game.rules.play.moves.Moves;
import game.rules.play.moves.nonDecision.effect.Effect;
import game.rules.play.moves.nonDecision.effect.Then;
import game.rules.start.StartRule;
import game.types.state.GameType;
import main.Constants;
import other.action.state.ActionSetTemp;
import other.action.state.ActionSetVar;
import other.concept.Concept;
import other.context.Context;
import other.move.Move;

/**
 * Stores an integer in the state in the variable "var".
 *
 * @author Eric.Piette & Alexandre Verlaine
 */
@Hide
public final class SetVar extends StartRule
{
    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------

    /** The value to set. */
    private final IntFunction value;

    /** The name of the var */
    private final String name;

    /**
     * @param name  The name of the var.
     * @param value The value to store in the context [-1].
     */
    public SetVar
    (
            @Opt final String      name,
            @Opt final IntFunction value
    )
    {
        this.value = (value == null) ? new IntConstant(Constants.UNDEFINED) : value;
        this.name = name;
    }

    //-------------------------------------------------------------------------

    @Override
    public void eval(final Context context)
    {


        if (name == null)
        {
            final ActionSetTemp actionTemp = new ActionSetTemp(value.eval(context));
            actionTemp.apply(context, true);
            context.trial().addMove(new Move(actionTemp));
            context.trial().addInitPlacement();
        }
        else
        {
            final ActionSetVar actionSetVar = new ActionSetVar(name, value.eval(context));
            actionSetVar.apply(context, true);
            context.trial().addMove(new Move(actionSetVar));
            context.trial().addInitPlacement();
        }

    }

    //-------------------------------------------------------------------------


    //-------------------------------------------------------------------------

    @Override
    public long gameFlags(final Game game)
    {
        long gameFlags = GameType.MapValue;
        if (value != null)
            gameFlags |= value.gameFlags(game);


        return gameFlags;
    }

    @Override
    public BitSet concepts(final Game game)
    {
        final BitSet concepts = new BitSet();
        concepts.or(super.concepts(game));

        if (value != null)
            concepts.or(value.concepts(game));

        concepts.set(Concept.Variable.id(), true);
        concepts.set(Concept.SetVar.id(), true);

        return concepts;
    }

    @Override
    public BitSet writesEvalContextRecursive()
    {
        final BitSet writeEvalContext = new BitSet();
        writeEvalContext.or(super.writesEvalContextRecursive());

        if (value != null)
            writeEvalContext.or(value.writesEvalContextRecursive());

        return writeEvalContext;
    }

    @Override
    public BitSet readsEvalContextRecursive()
    {
        final BitSet readEvalContext = new BitSet();
        readEvalContext.or(super.readsEvalContextRecursive());

        if (value != null)
            readEvalContext.or(value.readsEvalContextRecursive());

        return readEvalContext;
    }

    @Override
    public boolean missingRequirement(final Game game)
    {
        boolean missingRequirement = false;
        missingRequirement |= super.missingRequirement(game);

        if (value != null)
            missingRequirement |= value.missingRequirement(game);


        return missingRequirement;
    }

    @Override
    public boolean willCrash(final Game game)
    {
        boolean willCrash = false;
        willCrash |= super.willCrash(game);

        if (value != null)
            willCrash |= value.willCrash(game);


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
    }

    //-------------------------------------------------------------------------

    @Override
    public String toEnglish(final Game game)
    {
        String thenString = "";

        return "set the variable " + name + " to " + value.toEnglish(game) + thenString;
    }

    //-------------------------------------------------------------------------

}
