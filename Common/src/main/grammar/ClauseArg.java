package main.grammar;

//-----------------------------------------------------------------------------

/**
 * Argument in a symbol's clause (i.e. a constructor) with an optional label.
 * 
 * @author cambolbro
 */
public class ClauseArg
{
	/** Local parameter name. */
	private String label = null;

	/** Symbol representing parameter type. */
	private Symbol symbol = null;

	/** Whether arg is [optional]. */
	private final boolean optional;

	/** Which @Or groups this arg belongs to (0 is none). */
	private final int orGroup;

	/** Which @And groups this arg belongs to (0 is none). */
	private final int andGroup;

	/** Degree of array nesting (0 for none). */
	private int nesting = 0;

	//-------------------------------------------------------------------------

	/**
	 * Constructor.
	 * 
	 * @param label
	 * @param symbol
	 * @param optional
	 */
	public ClauseArg
	(
		final Symbol symbol, final String label, final boolean optional, 
		final int orGroup, final int andGroup
	)
	{
		this.symbol = symbol;
		this.label = (label == null) ? null : new String(label);
		this.optional = optional;
		this.orGroup = orGroup;
		this.andGroup = andGroup;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param other
	 */
	public ClauseArg(final ClauseArg other)
	{
		label = (other.label == null) ? null : new String(other.label);
		symbol = other.symbol;
		optional = other.optional;
		orGroup = other.orGroup;
		andGroup = other.andGroup;
		nesting = other.nesting;
	}

	//-------------------------------------------------------------------------

	/**
	 * @return Local parameter name.
	 */
	public String label()
	{
		return label;
	}

	/**
	 * @return Symbol representing parameter type.
	 */
	public Symbol symbol()
	{
		return symbol;
	}

	/**
	 * @param val
	 */
	public void setSymbol(final Symbol val)
	{
		symbol = val;
	}

	/**
	 * @return Degree of nesting (array depth).
	 */
	public int nesting()
	{
		return nesting;
	}

	/**
	 * @param val
	 */
	public void setNesting(final int val)
	{
		nesting = val;
	}

	/**
	 * @return Whether arg is optional.
	 */
	public boolean optional()
	{
		return optional;
	}

	public int orGroup()
	{
		return orGroup;
	}

	public int andGroup()
	{
		return andGroup;
	}

	//-------------------------------------------------------------------------

	@Override
	public String toString()
	{
		String str = "";

		if (symbol == null)
			return "NULL";

		switch (symbol.ludemeType())
		{
		case Primitive:
//			System.out.println("** Arg " + symbol.name() + " is a primitive.");
			str = symbol.token();
			break;
		case Constant:
//			str = "<" + symbol.keyword() + ">";
			str = symbol.token();
			break;
		case Predefined:
			case Ludeme:
			case SuperLudeme:
			case SubLudeme:
			case Structural:
			// Hack to convert "<String>" args to "string" in the grammar 
			if (symbol.token().equals("String"))
				str = "string";
			else
				str = "<" + symbol.grammarLabel() + ">";
			break;
		default:
			str += "[UNKNOWN]";
		}

		for (int n = 0; n < nesting; n++)
			str = "{" + str + "}";

		if (label != null)
		{
			String labelSafe = new String(label);
			if (Character.isUpperCase(labelSafe.charAt(0)))
			{
				// First char is capital, probably If, Else, etc.
				labelSafe = Character.toLowerCase(labelSafe.charAt(0)) + labelSafe.substring(1);
			}
			str = labelSafe + ":" + str; // named parameter
		}

		if (optional)
			str = "[" + str + "]";

		return str;
	}

	//-------------------------------------------------------------------------

}
