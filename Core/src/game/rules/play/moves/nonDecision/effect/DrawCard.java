package game.rules.play.moves.nonDecision.effect;

import annotations.Name;
import annotations.Opt;
import annotations.Or;
import game.Game;
import game.equipment.component.Component;
import game.equipment.container.Container;
import game.equipment.container.other.Deck;
import game.functions.booleans.BooleanConstant;
import game.functions.booleans.BooleanFunction;
import game.functions.intArray.state.Rotations;
import game.functions.ints.IntFunction;
import game.functions.region.RegionFunction;
import game.rules.play.moves.BaseMoves;
import game.rules.play.moves.Moves;
import game.types.board.SiteType;
import game.types.play.RoleType;
import game.types.state.GameType;
import game.util.moves.From;
import game.util.moves.To;
import gnu.trove.list.array.TIntArrayList;
import other.PlayersIndices;
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



    /** Cell/Edge/Vertex for the origin. */
    private SiteType typeFrom;

    /** Cell/Edge/Vertex for the target. */
    private SiteType typeTo;
    /** Count. */
    private final IntFunction countFn;

    /** To move a complete stack. */
    private final boolean stack;
    /** The mover of this moves for simultaneous game (e.g. Rock-Paper-Scissors). */
    private final RoleType mover;


    /**
     * @param then   The moves applied after this move is applied.
     *
     * @example (drawCard 1 (to (sites Hand Mover) if:(is Empty (to) and (is Occupied (from)))))
     */
    public DrawCard(
                            @Opt @Name  final IntFunction     count,
                            @Opt 		final RoleType mover,
                            @Opt @Name final Boolean         stack,
                            @Opt final Then then
    )
    {
        super(then);

        this.stack = (stack == null) ? false : stack.booleanValue();

        this.mover = mover == null ? RoleType.All : mover;
        countFn = count;

    }
    @Override
    public Moves eval(final Context context) {
        final int origFrom = context.from();
        final int origTo = context.to();
        final BaseMoves moves = new BaseMoves(super.then());
        if (countFn != null) {
            return moves;
        } else {
            return moves;

        }
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
