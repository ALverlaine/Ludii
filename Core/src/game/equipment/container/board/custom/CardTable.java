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
            // Maximum supported players is 16
            if (players < 1 || players > 16) {
                // If wrong number of players, return an empty graph
                return new Graph(new Float[0][0], new Integer[0][0]);
            }

            // Define the radius of the circle for placing the vertices
            float radius = 1.0f;

            // Create arrays for vertices and edges
            Float[][] vertices = new Float[players][2];
            // Integer[][] edges = new Integer[players][2];

            // Distribute vertices around the circle
            for (int i = 0; i < players; i++) {
                // Calculate the angle for this vertex
                double angle = 2 * Math.PI * i / players;

                // Calculate the x and y coordinates
                float x = (float) (radius * Math.cos(angle));
                float y = (float) (radius * Math.sin(angle));

                // Store vertex
                vertices[i][0] = x;
                vertices[i][1] = y;

                // Create edge to the next vertex (looping back to the start)
                //edges[i][0] = i;
                //edges[i][1] = (i + 1) % players;
            }

            // Return the generated graph
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
