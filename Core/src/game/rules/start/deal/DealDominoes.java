package game.rules.start.deal;


import java.util.ArrayList;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import annotations.Name;
import annotations.Opt;
import annotations.Or2;
import game.Game;
import game.equipment.component.Component;
import game.equipment.container.Container;
import game.functions.ints.IntFunction;
import game.rules.start.Start;
import game.rules.start.StartRule;
import game.types.board.SiteType;
import game.types.component.DealableType;
import game.types.state.GameType;
import gnu.trove.list.array.TIntArrayList;
import main.Constants;
import other.action.Action;
import other.action.move.move.ActionMove;
import other.context.Context;
import other.move.Move;
import other.state.container.ContainerState;

/**
 * To deal different components between players.
 *
 * @author Eric.Piette
 */
public final class DealDominoes extends StartRule
{
    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------

    /** The number to deal. */
    private final int count;

    /** The number to deal. */


    private final boolean stack;

    //-------------------------------------------------------------------------

    /**
     * @param count The number of components to deal [1].
     * @param stack If the deal is in a stack
     *
     * @example (deal Dominoes 7)
     */
    public DealDominoes
    (
            @Opt final Integer count,
            @Opt @Name final Boolean stack
    )

    {
        this.count = (count == null) ? 1 : count.intValue();
        this.stack = stack == null ? false : stack;
    }

    //-------------------------------------------------------------------------






    /**
     * To deal dominoes.
     *
     * @param context
     */
    @Override
    public void eval(final Context context)
    {
        final TIntArrayList handIndex = new TIntArrayList();
        for (final Container c : context.containers())
            if (c.isHand() && !c.isDeck() && !c.isDice())
                handIndex.add(context.sitesFrom()[c.index()]);

        // If each player does not have a hand, nothing to do.
        if (handIndex.size() != context.game().players().count())
            return;

        final Component[] components = context.components();
        if (components.length < count * handIndex.size())
            throw new IllegalArgumentException("You can not deal so much dominoes in the initial state.");

        final TIntArrayList toDeal = new TIntArrayList();
        for (int i = 1; i < components.length; i++)
            toDeal.add(i);

        final int nbPlayers = context.players().size() - 1;

        final ArrayList<boolean[]> masked = new ArrayList<>();
        for (int i = 1; i <= nbPlayers; i++)
        {
            masked.add(new boolean[nbPlayers]);
            for (int j = 1; j <= nbPlayers; j++)
            {
                if (i == j)
                    masked.get(i - 1)[j - 1] = false;
                else
                    masked.get(i - 1)[j - 1] = true;
            }
        }

        int dealed = 0;
        while (dealed < (count * 2))
        {
            final int index = context.rng().nextInt(toDeal.size());
            final int indexComponent = toDeal.getQuick(index);
            final Component component = components[indexComponent];
            final int currentPlayer = dealed % nbPlayers;
            Start.placePieces(context, handIndex.getQuick(currentPlayer) + (dealed / nbPlayers), component.index(), 1,
                    Constants.OFF, Constants.OFF, Constants.UNDEFINED, false,
                    SiteType.Cell);
            toDeal.removeAt(index);
            dealed++;
        }
    }

    //-------------------------------------------------------------------------

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
    public boolean isStatic()
    {
        return true;
    }

    @Override
    public long gameFlags(final Game game)
    {
        long gameFlags = stack ? GameType.Stacking : 0l;
        return gameFlags | GameType.LargePiece | GameType.Dominoes | GameType.Stochastic | GameType.HiddenInfo;

    }
    @Override
    public void preprocess(final Game game)
    {
        // Do nothing
    }

    //-------------------------------------------------------------------------

    @Override
    public String toString()
    {
        final String str = "(Deal Dominoes)";
        return str;
    }

    //-------------------------------------------------------------------------

    @Override
    public String toEnglish(final Game game)
    {
        return "deal " + count + " Dominoes to each player";
    }

    //-------------------------------------------------------------------------

}