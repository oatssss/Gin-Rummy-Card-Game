package ca.mcgill.cs.comp303.rummy.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for the Deck class.
 */
public class TestDeck
{
	public static final int DECK_SIZE = 52;
	
	/**
	 * Test the creation of a deck is working.
	 */
	@Test
	public void deckSizeShould52()
	{		
		Deck deck = new Deck();
		assertTrue("New deck size should be 52.", deck.size() == DECK_SIZE);
	}
}
