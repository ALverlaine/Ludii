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
import game.equipment.component.card.CardType;
import game.equipment.container.Container;
import game.functions.ints.IntFunction;
import game.functions.region.RegionFunction;
import game.rules.start.Start;
import game.rules.start.StartRule;
import game.types.board.SiteType;
import game.types.component.DealableType;
import game.types.state.GameType;
import gnu.trove.list.array.TIntArrayList;
import main.Constants;
import other.Sites;
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
public final class DealDeck extends StartRule
{
    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------

    /** The number to deal. */
    private final int count;



    private final boolean stack;
    private final IntFunction sites;
    private final String deckType;

    //-------------------------------------------------------------------------

    /**
     * @param count The number of components to deal [1].
     * @param stack If the deal is in a stack
     *
     * @example (deal Dominoes 7)
     */
    public DealDeck
    (
            @Opt final Integer count,
            @Opt final IntFunction sites,
            @Opt @Name final Boolean stack,
            @Opt @Name final String deckType
    )

    {
        this.count = (count == null) ? 1 : count.intValue();
        this.stack = stack == null ? false : stack;
        this.sites = sites;
        this.deckType = deckType;
    }

    //-------------------------------------------------------------------------




    @Override
    public void eval(Context context) {
        final TIntArrayList handIndex = new TIntArrayList();
        for (final Container c : context.containers())
            if (c.isHand() && !c.isDeck() && !c.isDice())
                if(c.isSharedHand())
                    handIndex.add(context.sitesFrom()[c.index()]);


        final Component[] components = context.components();
        System.out.println(Arrays.toString(components));
        if (components.length < count)
            throw new IllegalArgumentException("You can not deal so much card in the initial state.");

        final TIntArrayList toDeal = new TIntArrayList();
        for (int i = 1; i < components.length; i++)
            if(deckType != null){
                final CardType card = (CardType) components[i];
                if(card.getDeckType().equals(deckType)){
                    toDeal.add(i);
                }
            }
            else {
                toDeal.add(i);
            }

        int dealed = 0;
        while (dealed < count)
        {
            final int index = context.rng().nextInt(toDeal.size());
            final int indexComponent = toDeal.getQuick(index);
            final Component component = components[indexComponent];
            final int Deck = sites != null ? sites.eval(context): handIndex.getQuick(0);
            Start.placePieces(context, Deck, component.index(), 1,
                    Constants.OFF, Constants.OFF, Constants.UNDEFINED, true,
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
        return gameFlags | GameType.LargePiece | GameType.Dominoes | GameType.Stochastic;
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
        final String str = "(Deal Deck)";
        return str;
    }

    //-------------------------------------------------------------------------

    @Override
    public String toEnglish(final Game game)
    {
        return "deal " + count + " Deck to each player";
    }

    //-------------------------------------------------------------------------

}