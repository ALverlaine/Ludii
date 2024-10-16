package game.equipment.container.board.custom;

import annotations.Name;
import annotations.Opt;
import annotations.Or;
import game.Game;
import game.equipment.container.board.Board;
import game.equipment.container.board.Track;
import game.functions.dim.DimConstant;
import game.functions.graph.BaseGraphFunction;
import game.functions.graph.generators.basis.square.Square;
import game.types.board.SiteType;
import game.types.board.StoreType;
import game.util.graph.Graph;
import other.context.Context;

import java.util.BitSet;

/**
 * Defines a TableCard-style board for a specific number of players.
 *
 * @example (TableCard 2)
 */
public class CardTable extends Board
{
    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------

    /** Number of players. */
    private final int numPlayers;

    //-------------------------------------------------------------------------

    /**
     * @param players   The number of players.
     * @param largeStack The game can involve stack(s) higher than 32.
     */
    public CardTable
        (
                final Integer players,
                @Opt @Name final Boolean largeStack,
                @Opt @Or final Track track,
                @Opt @Or      final Track[]   tracks
        )
{
    super(new BaseGraphFunction() {
        private static final long serialVersionUID = 1L;

        //-------------------------------------------------------------------------

        @Override
        public Graph eval(final Context context, final SiteType siteType)
        {
            Float[][] vertices = {
                    {0.0f, 0.0f},    // Vertex at (0,0)
                    {1.0f, 0.0f},    // Vertex at (1,0)
            };

            Integer[][] edges = {
                    {0, 1},          // Edge between Vertex 0 and Vertex 1
            };

            // If wrong parameter, no need of a graph.
            return new Graph(vertices, null);
        }
        @Override
        public long gameFlags(Game game) {
            return 0;
        }

        @Override
        public void preprocess(Game game) {

        }
    }, track, tracks, null, null, SiteType.Vertex, largeStack);

    // Store the parameter to access it later in TableCard logic.
    this.numPlayers = players.intValue();

    if (numPlayers < 2)
        throw new IllegalArgumentException("TableCard: Number of players must be at least 2.");
}

    //-------------------------------------------------------------------------

    /**
     * @return The number of players.
     */
    public int numPlayers()
    {
        return numPlayers;
    }

    //-------------------------------------------------------------------------



    @Override
    public String toEnglish(final Game game)
    {
        return numPlayers + "-player TableCard board.";
    }

    //-------------------------------------------------------------------------

    @Override
    public BitSet concepts(final Game game)
    {
        final BitSet concepts = new BitSet();
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
}
