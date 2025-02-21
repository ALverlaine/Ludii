package game.rules.start.deal;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import annotations.Name;
import annotations.Opt;
import annotations.Or2;
import game.Game;
import game.equipment.component.Component;
import game.equipment.container.Container;
import game.functions.ints.IntFunction;
import game.functions.region.RegionFunction;
import game.functions.region.sites.Sites;
import game.rules.Rule;
import game.rules.start.Start;
import game.rules.start.StartRule;
import game.rules.start.forEach.team.ForEachTeam;
import game.types.board.SiteType;
import game.types.component.DealableType;
import game.types.play.RoleType;
import game.types.state.GameType;
import gnu.trove.list.array.TIntArrayList;
import main.Constants;
import other.action.Action;
import other.action.move.move.ActionMove;
import other.context.Context;
import other.move.Move;
import other.state.container.ContainerState;

/**
 * To deal different components between players.
 *
 * @author Eric.Piette
 */
public final class Deal extends StartRule
{
	private static final long serialVersionUID = 1L;

	//-------------------------------------------------------------------------

	/**
	 * @param type Type of deal.
	 * @param count The number of components to deal [1].
	 *
	 * @example (deal Dominoes 7)
	 */
	public static Rule construct
	(

			final DealableType type,
			@Opt final Integer count,
			@Opt @Name final Boolean stack
	)

	{
		switch (type)
		{
			case Dominoes:
				return new DealDominoes(count, stack);
			default:
				break;
		}
		// We should never reach that except if we forget some codes.
		throw new IllegalArgumentException("Deal(): A DealableType is not implemented.");
	}

	//-------------------------------------------------------------------------

	public static Rule construct
			(

					final DealableType type,
					final Integer count,
					final RoleType who,
					@Opt @Name final Boolean stack,
					@Opt @Name final String deckType
			)
	{
		switch (type)
		{
			case Cards:
				return new DealCards(count, who, stack, deckType);
			default:
				break;
		}
		// We should never reach that except if we forget some codes.
		throw new IllegalArgumentException("Deal(): A DealableType is not implemented.");
	}

	//-------------------------------------------------------------------------

	public static Rule construct
			(

					final DealableType type,
					@Opt final Integer count,
					@Opt final IntFunction who,
					@Opt @Name final Boolean stack,
					@Opt @Name final String deckType
			)
	{
		switch (type)
		{
			case Deck:
				return new DealDeck(count, who, stack, deckType);
			default:
				break;
		}
		// We should never reach that except if we forget some codes.
		throw new IllegalArgumentException("Deal(): A DealableType is not implemented.");
	}
	@Override
	public void eval(final Context context)
	{
		// Should not be called, should only be called on subclasses
		throw new UnsupportedOperationException("Deal.eval(): Should never be called directly.");
	}


	//-------------------------------------------------------------------------

	@Override
	public boolean isStatic()
	{
		// Should never be there
		return false;
	}


	@Override
	public long gameFlags(final Game game)
	{
		// Should never be there
		return 0L;
	}
	@Override
	public void preprocess(final Game game)
	{
		// Do nothing
	}

	//-------------------------------------------------------------------------

}