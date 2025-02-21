package game.functions.booleans.card;

import game.Game;
import game.equipment.component.card.CardType;
import game.equipment.container.Container;
import game.functions.booleans.BaseBooleanFunction;
import game.functions.ints.IntFunction;
import game.types.board.SiteType;
import game.util.moves.From;
import other.context.Context;
import other.state.container.ContainerState;

import java.util.Arrays;

public class HasAttribute extends BaseBooleanFunction {

    private static final long serialVersionUID = 1L;
    private final From from;
    private final String cardAttribute;

    public HasAttribute(
            final From from,
            final String cardAttribute
    )
    {
        this.from = from;
        this.cardAttribute = cardAttribute;
    }
    @Override
    public boolean eval(Context context) {
        IntFunction toCompare = from.loc();
        final int cid = context.containerId()[toCompare.eval(context)];
        final ContainerState hid = context.state().containerStates()[cid];
        final int wat = hid.what(toCompare.eval(context), SiteType.Cell);
        final CardType card = (CardType) context.components()[wat];
       for(String attribute : card.getAttributesName()) {
            if(attribute.equals(cardAttribute)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public long gameFlags(Game game) {
        return 0;
    }

    @Override
    public void preprocess(Game game) {

    }
}
