package game.functions.ints.value.card;

import java.util.Arrays;
import java.util.BitSet;
import annotations.Hide;
import annotations.Name;
import annotations.Opt;
import game.Game;
import game.equipment.Equipment;
import game.equipment.component.Component;
import game.equipment.component.card.CardType;
import game.functions.ints.BaseIntFunction;
import game.functions.ints.IntConstant;
import game.functions.ints.IntFunction;
import game.types.board.SiteType;
import game.types.state.GameType;
import main.Constants;
import other.BaseLudeme;
import other.concept.Concept;
import other.context.Context;
import other.state.State;
import other.state.container.ContainerState;
import other.state.stacking.BaseContainerStateStacking;

/**
 * Returns the value of a card attribute.
 *
 * @remarks Useful for games where cards have attributes with specific values.
 */
@Hide
public final class ValueCard extends BaseIntFunction {
    private static final long serialVersionUID = 1L;

    /**
     * The attribute to check.
     */
    private final String cardAttribute;

    /**
     * The location to check.
     */
    private final IntFunction loc;
    private final IntFunction level;
    private SiteType type;

    /**
     * The type of site, e.g., Cell, Edge, Vertex.
     */
    //private final SiteType type;

    /**

     * @param cardAttribute The attribute of the card to retrieve.
     * @param at            The location to check.
     */
    public ValueCard
    (
            final String cardAttribute,
            @Opt       final SiteType    type,
            @Name final IntFunction at,
            @Opt @Name final IntFunction level
    ) {
        this.level = (level == null) ? new IntConstant(Constants.UNDEFINED) : level;
        this.cardAttribute = cardAttribute;
        this.loc = at;
        this.type = type;
    }
    private int convertStringToNumber(String attribute) {
        int sum = 0;
        for (char c : attribute.toCharArray()) {
            sum += (int) c; // Ajoute le code ASCII du caractère
        }
        return sum;
    }

    public int eval(final Context context) {
        // Évalue la localisation de la carte à partir du contexte
        final int location = loc.eval(context);
        // Vérifie si la carte est "OFF" (hors du jeu)
        if (location == Constants.OFF) {
            System.out.println("Location is OFF");
            return Constants.UNDEFINED; // ou toute autre valeur qui représente un cas "non trouvé"
        }
        // Récupère l'ID du conteneur associé à cette localisation
        final int containerId = context.containerId()[location];
        // Si le jeu n'est pas un jeu de Stacking, utilise le ContainerState basique


        final ContainerState cs = context.state().containerStates()[containerId];
        final int what = cs.what(location, type);
        final Component[] equipment = context.game().equipment().components();
        if(what == 0) {
            System.out.println("What is 0");
            return Constants.UNDEFINED;
        }
        final CardType cardType = (CardType) equipment[what];
        final String[] attributes = cardType.getAttributesValue();
        final String[] attributesName = cardType.getAttributesName();


        for (int i = 0; i < attributesName.length; i++) {
            if (attributesName[i].equals(cardAttribute)) {
                int value;
                try {
                    // Essaye de convertir en entier
                    value = Integer.parseInt(attributes[i]);
                } catch (NumberFormatException e) {
                    // Si ce n'est pas un entier, convertit la chaîne en fonction des caractères
                    value = convertStringToNumber(attributes[i]); // Conversion de la chaîne en entier basé sur les caractères
                }
                System.out.println("ValueCard: " + value + " for Attribute " + attributes[i]);
                return value;
            }
        }
        return Constants.UNDEFINED; // attributes wasnt found
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
        type = SiteType.use(type, game);
        loc.preprocess(game);
        level.preprocess(game);

    }
}