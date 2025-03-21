package game.functions.booleans.card;

import game.Game;
import game.equipment.component.card.CardType;
import game.equipment.container.Container;
import game.equipment.container.other.Hand;
import game.functions.booleans.BaseBooleanFunction;
import game.functions.ints.IntFunction;
import game.types.board.SiteType;
import game.util.moves.From;
import other.context.Context;
import other.state.container.ContainerState;

import java.util.Arrays;

public class IsMaxAttribute extends BaseBooleanFunction {

    private static final long serialVersionUID = 1L;
    private final From from;
    private final String cardAttribute;

    public IsMaxAttribute(
            final From from,
            final String cardAttribute
    )
    {
        this.from = from;
        this.cardAttribute = cardAttribute;
    }
    private int convertStringToNumber(String attribute) {
        int sum = 0;
        for (char c : attribute.toCharArray()) {
            sum += (int) c; // Ajoute le code ASCII du caractère
        }
        return sum;
    }
    @Override
    public boolean eval(Context context) {
        IntFunction toCompare = from.loc();
        //get container id
        final int cid = context.containerId()[toCompare.eval(context)];
        //get hand id
        final ContainerState hid = context.state().containerStates()[cid];
        //get precise card
        final int wat = hid.what(toCompare.eval(context), SiteType.Cell);
        final CardType card = (CardType) context.components()[wat];
        for(int i = 0; i<context.components().length; i++)
        {


            try{
                System.out.println(hid.what(i, SiteType.Cell));
                int nwat = hid.what(i, SiteType.Cell);
                final CardType ncard = (CardType) context.components()[nwat];
                if(Integer.parseInt(card.getValue(cardAttribute)) < Integer.parseInt(ncard.getValue(cardAttribute))){
                    return false;
                }
            }
            catch (Exception e){
                continue;
            }
        }
        return true;
    }

    @Override
    public long gameFlags(Game game) {
        return 0;
    }

    @Override
    public void preprocess(Game game) {

    }
}
