package ca.mcgill.cs.comp303.rummy.model;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.HashSet;
import java.util.Iterator;

import ca.mcgill.cs.comp303.rummy.model.Card.Rank;
import ca.mcgill.cs.comp303.rummy.model.Card.Suit;
import ca.mcgill.cs.comp303.rummy.model.CardSet.SetType;

/**
 * Models a hand of 10 cards. The hand is not sorted. Not threadsafe.
 * The hand is a set: adding the same card twice will not add duplicates
 * of the card.
 * @inv size() > 0
 * @inv size() <= HAND_SIZE
 */
public class Hand
{
	public static final int HAND_SIZE = 10;
	public static final int MIN_MATCHED_SIZE = 3;
	private HashSet<Card> aHand;
	private HashSet<ICardSet> aMatchedSets;
	
	/**
	 * Creates a new, empty hand.
	 */
	public Hand()
	{
		aHand = new HashSet<Card>();
		aMatchedSets = new HashSet<ICardSet>();
	}
	
	/**
	 * Adds pCard to the list of unmatched cards.
	 * If the card is already in the hand, it is not added.
	 * @param pCard The card to add.
	 * @throws HandException if the hand is complete.
	 * @throws HandException if the card is already in the hand.
	 * @pre pCard != null
	 */
	public void add( Card pCard ) throws HandException
	{		
		if (this.contains(pCard)) // throw the exceptions
		{
			throw new HandException("The hand already contains " + pCard.toString() + ".");
		}
		else if (this.isComplete())
		{
			throw new HandException("The hand is already complete; no new cards may be added.");
		}
		else
		{
			aHand.add(pCard);
		}
	}
	
	/**
	 * Remove pCard from the hand and break any matched set
	 * that the card is part of. Does nothing if
	 * pCard is not in the hand.
	 * @param pCard The card to remove.
	 * @pre pCard != null
	 */
	public void remove( Card pCard )
	{
		if(aHand.remove(pCard))								// if there was a card removed
		{
			for (ICardSet aMatchedSet : getMatchedSets())	// check if the card was part of a match set
			{
				if (aMatchedSet.contains(pCard))			// if it was, then remove the match set as it is no longer a match without the card
				{
					removeMatchedSet(aMatchedSet);
					break;
				}
			}
		}
	}
	
	/**
	 * @return True if the hand is complete.
	 */
	public boolean isComplete()
	{
		return aHand.size() >= HAND_SIZE;
	}
	
	/**
	 * Removes all the cards from the hand.
	 */
	public void clear()
	{
		aHand.clear();
	}
	
	/**
	 * @return A copy of the set of matched sets.
	 */
	public HashSet<ICardSet> getMatchedSets()
	{
		return new HashSet<ICardSet>(aMatchedSets);
	}
	
	/**
	 * @return A copy of the set of unmatched cards.
	 */
	public HashSet<Card> getUnmatchedCards()
	{
		HashSet<Card> hand = new HashSet<Card>(aHand);
		
		for (ICardSet matchedSet : getMatchedSets())
		{
			for (Card card : matchedSet)
			{
				hand.remove(card);
			}
		}
		
		return hand;
	}
	
	/**
	 * Add a matched set to the set of matched sets.
	 * @param pMatchedSet The matched set to add.
	 * @pre pMatchSet != null
	 * @return True if the matched sets did not already contain the matched set.
	 */
	public boolean addMatchedSet(ICardSet pMatchedSet)
	{
		return aMatchedSets.add(pMatchedSet);
	}
	
	/**
	 * Remove a matched set from the set of matched sets.
	 * @param pMatchedSet The matched set to remove.
	 * @pre pMatchSet != null
	 * @return True if the match set was removed.
	 */
	public boolean removeMatchedSet(ICardSet pMatchedSet)
	{
		return aMatchedSets.remove(pMatchedSet);
	}
	
	/**
	 * @return The number of cards in the hand.
	 */
	public int size()
	{
		return aHand.size();
	}
	
	/**
	 * Determines if pCard is already in the hand, either as an
	 * unmatched card or as part of a set.
	 * @param pCard The card to check.
	 * @return true if the card is already in the hand.
	 * @pre pCard != null
	 */
	public boolean contains( Card pCard )
	{
		return aHand.contains(pCard);
	}
	
	/**
	 * @return The total point value of the unmatched cards in this hand.
	 */
	public int score()
	{
		return getUnmatchedCards().size();
	}
	
