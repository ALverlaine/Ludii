package game.functions.region.cardSite;

import annotations.Name;
import annotations.Opt;
import annotations.Or;
import annotations.Or2;
import game.Game;
import game.functions.booleans.BooleanFunction;
import game.functions.ints.IntFunction;
import game.functions.region.BaseRegionFunction;
import game.functions.region.RegionFunction;
import game.functions.region.sites.SitesLoopType;
import game.functions.region.sites.context.SitesContext;
import game.types.board.SiteType;
import game.types.play.RoleType;
import game.util.directions.Direction;
import game.util.equipment.Region;
import other.context.Context;

public final class CardSites extends BaseRegionFunction {
    private static final long serialVersionUID = 1L;

    //-------------------------------------------------------------------------

    /**
     * For getting the sites iterated in ForEach Moves.
     *
     * @example (sites)
     */
    public static RegionFunction construct() {
        return new SitesContext();
    }

    //-------------------------------------------------------------------------

    /**
     * For getting the sites in a loop or making the loop.
     *
     * @param regionType   Type of sites to return.
     * @param inside       True to return the sites inside the loop [False].
     * @param type         The graph element type [default SiteType of the board].
     * @param surround     Used to define the inside condition of the loop.
     * @param surroundList The list of items inside the loop.
     * @param directions   The direction of the connection [Adjacent].
     * @param colour       The owner of the looping pieces [Mover].
     * @param start        The starting point of the loop [(last To)].
     * @param regionStart  The region to start to detect the loop.
     * @example (sites Loop)
     */
    public static RegionFunction construct
    (
            final SitesLoopType regionType,
            @Opt @Name final BooleanFunction inside,
            @Opt final SiteType type,
            @Or @Opt @Name final RoleType surround,
            @Or @Opt final RoleType[] surroundList,
            @Opt final Direction directions,
            @Opt final IntFunction colour,
            @Or2 @Opt final IntFunction start,
            @Or2 @Opt final RegionFunction regionStart
    ) {
        return null;

    }

    @Override
    public Region eval(Context context) {
        return null;
    }

    @Override
    public long gameFlags(Game game) {
        return 0;
    }

    @Override
    public void preprocess(Game game) {

    }
}
