package game.rules.play.moves.nonDecision.effect.deal.cards;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import annotations.Name;
import annotations.Opt;
import annotations.Or;
import game.Game;
import game.equipment.component.Component;
import game.equipment.container.Container;
import game.equipment.container.other.Deck;
import game.functions.ints.IntConstant;
import game.functions.ints.IntFunction;
import game.functions.region.RegionFunction;
import game.rules.play.moves.BaseMoves;
import game.rules.play.moves.Moves;
import game.rules.play.moves.nonDecision.effect.Effect;
import game.rules.play.moves.nonDecision.effect.FromTo;
import game.rules.play.moves.nonDecision.effect.Then;
import game.types.board.SiteType;
import game.types.component.DealableType;
import game.types.state.GameType;
import game.util.equipment.Region;
import game.util.moves.From;
import game.util.moves.To;
import gnu.trove.list.array.TIntArrayList;
import main.Constants;
import other.action.Action;
import other.action.BaseAction;
import other.action.move.ActionAdd;
import other.action.move.move.ActionMove;
import other.concept.Concept;
import other.context.Context;
import other.move.Move;
import other.state.container.ContainerState;

/**
 * Deals cards or dominoes to each player.
 *
 * @author Eric.Piette
 */
public final class dealCards extends Effect
{
    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------

    /** The number to deal. */
    private final IntFunction countFn;

    /** The region to deal. */
    private final From fromFn;
    /** The site to deal. */
    private final To toFn;



    //-------------------------------------------------------------------------

    /**
     * @param count     number of cards to deal.
     * @param from    The from to take cards.
     * @param to      The to to fill with card.
     * @param then      The moves applied after that move is applied.
     *
     * @example (deal Cards 3 (sites Hand Mover))
     */
    public dealCards
    (
            final IntFunction  count,
            @Name final From from,
            @Name final To to,
            @Opt final Then then
    )
    {
        super(then);
        this.countFn = count;
        this.fromFn = from;
        this.toFn = to;
    }

    //-------------------------------------------------------------------------

    @Override
    public Moves eval(final Context context)
    {
        int count = countFn.eval(context);
        if(toFn.region().eval(context).sites().length > count) {
            count = toFn.region().eval(context).sites().length;
            if (fromFn.region().eval(context).sites().length < count)
                count = fromFn.region().eval(context).sites().length;
        }
        final Moves moves = new BaseMoves(super.then());
        for(int i = 0; i < count; i++)
        {
            FromTo fromTo = new FromTo(fromFn, toFn, null, null, null, null, null);

        }
        return moves;
    }

    //-------------------------------------------------------------------------
    @Override
    public boolean canMove(final Context context)
    {
        return false;
    }

    @Override
    public boolean canMoveTo(final Context context, final int target)
    {
        return false;
    }

    //-------------------------------------------------------------------------

    @Override
    public long gameFlags(final Game game)
    {
        long gameFlags = countFn.gameFlags(game) | super.gameFlags(game);

        if (then() != null)
            gameFlags |= then().gameFlags(game);
        return GameType.Card | gameFlags;
    }

    @Override
    public BitSet concepts(final Game game)
    {
        final BitSet concepts = new BitSet();
        concepts.or(super.concepts(game));
        concepts.or(countFn.concepts(game));

        if (then() != null)
            concepts.or(then().concepts(game));

        return concepts;
    }

    @Override
    public BitSet writesEvalContextRecursive()
    {
        final BitSet writeEvalContext = new BitSet();
        writeEvalContext.or(super.writesEvalContextRecursive());
        writeEvalContext.or(countFn.writesEvalContextRecursive());

        if (then() != null)
            writeEvalContext.or(then().writesEvalContextRecursive());
        return writeEvalContext;
    }

    @Override
    public BitSet readsEvalContextRecursive()
    {
        final BitSet readEvalContext = new BitSet();
        readEvalContext.or(super.readsEvalContextRecursive());
        readEvalContext.or(countFn.readsEvalContextRecursive());

        if (then() != null)
            readEvalContext.or(then().readsEvalContextRecursive());
        return readEvalContext;
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
            game.addRequirementToReport("The ludeme (deal Cards ...) is used but the equipment has no cards.");
            missingRequirement = true;
        }


        missingRequirement |= super.missingRequirement(game);
        missingRequirement |= countFn.missingRequirement(game);

        if (then() != null)
            missingRequirement |= then().missingRequirement(game);
        return missingRequirement;
    }

    @Override
    public boolean willCrash(final Game game)
    {
        boolean willCrash = false;
        willCrash |= super.willCrash(game);
        willCrash |= countFn.willCrash(game);

        if (then() != null)
            willCrash |= then().willCrash(game);
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
        super.preprocess(game);
        countFn.preprocess(game);
    }

    //-------------------------------------------------------------------------

    @Override
    public String toEnglish(final Game game)
    {
        String beginString = "";

        String thenString = "";
        if (then() != null)
            thenString = " then " + then().toEnglish(game);

        return "deal " + countFn.toEnglish(game) + " cards to each player" + beginString + thenString;
    }

    //-------------------------------------------------------------------------

}
