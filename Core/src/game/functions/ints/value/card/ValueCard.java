package game.functions.ints.value.card;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Objects;

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
    public static int convertStringToNumber(Object input) {
        if (input instanceof Integer) {
            // Manipuler l'entier pour le différencier d'un String
            return ((Integer) input) * 31;
        } else if (input instanceof String) {
            // Calculer un hash code pour la chaîne pour obtenir un entier unique
            return Objects.hash(input);
        } else {
            throw new IllegalArgumentException("Input must be either an Integer or a String");
        }
    }

    public int eval(final Context context) {
        // Évalue la localisation de la carte à partir du contexte
        final int location = loc.eval(context);

        // Vérifie si la carte est "OFF" (hors du jeu)
        if (location == Constants.OFF) {
            return Constants.UNDEFINED; // ou toute autre valeur qui représente un cas "non trouvé"
        }

        // Récupère l'ID du conteneur associé à cette localisation
        final int containerId = context.containerId()[location];

        // Si le jeu est un jeu de stacking, utilise le BaseContainerStateStacking
        if ((context.game().gameFlags() & GameType.Stacking) != 0) {
            final BaseContainerStateStacking state = (BaseContainerStateStacking) context.state()
                    .containerStates()[containerId];

            // Évalue le niveau
            final int evaluatedLevel = level.eval(context);

            // Récupère la carte au niveau donné ou au sommet si le niveau est -1
            final int what = (evaluatedLevel == -1)
                    ? state.what(location, type) // Sommet de la pile
                    : state.what(location, evaluatedLevel, type); // Niveau spécifique

            if (what == 0) {
                return Constants.UNDEFINED; // Pas de carte trouvée
            }

            // Récupère les informations de la carte
            final Component[] equipment = context.game().equipment().components();
            final CardType cardType = (CardType) equipment[what];
            final String[] attributes = cardType.getAttributesValue();
            final String[] attributesName = cardType.getAttributesName();

            // Recherche l'attribut correspondant
            for (int i = 0; i < attributesName.length; i++) {
                if (attributesName[i].equals(cardAttribute)) {
                    try {
                        // Essaye de convertir en entier
                        return Integer.parseInt(attributes[i]);
                    } catch (NumberFormatException e) {
                        // Si ce n'est pas un entier, convertit la chaîne en entier basé sur les caractères
                        return convertStringToNumber(attributes[i]);
                    }
                }
            }
        } else {
            // Si ce n'est pas un jeu de stacking, utilise le ContainerState basique
            final ContainerState cs = context.state().containerStates()[containerId];
            final int what = cs.what(location, type);

            if (what == 0) {
                return Constants.UNDEFINED; // Pas de carte trouvée
            }

            // Récupère les informations de la carte
            final Component[] equipment = context.game().equipment().components();
            final CardType cardType = (CardType) equipment[what];
            final String[] attributes = cardType.getAttributesValue();
            final String[] attributesName = cardType.getAttributesName();

            // Recherche l'attribut correspondant
            for (int i = 0; i < attributesName.length; i++) {
                if (attributesName[i].equals(cardAttribute)) {
                    try {
                        // Essaye de convertir en entier
                        return Integer.parseInt(attributes[i]);
                    } catch (NumberFormatException e) {
                        // Si ce n'est pas un entier, convertit la chaîne en entier basé sur les caractères
                        return convertStringToNumber(attributes[i]);
                    }
                }
            }
        }

        return Constants.UNDEFINED; // Attribut non trouvé
    }



    @Override
    public long gameFlags(Game game) {
        return 0L;
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