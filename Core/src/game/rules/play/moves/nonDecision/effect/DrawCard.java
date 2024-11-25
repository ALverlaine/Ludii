package game.rules.play.moves.nonDecision.effect;

import annotations.Opt;
import annotations.Or;
import game.Game;
import game.equipment.component.Component;
import game.equipment.container.Container;
import game.equipment.container.other.Deck;
import game.functions.ints.IntFunction;
import game.functions.region.RegionFunction;
import game.rules.play.moves.BaseMoves;
import game.rules.play.moves.Moves;
import game.types.board.SiteType;
import game.types.state.GameType;
import other.action.Action;
import other.action.move.move.ActionMove;
import other.context.Context;
import other.move.Move;
import other.state.container.ContainerState;
import other.trial.Trial;
import main.Constants;

import java.util.BitSet;

/**
 * Draws a card from a deck and adds it to the player's hand.
 *
 * @example (drawCard)
 */
public final class DrawCard extends Effect {
    private static final long serialVersionUID = 1L;

    // The deck ID from which to draw.
    /** Which local state. */
    private final IntFunction localState;

    private final RegionFunction region;
    /**
     * @param then   The moves applied after this move is applied.
     *
     * @example (drawCard 1 (to (sites Hand Mover) if:(is Empty (to) and (is Occupied (from)))))
     */
    public DrawCard(
                            final       int             numberOfDraw,
            @Opt            final       SiteType        type,
                    @Or     final       IntFunction     locationFunction,
                    @Or     final       RegionFunction  regionFunction,
            @Opt final Then then
    )
    {
        super(then);
    this.localState = null;
    this.region = null;

    }
    @Override
    public Moves eval(final Context context)
    {
        final Moves moves = new BaseMoves(super.then());

        // Check if there is a deck available
        if (context.game().handDeck().isEmpty())
            return moves;

        final int mover = context.state().mover();
        int moverHandIndex = -1;

        // Identify the hand of the current player (mover)
        for (final Container c : context.containers())
        {
            if (c.isHand() && !c.isDeck() && !c.isDice() && c.owner() == mover)
            {
                moverHandIndex = context.sitesFrom()[c.index()];
                break;
            }
        }

        // If the mover has no hand, nothing to do
        if (moverHandIndex == -1)
            return moves;

        // Access the deck
        final Deck deck = context.game().handDeck().get(0);
        final ContainerState cs = context.containerState(deck.index());
        final int indexSiteDeck = context.sitesFrom()[deck.index()];
        final int sizeDeck = cs.sizeStackCell(indexSiteDeck);
        final int count = 1; // Number of cards to draw

        // Check if there are enough cards to give
        if (sizeDeck < count)
            throw new IllegalArgumentException("Not enough cards to draw.");

        // Distribute the specified number of cards to the mover
        for (int i = 0; i < count; i++)
        {
            // Construct an action to move a card from the deck to the mover's hand
            final Action dealAction = ActionMove.construct(
                    SiteType.Cell,
                    indexSiteDeck,
                    cs.sizeStackCell(indexSiteDeck) - 1 - i, // Top card on the stack
                    SiteType.Cell,
                    moverHandIndex,
                    Constants.OFF, Constants.OFF, Constants.OFF, Constants.OFF,
                    false
            );
            final Move move = new Move(dealAction);
            move.setMover(mover);
            moves.moves().add(move);
        }

        // Add any subsequent moves specified by "then"
        if (then() != null)
            for (final Move move : moves.moves())
                move.then().add(then().moves());

        // Register moves with this Ludeme
        for (final Move move : moves.moves())
            move.setMovesLudeme(this);

        return moves;
    }


    @Override
    public long gameFlags(final Game game) {
        return GameType.Card | super.gameFlags(game);
    }

    @Override
    public BitSet concepts(final Game game) {
        final BitSet concepts = super.concepts(game);
        return concepts;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean missingRequirement(final Game game) {
        return super.missingRequirement(game);
    }

    @Override
    public boolean willCrash(final Game game) {
        return super.willCrash(game);
    }

    @Override
    public void preprocess(final Game game) {
        super.preprocess(game);
    }
}
