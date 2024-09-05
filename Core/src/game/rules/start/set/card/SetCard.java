package game.rules.start.set.card;

import game.Game;
import game.rules.start.StartRule;
import other.context.Context;

public class SetCard extends StartRule {

    private static final long serialVersionUID = 1L;
    private final String typeName;
    private final String cardName;
    private final String[] cardProperties;

    public SetCard(String typeName, String cardName, String[] cardProperties) {
        this.typeName = typeName;
        this.cardName = cardName;
        this.cardProperties = cardProperties;

    }
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
