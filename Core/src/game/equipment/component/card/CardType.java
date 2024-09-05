package game.equipment.component.card;

import java.io.Serializable;
import java.util.BitSet;

import annotations.Opt;
import game.Game;
import game.equipment.component.Component;
import game.rules.play.moves.Moves;
import game.types.play.RoleType;
import main.StringRoutines;
import metadata.graphics.util.ComponentStyleType;
import other.concept.Concept;

/**
 * Defines a single domino.
 *
 * @author Eric.Piette
 *
 * @remarks The domino defined with this ludeme will be not included in the
 *          dominoes container by default and so cannot be shuffled with other
 *          dominoes.
 */
public class CardType extends Component implements Serializable
{
    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------


    private String[] Attributes = null;
    /**
     * @param name       The name of the card.
     * @param role       The owner of the card.
     * @param generator  The moves associated with the component.
     * @param attributes
     * @example (domino " Domino45 " Shared value : 4 value2 : 5)
     */
    public CardType
    (
            final String   name,
            @Opt final RoleType role,
            @Opt final Moves generator,
            String[] attributes
    )
    {
        super(name, role,  null,
                null,
                generator, null, null, null);
        Attributes = attributes;

        nameWithoutNumber = StringRoutines.removeTrailingNumbers(name);

        style = ComponentStyleType.Domino;
    }

    //-------------------------------------------------------------------------

    /**
     * Copy constructor.
     * <p>
     * Protected because we do not want the compiler to detect it, this is called
     * only in Clone method.
     *
     * @param other
     */
    protected CardType(final CardType other)
    {
        super(other);
    }

    @Override
    public CardType clone()
    {
        return new CardType(this);
    }

    @Override
    public boolean isDoubleDomino()
    {
        return getValue() == getValue2();
    }

    @Override
    public boolean isDomino()
    {
        return true;
    }

    @Override
    public int numSides()
    {
        return 4;
    }

    @Override
    public boolean isTile()
    {
        return true;
    }

    @Override
    public BitSet concepts(final Game game)
    {
        final BitSet concepts = new BitSet();
        concepts.set(Concept.CanNotMove.id(), true);
        concepts.set(Concept.LargePiece.id(), true);
        concepts.set(Concept.Tile.id(), true);
        concepts.or(super.concepts(game));
        return concepts;
    }

    @Override
    public BitSet writesEvalContextRecursive()
    {
        final BitSet writeEvalContext = new BitSet();
        writeEvalContext.or(super.writesEvalContextRecursive());
        return writeEvalContext;
    }

    @Override
    public BitSet readsEvalContextRecursive()
    {
        final BitSet readEvalContext = new BitSet();
        readEvalContext.or(super.readsEvalContextRecursive());
        return readEvalContext;
    }

    @Override
    public boolean missingRequirement(final Game game)
    {
        boolean missingRequirement = false;
        if (role() != null)
        {
            final int indexOwnerPhase = role().owner();
            if (
                    (
                            indexOwnerPhase < 1
                                    &&
                                    !role().equals(RoleType.Shared)
                                    &&
                                    !role().equals(RoleType.Neutral)
                                    &&
                                    !role().equals(RoleType.All)
                    )
                            ||
                            indexOwnerPhase > game.players().count()
            )
            {
                game.addRequirementToReport(
                        "A domino is defined in the equipment with an incorrect owner: " + role() + ".");
                missingRequirement = true;
            }
        }
        return missingRequirement;
    }

    @Override
    public String toEnglish(final Game game)
    {
        //-----------------------------------------------------

        String string = nameWithoutNumber;

        String plural = StringRoutines.getPlural(nameWithoutNumber);
        string += plural;

        string += ", card named " + name();

        return string;

        //-----------------------------------------------------
    }
}
