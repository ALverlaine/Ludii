package game.rules.start.set.deck;

import game.Game;
import game.rules.start.StartRule;
import other.context.Context;

public class SetDeck extends StartRule
{
    private final String name;
    private final String[] cards;
    public SetDeck(String name, String[] cards) {
        this.name = name;
        this.cards = cards;
    }
    private static final long serialVersionUID = 1L;
    @Override
    public void eval(Context context) {

    }

    @Override
    public long gameFlags(Game game) {
        return 0;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public void preprocess(Game game) {

    }
}
