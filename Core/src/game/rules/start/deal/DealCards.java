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
import game.rules.start.Start;
import game.rules.start.StartRule;
import game.types.board.SiteType;
import game.types.component.DealableType;
import game.types.play.RoleType;
import game.types.state.GameType;
import gnu.trove.list.array.TIntArrayList;
import main.Constants;
import other.PlayersIndices;
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
public final class DealCards extends StartRule
{
    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------

    /** The number to deal. */
    private final int count;


    private final boolean stack;
    private final RoleType roletype;
    private final String deckType;

    //-------------------------------------------------------------------------

    /**
     * @param count The number of components to deal [1].
     * @param stack If the deal is in a stack
     *
     * @example (deal Dominoes 7)
     */
    public DealCards
    (
            final Integer count,
            final RoleType who,
            @Opt @Name final Boolean stack,
            @Opt @Name final String deckType
    )

    {
        this.count = (count == null) ? 1 : count.intValue();
        this.stack = stack == null ? false : stack;
        this.roletype = who;
        this.deckType = deckType;
    }

    //-------------------------------------------------------------------------





    /**
     * To deal cards.
     *
     * @param context The game context.
     */
    @Override
    public void eval(final Context context)
    {
        if(roletype.equals(RoleType.All)){
            evalAll(context);
        }
        else {
            evalPlayer(context);
        }

    }

    private void evalPlayer(Context context) {
        final TIntArrayList handIndex = new TIntArrayList();
        for (final Container c : context.containers())
        {
            if (c.isHand() && !c.isDeck() && !c.isDice() && !c.isSharedHand())

                handIndex.add(context.sitesFrom()[c.index()]);
        }

        // If each player does not have a hand, nothing to do.
        if (handIndex.size() < context.game().players().count())
            return;

        final Component[] components = context.components();
        System.out.println(Arrays.toString(components));
        if (components.length < count && !stack)
            throw new IllegalArgumentException("Not enough cards to deal.");

        final TIntArrayList toDeal = new TIntArrayList();
        for (int i = 1; i < components.length; i++)
        {
            if(deckType != null){
                final CardType card = (CardType) components[i];
                if(card.getDeckType().equals(deckType)){
                    toDeal.add(i);
                }
            }
            else {
                toDeal.add(i);
            }
        }

        //final int nbPlayers = context.players().size() - 1;
        int dealt = 0;
        //System.out.println(toDeal.toString());
        while (dealt < count)
        {
            if(toDeal.size() <= 0) throw new IllegalArgumentException("Dealing to many cards");
            final int index = context.rng().nextInt(toDeal.size());
            final int cardIndex = toDeal.getQuick(index);
            final Component card = components[cardIndex];
            System.out.println(card.name());
            final TIntArrayList idPlayers = PlayersIndices.getIdRealPlayers(context, roletype);
            System.out.println(idPlayers.toString());
            final int currentPlayer = idPlayers.getQuick(0);
            if(stack){
                Start.placePieces(context, handIndex.getQuick(currentPlayer-1),
                        card.index(), 1, Constants.OFF, Constants.OFF, Constants.UNDEFINED, stack,
                        SiteType.Cell);
            }
            else{
                Start.placePieces(context, handIndex.getQuick(currentPlayer-1) + dealt,
                        card.index(), 1, Constants.OFF, Constants.OFF, Constants.UNDEFINED, stack,
                        SiteType.Cell);
            }
            toDeal.removeAt(index);
            dealt++;
        }

    }

    private void evalAll(Context context)
    {
        final TIntArrayList handIndex = new TIntArrayList();
        for (final Container c : context.containers())
        {
            if (c.isHand() && !c.isDeck() && !c.isDice())

                handIndex.add(context.sitesFrom()[c.index()]);
        }

        // If each player does not have a hand, nothing to do.
        if (handIndex.size() < context.game().players().count())
            return;

        final Component[] components = context.components();
        if (components.length < count * handIndex.size() && !stack)
            throw new IllegalArgumentException("Not enough cards to deal.");

        final TIntArrayList toDeal = new TIntArrayList();
        for (int i = 1; i < components.length; i++)
        {
            if(deckType != null){
                final CardType card = (CardType) components[i];
                if(card.getDeckType().equals(deckType)){
                    toDeal.add(i);
                }
            }
            else {
                toDeal.add(i);
            }
        }

        final int nbPlayers = context.players().size() - 1;
        int dealt = 0;
        System.out.println(toDeal.toString());
        while (dealt < (count * nbPlayers))
        {
            if(toDeal.size() <= 0) throw new IllegalArgumentException("Dealing to many cards");
            final int index = context.rng().nextInt(toDeal.size());
            final int cardIndex = toDeal.getQuick(index);
            final Component card = components[cardIndex];
            System.out.println(card.name());
            final int currentPlayer = dealt % nbPlayers;
            if(stack){
                Start.placePieces(context, handIndex.getQuick(currentPlayer),
                        card.index(), 1, Constants.OFF, Constants.OFF, Constants.UNDEFINED, stack,
                        SiteType.Cell);
            }
            else{
                Start.placePieces(context, handIndex.getQuick(currentPlayer) + (dealt / nbPlayers),
                        card.index(), 1, Constants.OFF, Constants.OFF, Constants.UNDEFINED, stack,
                        SiteType.Cell);
            }
            toDeal.removeAt(index);
            dealt++;
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
        final String str = "(Deal Cards)";
        return str;
    }

    //-------------------------------------------------------------------------

    @Override
    public String toEnglish(final Game game)
    {
        return "deal " + count + " Cards to each player";
    }

    //-------------------------------------------------------------------------

}