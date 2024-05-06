package other;

public class CardSites {
    private int count = 0;
    private final int[] sites;

    //-------------------------------------------------------------------------

    /**
     * Constructor.
     *
     * @param count
     */
    public CardSites(final int count)
    {
        this.count = count;
        this.sites = new int[count];
        for (int n = 0; n < count; n++)
            this.sites[n] = n;
    }

    /**
     * Constructor.
     *
     * @param sites
     */
    public CardSites(final int[] sites) {
        this.count = sites.length;
        this.sites = new int[this.count];
    }
}