	/**
	 * Verifies that a group of cards of the same rank exists in <b>pCards</b>
	 * @param pCards The cards to groups
	 * @pre pCards != null
	 * @return <b>true</b> if <b>pCards</b> makes a group, <b>false</b> otherwise
	 */
	private boolean groupExists( HashSet<Card> pCards )
	{
		if (pCards.size() < MIN_MATCHED_SIZE)
		{
			return false;
		}
		
		Rank groupRank = null;
		HashSet<Card> unmatched = getUnmatchedCards();
		
		for (Card card : pCards)
		{
			if (unmatched.contains(card))	// make sure all the cards are still unmatched
			{
				if (groupRank == null)
				{
					groupRank = card.getRank();
				}
				else if (card.getRank().compareTo(groupRank) != 0)
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		// only a valid group gets this far
		return true;
	}
	
	/**
	 * Verifies that a run of cards of the same suit exists in <b>pCards</b>
	 * <p>
	 * The algorithm works by checking:
	 * <ol>
	 * <li>That there are enough cards to make a run
	 * <li>That all cards are of the same suit
	 * <li>That there exists an ordering of the cards such that all cards are consecutive.
	 * <br>It does step 3 by:
	 *     <ul>
	 *     <li>Finding the lowest rank, then adding the number of cards to get 'nTotal'
	 *     <li>Using the sum formula (n*(n + 1))/2 to get sums s1 and s2,
	 *     	   with n1=nTotal and n2=lowest rank
	 *     <li>Subtracting s2 from s1 to get the sum of the ranks for the theoretical value
	 *     <li>Checking the theoretical value matches the actual sum of the ranks
	 *     </ul>
	 * </ol>
	 * 
	 * @param pCards The cards to group into a run
	 * @pre pCards != null
	 * @return <b>true</b> if <b>pCards</b> makes a run, <b>false</b> otherwise
	 * 
	 */
	private boolean runExists( HashSet<Card> pCards)
	{
		int numCards = pCards.size();
		
		if (numCards < MIN_MATCHED_SIZE)
		{
			return false;
		}
		
		Suit runSuit = null;
		Rank lowestRank = null;
		int rankSum = 0;
		HashSet<Card> unmatched = getUnmatchedCards();
		
		for (Card card : pCards)
		{
			if (unmatched.contains(card))	// make sure all the cards are still unmatched
			{
				if (runSuit == null)	// this is the first card being examined in the set
				{
					runSuit = card.getSuit();
					lowestRank = card.getRank();
				}
				else if (card.getSuit().compareTo(runSuit) != 0)	// there are two cards that don't have the same suit
				{
					return false;
				}
				else	// suit is the same as the last card's
				{
					if (card.getRank().compareTo(lowestRank) < 0)
					{
						lowestRank = card.getRank();
					}
				}
				
				rankSum += card.getRank().ordinal() + 1;	// +1 since ordinals start at 0
			}
			else
			{
				throw new HandException("The card (" + card.toString() + ") is already matched.");
			}
		}
		
		// only a set of cards made up of the same suit gets this far
		
		int lowest = lowestRank.ordinal();
		int n = lowest + numCards;
		int expectedSum = (n*(n + 1))/2 - (lowest*(lowest + 1))/2;	// use expected sum for consecutive sequence to verify run validity
		
		if (expectedSum != rankSum)	// the cards are the same suit, but don't form a sequence
		{
			return false;
		}
		
		// only a valid run gets this far
		return true;
	}
	
	/**
	 * Adds a group-set to the hand's set of matched sets if a group can be constructed from the given cards
	 * @param pCards The cards to make the group from
	 * @throws HandException If <b>pCards</b> does not make a valid group
	 */
	public void createGroup( HashSet<Card> pCards ) throws HandException
	{		
		HandException invalidGroupException = new HandException("The cards don't construct a valid group.");
		
		if (groupExists(pCards))
		{
			addMatchedSet(new CardSet(pCards, SetType.GROUP));
		}
		else
		{
			throw invalidGroupException;
		}
	}
	
	/**
	 * Adds a run-set to the hand's set of matched sets if a run can be constructed from the given cards
	 * @param pCards The cards to make the run from
	 * @throws HandException If <b>pCards</b> does not make a valid run
	 */
	public void createRun( HashSet<Card> pCards ) throws HandException
	{
		HandException invalidRunException = new HandException("The cards don't construct a valid run.");
				
		if (runExists(pCards))
		{
			addMatchedSet(new CardSet(pCards, SetType.RUN));
		}
		else
		{
			throw invalidRunException;
		}
	}
	
	/**
	 * Calculates the matching of cards into groups and runs that
	 * results in the lowest amount of points for unmatched cards.
	 */
	public void autoMatch()
	{
		// get all the possible matches that can be made by the cards in the hand
		HashSet<HashSet<Card>> allMatches = getAllPossibleMatches(aHand);
		
		// find the optimal solution
		HashSet<HashSet<Card>> optimal = optimalMatches(allMatches);		
		
		for (HashSet<Card> match : optimal)
		{
			// convert each optimal match to its corresponding CardSet
			Iterator<Card> iterator = match.iterator();
			Card first = iterator.next();
			Card second = iterator.next();
			// if the ranks of the two cards are the same, the current match must be a group, otherwise a run
			if (first.getRank() == second.getRank())
			{
				// match is a group
				CardSet group = new CardSet(match, SetType.GROUP);
				// clear, then add to hand's matched sets
				aMatchedSets.add(group);
			}
			else
			{
				// match is a run
				CardSet run = new CardSet(match, SetType.RUN);
				aMatchedSets.add(run);
			}
		}
	}
	
	/**
	 * Calculates all the possible matches that can be made from the cards in <b>pCards</b>
	 * @param pCards The cards to make the matches from
	 * @pre pCards != null
	 * @return A HashSet of matched Card HashSets
	 */
	private HashSet<HashSet<Card>> getAllPossibleMatches(HashSet<Card> pCards)
	{
		HashSet<HashSet<Card>> matched = new HashSet<HashSet<Card>>();
		
		if (pCards.size() > MIN_MATCHED_SIZE)
		{
			for (Card card : pCards)
			{
				// make a set from all the cards except the current card being looked at
				HashSet<Card> complement = new HashSet<Card>(pCards);
				complement.remove(card);
				
				// recursive call to get all the possible matches of the set without the card
				HashSet<HashSet<Card>> complementMatches = getAllPossibleMatches(complement);
				
				for (HashSet<Card> compMatch : complementMatches)
				{
					if (runExists(compMatch) || groupExists(compMatch))
					{
						matched.add(new HashSet<Card>(compMatch));
					}
					compMatch.add(card);
					if (runExists(compMatch) || groupExists(compMatch))
					{
						matched.add(new HashSet<Card>(compMatch));
					}
				}
			}
		}
		else if (runExists(pCards) || groupExists(pCards))
		{
			matched.add(new HashSet<Card>(pCards));
		}
		
		return matched;
	}
	
	/**
	 * Calculates the arrangement of cards into groups/runs such that the best score is achieved.
	 * @param pRemainingMatches The set of card sets to arrange into optimal groups and runs
	 * @return The set of optimal groups/runs
	 */
	private HashSet<HashSet<Card>> optimalMatches(HashSet<HashSet<Card>> pRemainingMatches)
	{
		HashSet<HashSet<Card>> optimal = new HashSet<HashSet<Card>>();;
		int optimalPoints = aHand.size();
		
		// for each match, get its compatible matches
		for (HashSet<Card> match : pRemainingMatches)
		{
			HashSet<HashSet<Card>> compatible = new HashSet<HashSet<Card>>();
			
			// find the compatible matches
			for (HashSet<Card> potentialCompatible : pRemainingMatches)
			{
				boolean isCompatible = true;
				
				for (Card card : match)
				{
					if (potentialCompatible.contains(card))
					{
						isCompatible = false;
						break;
					}
				}
				
				if (isCompatible)
				{
					compatible.add(potentialCompatible);
				}
			}
			
			HashSet<HashSet<Card>> optimalCompatible = optimalMatches(compatible);
			
			// add the current match to create the full set of matches
			optimalCompatible.add(match);
			
			// calculate the points associated with this setup
			int currentPoints = aHand.size();
			for (HashSet<Card> matched : optimalCompatible)
			{
				currentPoints -= matched.size();
			}
			
			// compare to last iteration and keep this arrangement if it results in less points (better), or equal points w/ less sets
			if ((currentPoints < optimalPoints) || ((currentPoints == optimalPoints) && (optimalCompatible.size() < optimal.size())))
			{
				optimal = new HashSet<HashSet<Card>>(optimalCompatible);
				optimalPoints = currentPoints;
			}
		}
		
		return optimal;
	}
}
