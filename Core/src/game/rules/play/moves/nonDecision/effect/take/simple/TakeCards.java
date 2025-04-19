package game.rules.play.moves.nonDecision.effect.take.simple;

import java.util.Arrays;
import java.util.BitSet;

import annotations.Hide;
import annotations.Name;
import annotations.Opt;
import annotations.Or;
import game.Game;
import game.equipment.container.Container;
import game.equipment.container.other.Hand;
import game.functions.ints.IntFunction;
import game.functions.ints.last.LastFrom;
import game.functions.ints.last.LastTo;
import game.functions.region.RegionFunction;
import game.functions.region.last.Last;
import game.functions.region.*;
import game.rules.play.moves.BaseMoves;
import game.rules.play.moves.Moves;
import game.rules.play.moves.nonDecision.effect.Effect;
import game.rules.play.moves.nonDecision.effect.Then;
import game.types.board.SiteType;
import game.types.state.GameType;
import game.util.equipment.Region;
import game.util.moves.From;
import game.util.moves.To;
import gnu.trove.list.array.TIntArrayList;
import main.Constants;
import other.action.Action;
import other.action.move.ActionAdd;
import other.action.move.move.ActionMove;
import other.action.others.ActionPass;
import other.concept.Concept;
import other.context.Context;
import other.move.Move;
import other.state.container.ContainerState;

/**
 * Takes cards from somewhere.
 *
 * @author AlexandreVerlaine
 */
@Hide
public final class TakeCards extends Effect
{
    private static final long serialVersionUID = 1L;

    final  From fromFn;
    final To toFn;

    //-------------------------------------------------------------------------

    /**
     * @param then The moves applied after that move is applied.
     */
    public TakeCards
    (
             @Opt final From from,
             @Opt final To to,
            @Opt final Then then
    )
    {
        super(then);
        fromFn = from;
        toFn = to;
    }

    //-------------------------------------------------------------------------

    @Override
    public Moves eval(final Context context)
    {
        final Moves moves = new BaseMoves(super.then());

        //int to = toFn.eval(context);
        int site = Constants.OFF;
        RegionFunction regfrom = fromFn.region();
        RegionFunction regto = toFn.region();
        int[] from = regfrom.eval(context).sites();
        int[] to = regto.eval(context).sites();
        //System.out.println(to);

        for (int i = 0; i < from.length; i++) {
            int f = from[i];

            // Calculer l'index correspondant dans "to", en partant de la fin
            int t = to[to.length - 1 - i]; // Utilise les indices de la fin de "to"

            final Action dealAction = ActionMove.construct(
                    SiteType.Cell, f, 0, SiteType.Cell, t,
                    Constants.OFF, Constants.OFF, Constants.OFF, Constants.OFF, false
            );
            final Move move = new Move(dealAction);
            moves.moves().add(move);
        }

        if (then() != null)
            for (int j = 0; j < moves.moves().size(); j++)
                moves.moves().get(j).then().add(then().moves());
        // Store the Moves in the computed moves.
        for (int j = 0; j < moves.moves().size(); j++)
            moves.moves().get(j).setMovesLudeme(this);

        return moves;
    }

    //-------------------------------------------------------------------------

    @Override
    public long gameFlags(final Game game)
    {
        long gameFlags = GameType.LargePiece | GameType.Dominoes | super.gameFlags(game);

        if (then() != null)
            gameFlags |= then().gameFlags(game);

        return gameFlags;
    }

    @Override
    public BitSet concepts(final Game game)
    {
        final BitSet concepts = new BitSet();
        concepts.or(super.concepts(game));
        concepts.set(Concept.Domino.id(), true);

        if (then() != null)
            concepts.or(then().concepts(game));

        return concepts;
    }

    @Override
    public BitSet writesEvalContextRecursive()
    {
        final BitSet writeEvalContext = new BitSet();
        writeEvalContext.or(super.writesEvalContextRecursive());

        if (then() != null)
            writeEvalContext.or(then().writesEvalContextRecursive());
        return writeEvalContext;
    }

    @Override
    public BitSet readsEvalContextRecursive()
    {
        final BitSet readEvalContext = new BitSet();
        readEvalContext.or(super.readsEvalContextRecursive());

        if (then() != null)
            readEvalContext.or(then().readsEvalContextRecursive());
        return readEvalContext;
    }

    @Override
    public boolean missingRequirement(final Game game)
    {
        boolean missingRequirement = false;
        if (!game.hasDominoes())
        {
            game.addRequirementToReport("The ludeme (take Domino ...) is used but the equipment has no dominoes.");
            missingRequirement = true;
        }
        missingRequirement |= super.missingRequirement(game);

        if (then() != null)
            missingRequirement |= then().missingRequirement(game);
        return missingRequirement;
    }

    @Override
    public boolean willCrash(final Game game)
    {
        boolean willCrash = false;
        willCrash |= super.willCrash(game);

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
        // Nothing todo
    }

    //-------------------------------------------------------------------------

    @Override
    public String toEnglish(final Game game)
    {
        String thenString = "";
        if (then() != null)
            thenString = " then " + then().toEnglish(game);

        return "take a domino" + thenString;
    }

    //-------------------------------------------------------------------------

}
