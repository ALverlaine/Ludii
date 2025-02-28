package game.rules.play.moves.nonDecision.effect.deal;

import annotations.Name;
import annotations.Opt;
import annotations.Or;
import annotations.Or2;
import game.Game;
import game.functions.ints.IntFunction;
import game.functions.region.RegionFunction;
import game.rules.play.moves.Moves;
import game.rules.play.moves.nonDecision.effect.Effect;
import game.rules.play.moves.nonDecision.effect.Then;
import game.rules.play.moves.nonDecision.effect.deal.cards.*;
import game.rules.play.moves.nonDecision.effect.take.control.TakeControl;
import game.rules.play.moves.nonDecision.effect.take.simple.TakeCards;
import game.rules.play.moves.nonDecision.effect.take.simple.TakeDomino;
import game.types.board.SiteType;
import game.types.component.DealableType;
import game.types.play.RoleType;
import game.util.moves.From;
import game.util.moves.To;
import other.context.Context;

/**
 * Takes a piece or the control of pieces.
 *
 * @author Eric.Piette
 */
@SuppressWarnings("javadoc")
public final class Deal1 extends Effect
{
    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------

    /**
     * For taking a domino.
     *
     * @param dealType The type of property to take.
     * @param count    Number of cards to deal.
     * @param from    The from to take cards.
     * @param to      The to to fill with card.
     * @param then    The moves applied after that move is applied.
     *
     * @example (deal Cards 5 from:(sites Hand Shared) to:(sites Hand Mover)))
     */
    public static Moves construct
    (
            final DealableType dealType,
            final IntFunction  count,
            @Name final From from,
            @Name final To to,
            @Opt final Then then
    )
    {
        switch (dealType)
        {
            case Cards:
                return new dealCards(count, from, to, then);
            default:
                break;
        }

        // We should never reach that except if we forget some codes.
        throw new IllegalArgumentException("Deal(): A DealType is not implemented.");
    }



    private Deal1()
    {
        super(null);
        // Ensure that compiler does pick up default constructor
    }

    @Override
    public Moves eval(final Context context)
    {
        // Should not be called, should only be called on subclasses
        throw new UnsupportedOperationException("Deal.eval(): Should never be called directly.");
    }

    //-------------------------------------------------------------------------

    @Override
    public boolean isStatic()
    {
        // Should never be there
        return false;
    }

    @Override
    public long gameFlags(final Game game)
    {
        // Should never be there
        return 0L;
    }

    @Override
    public void preprocess(final Game game)
    {
        // Nothing to do.
    }

    @Override
    public boolean canMoveTo(Context context, int target)
    {
        // Should never be there
        throw new UnsupportedOperationException("Deal.canMoveTo(): Should never be called directly.");
    }

}