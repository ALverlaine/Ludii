package game.equipment.container.deck;

import java.util.BitSet;

import annotations.Name;
import annotations.Opt;
import game.Game;
import game.equipment.container.Container;
import game.functions.dim.DimConstant;
import game.functions.graph.generators.basis.hex.RectangleOnHex;
import game.functions.graph.generators.basis.square.RectangleOnSquare;
import game.functions.graph.generators.basis.tri.RectangleOnTri;
import game.types.board.SiteType;
import game.util.graph.Face;
import game.util.graph.Graph;
import main.Constants;
import metadata.graphics.util.ContainerStyleType;
import other.ItemType;
import other.concept.Concept;
import other.topology.Cell;
import other.topology.Topology;
import other.topology.Vertex;

/**
 * Defines a deck of cards or components.
 *
 * @author Eric.Piette
 *
 * @remarks Represents any stack of components not directly owned by a player.
 */
public class Deck extends Container
{
    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------

    /** The number of locations in this container. */
    protected int numLocs;

    /** The name of the deck. */
    protected String deckName;

    //-------------------------------------------------------------------------

    /**
     * @param name The name of the deck.
     * @param size The number of sites in the deck.
     *
     * @example (deck "STOCK" 32)
     */
    public Deck
    (
            final String name,
            @Opt final Integer size
    )
    {
        super(name, Constants.UNDEFINED, null);

        // Set the name of the deck.
        this.deckName = name;
        this.setName(name);

        // Set the size of the deck (default 1 if not provided).
        this.numLocs = (size == null) ? 1 : size.intValue();

        // Set the container style to Deck.
        this.style = ContainerStyleType.Deck;
        setType(ItemType.Deck);
    }

    /**
     * Copy constructor.
     *
     * Protected because we do not want the compiler to detect it, this is called
     * only in Clone method.
     *
     * @param other
     */
    protected Deck(final Deck other)
    {
        super(other);
        numLocs = other.numLocs;
        deckName = other.deckName;
    }

    //-------------------------------------------------------------------------

    @Override
    public void createTopology(final int beginIndex, final int numEdge)
    {
        final double unit = 1.0 / numLocs;

        topology = new Topology();
        final int realNumEdge = (numEdge == Constants.UNDEFINED) ? 4 : numEdge;

        Graph graph = null;

        if (realNumEdge == 6)
            graph = new RectangleOnHex(new DimConstant(1), new DimConstant(this.numLocs)).eval(null, SiteType.Cell);
        else if (realNumEdge == 3)
            graph = new RectangleOnTri(new DimConstant(1), new DimConstant(this.numLocs)).eval(null, SiteType.Cell);
        else
            graph = new RectangleOnSquare(new DimConstant(1), new DimConstant(this.numLocs), null, null).eval(null,
                    SiteType.Cell);

        // Add the cells to the topology.
        for (int i = 0; i < this.numLocs; i++)
        {
            final Face face = graph.faces().get(i);
            final Cell cell = new Cell(face.id() + beginIndex, face.pt().x() + (i * unit), face.pt().y(),
                    face.pt().z());
            cell.setCoord(cell.row(), cell.col(), 0);
            cell.setCentroid(face.pt().x(), face.pt().y(), 0);
            topology.cells().add(cell);

            // We add the vertices of the cells and vice versa.
            for (final game.util.graph.Vertex v : face.vertices())
            {
                final double x = v.pt().x();
                final double y = v.pt().y();
                final double z = v.pt().z();
                final Vertex vertex = new Vertex(Constants.UNDEFINED, x, y, z);
                cell.vertices().add(vertex);
            }
        }

        numSites = topology.cells().size();
    }

    //-------------------------------------------------------------------------

    /**
     * @return The number of sites in this deck.
     */
    public int numLocs()
    {
        return this.numLocs;
    }

    //-------------------------------------------------------------------------

    /**
     * @return The name of this deck.
     */
    public String deckName()
    {
        return this.deckName;
    }

    //-------------------------------------------------------------------------

    @Override
    public Deck clone()
    {
        return new Deck(this);
    }

    @Override
    public boolean isDeck()
    {
        return true;
    }

    //-------------------------------------------------------------------------

    @Override
    public BitSet concepts(final Game game)
    {
        final BitSet concepts = new BitSet();
        concepts.or(super.concepts(game));
        concepts.set(Concept.Deck.id(), true);
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

        // No owner validation necessary for Deck as it belongs to no player.
        if (deckName == null || deckName.isEmpty())
        {
            game.addRequirementToReport("A deck is defined without a valid name.");
            missingRequirement = true;
        }

        return missingRequirement;
    }
}
