package game.equipment.component;

import game.rules.play.moves.Moves;
import game.types.board.StepType;
import game.types.play.RoleType;
import game.util.directions.DirectionFacing;

import java.io.Serial;
import java.io.Serializable;

public class Card extends Component implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;
    public Card(String label, RoleType role, StepType[][] walk, DirectionFacing dirn, Moves generator, Integer maxState, Integer maxCount, Integer maxValue) {
        super(label, role, null, dirn, generator, maxState, maxCount, maxValue);
    }
}
