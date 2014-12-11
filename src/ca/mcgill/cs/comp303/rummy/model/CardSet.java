package ca.mcgill.cs.comp303.rummy.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * An immutable set of Cards.
 */
public class CardSet implements ICardSet
{	
	/**
	 * Represents the type of this set; either a Run or a Group.
	 */
	public enum SetType
	{ RUN, GROUP }
	
	private final HashSet<Card> aCardSet;
	private final SetType aSetType;
	
	/**
	 * @param pSet The collection of cards to construct the CardSet with.
	 * @param pSetType The type of the CardSet; either Run or Group.
	 */
	public CardSet(Set<Card> pSet, SetType pSetType)
	{
		aCardSet = new HashSet<Card>(pSet);
		aSetType = pSetType;
	}

	@Override
	public Iterator<Card> iterator()
	{
		return aCardSet.iterator();
	}

	@Override
	public boolean contains(Card pCard)
	{
		return aCardSet.contains(pCard);
	}

	@Override
	public int size()
	{
		return aCardSet.size();
	}

	/**
	 * Whether this CardSet is a group or not.
	 * @pre size() >= 2
	 * @return true if this CardSet is a group.
	 */
	@Override
	public boolean isGroup()
	{
		return aSetType.compareTo(SetType.GROUP) == 0;
	}

	/**
	 * Whether this CardSet is a run or not.
	 * @pre size() >= 2
	 * @return true if this CardSet is a run.
	 */
	@Override
	public boolean isRun()
	{
		return aSetType.compareTo(SetType.RUN) == 0;
	}
	
	@Override
	public String toString()
	{
		return aCardSet.toString();
	}
}
