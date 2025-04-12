package other.action.cards;

import java.util.BitSet;
import java.util.Objects;

import game.rules.play.moves.Moves;
import game.types.component.SuitType;
import other.action.Action;
import other.action.ActionType;
import other.action.BaseAction;
import other.concept.Concept;
import other.context.Context;

/**
 * Sets the trump suit for card games.
 *
 * @author Eric.Piette
 */
public class ActionSetTrump extends BaseAction
{
	private static final long serialVersionUID = 1L;

	//-------------------------------------------------------------------------

	/** The new trump suit. */
	private final int trumpSuit;
	String strTrumpSuit = null;

	//-------------------------------------------------------------------------
	
	/** A variable to know that we already applied this action so we do not want to modify the data to undo if apply again. */
	private boolean alreadyApplied = false;
	
	/** The previous trump suit. */
	private int previousTrumpSuit;
	
	//-------------------------------------------------------------------------

	/**
	 * @param trumpSuit The trump suit.
	 */
	public ActionSetTrump
	(
		final int trumpSuit
	)
	{
		this.trumpSuit = trumpSuit;
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
	/**
	 * Reconstructs an ActionSetTrumpSuit object from a detailed String (generated
	 * using toDetailedString())
	 *
	 * @param detailedString
	 */
	public ActionSetTrump(final String detailedString)
	{
		assert (detailedString.startsWith("[SetTrumpSuit:"));

		strTrumpSuit = Action.extractData(detailedString, "trumpSuit");
		trumpSuit = convertStringToNumber(strTrumpSuit);

		final String strDecision = Action.extractData(detailedString, "decision");
		decision = (strDecision.isEmpty()) ? false : Boolean.parseBoolean(strDecision);
	}

	//-------------------------------------------------------------------------

	@Override
	public Action apply(final Context context, final boolean store)
	{
		if(!alreadyApplied)
		{
			previousTrumpSuit = context.state().trump();
			alreadyApplied = true;
		}
		
		context.state().setTrump(trumpSuit);
		return this;
	}
	
	//-------------------------------------------------------------------------
	
	@Override
	public Action undo(final Context context, boolean discard)
	{
		context.state().setTrump(previousTrumpSuit);
		return this;
	}

	//-------------------------------------------------------------------------

	@Override
	public String toTrialFormat(final Context context)
	{
		final StringBuilder sb = new StringBuilder();

		sb.append("[SetTrumpSuit:");
		sb.append("trumpSuit=" + trumpSuit);
		if (decision)
			sb.append(",decision=" + decision);
		sb.append(']');

		return sb.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (decision ? 1231 : 1237);
		result = prime * result + trumpSuit;
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;

		if (!(obj instanceof ActionSetTrump))
			return false;

		final ActionSetTrump other = (ActionSetTrump) obj;

		return (trumpSuit == other.trumpSuit
				&& decision == other.decision);
	}

	//-------------------------------------------------------------------------

	@Override
	public String toTurnFormat(final Context context, final boolean useCoords)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("TrumpSuit = " + SuitType.values()[trumpSuit]);
		return sb.toString();
	}

	@Override
	public String getDescription()
	{
		return "SetTrumpSuit";
	}

	@Override
	public String toMoveFormat(final Context context, final boolean useCoords)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("(TrumpSuit = " + SuitType.values()[trumpSuit] + ")");
		return sb.toString();
	}

	//-------------------------------------------------------------------------

	@Override
	public boolean isOtherMove()
	{
		return true;
	}

	@Override
	public ActionType actionType()
	{
		return ActionType.SetTrump;
	}

	@Override
	public int what()
	{
		return trumpSuit;
	}

	//-------------------------------------------------------------------------

	@Override
	public BitSet concepts(final Context context, final Moves movesLudeme)
	{
		final BitSet concepts = new BitSet();
		
		if(decision)
			concepts.set(Concept.ChooseTrumpSuitDecision.id(), true);
		else
			concepts.set(Concept.SetTrumpSuit.id(), true);
		
		return concepts;
	}

}