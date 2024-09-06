package game.rules.play.moves.nonDecision.effect;

import annotations.Opt;
import game.Game;
import game.equipment.component.Component;
import game.rules.play.moves.BaseMoves;
import game.rules.play.moves.Moves;
import game.types.board.SiteType;
import game.types.state.GameType;
import main.Constants;
import other.action.Action;
import other.action.move.move.ActionMove;
import other.action.others.ActionPass;
import other.context.Context;
import other.move.Move;

import java.util.BitSet;

public class DrawCard extends Effect {
    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------

    /**
     * @param then The moves applied after that move is applied.
     * @example (playCard)
     */
    public DrawCard
    (
            @Opt final Then then
    ) {
        super(then);
    }

    @Override
    public Moves eval(final Context context)
    {
        final Moves moves = new BaseMoves(super.then());
        final DrawCard drawCard = new DrawCard(null);
        final Move move = new Move(String.valueOf(drawCard));
        move.setFromNonDecision(Constants.OFF);
        move.setToNonDecision(Constants.OFF);
        move.setMover(context.state().mover());
        moves.moves().add(move);
        if (then() != null)
            for (int j = 0; j < moves.moves().size(); j++)
                moves.moves().get(j).then().add(then().moves());

        return moves;

    }
}

    //-------------------------------------------------------------------------

