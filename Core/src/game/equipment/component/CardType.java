package game.equipment.component;

import game.rules.play.moves.Moves;
import game.types.board.StepType;
import game.types.play.RoleType;
import game.util.directions.DirectionFacing;

import java.io.Serial;
import java.io.Serializable;

/**
 * Cette fonction crée un type de carte spécifique en prenant un nom de type et une liste d'attributs associés.
 *
 * @param cardType Le nom du type de carte à créer (par exemple, "UNO_CARD").
 * @param attributes Une collection d'attributs sous forme de paires clé-valeur, où chaque clé est le nom
 *                   de l'attribut (par exemple, "value", "suits") et la valeur correspondante représente
 *                   la caractéristique associée.
 *
 * La fonction retourne un objet représentant ce type de carte, qui peut ensuite être utilisé pour créer
 * des instances de cartes spécifiques conformes à ce type.
 */

public class CardType extends Component implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public CardType(String name, RoleType role, StepType[][] walk, DirectionFacing dirn, Moves generator, Integer maxState, Integer maxCount, Integer maxValue) {
        super(name, role, walk, dirn, generator, maxState, maxCount, maxValue);
    }
}
