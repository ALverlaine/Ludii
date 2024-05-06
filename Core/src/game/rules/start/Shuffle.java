package game.rules.start;

import game.Game;
import game.equipment.component.Component;
import game.equipment.container.Container;
import game.types.component.DealableType;
import game.types.state.GameType;
import other.context.Context;
import other.state.container.ContainerState;

import java.util.BitSet;

/**
 * To shuffle the deck.
 */
public class Shuffle extends StartRule {
    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public Shuffle() {
        // Constructor
    }

    //-------------------------------------------------------------------------

    @Override
    public void eval(Context context) {
        shuffleDeck(context);
    }

    /**
     * To shuffle the deck.
     *
     * @param context The game context.
     */
    public void shuffleDeck(Context context) {
        for (Container container : context.containers()) {
            if (container.isDeck()) {
                shuffleContainer(container, context);
            }
        }
    }

    /**
     * Shuffle the components inside a container.
     *
     * @param container The container to shuffle.
     * @param context   The game context.
     */
    private void shuffleContainer(Container container, Context context) {
        Component[] components = context.components();

        // Perform shuffle algorithm here

        // For example:
        // Implement a shuffling algorithm like Fisher-Yates shuffle
        // (https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle)

        // Dummy implementation:
        for (int i = components.length - 1; i > 0; i--) {
            int j = context.rng().nextInt(i + 1);
            // Swap components[i] and components[j]
            Component temp = components[i];
            components[i] = components[j];
            components[j] = temp;
        }
    }

    //-------------------------------------------------------------------------

    @Override
    public BitSet writesEvalContextRecursive() {
        return new BitSet(); // No writes to eval context
    }

    @Override
    public BitSet readsEvalContextRecursive() {
        return new BitSet(); // No reads from eval context
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public long gameFlags(Game game) {
        return GameType.Stochastic; // Shuffling involves randomness
    }

    @Override
    public void preprocess(Game game) {
        // No preprocessing needed for shuffling
    }

    //-------------------------------------------------------------------------

    @Override
    public String toString() {
        return "(Shuffle)";
    }

    //-------------------------------------------------------------------------

    @Override
    public String toEnglish(Game game) {
        return "shuffle the deck";
    }
}

