package ca.mcgill.cs.comp303.rummy.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ca.mcgill.cs.comp303.rummy.testutils.AllCards;

/**
 * Test class for the Hand class.
 */
public class TestHand
{
	
	/**
	 * Verify createGroup() is functioning as expected.
	 */
	@Test
	public void testCreateGroup()
	{	
		Hand aHand = new Hand();
		aHand.add(AllCards.CAC);
		aHand.add(AllCards.CAD);
		aHand.add(AllCards.CAH);
		aHand.add(AllCards.CAS);
		
		aHand.createGroup(aHand.getUnmatchedCards());
		
		assertTrue(aHand.getMatchedSets().iterator().next().isGroup());
	}
	
	/**
	 * Verify createRun() is functioning as expected.
	 */
	@Test
	public void testCreateRun()
	{	
		Hand aHand = new Hand();
//		aHand.add(AllCards.CAC);
		aHand.add(AllCards.C2C);
		aHand.add(AllCards.C3C);
		aHand.add(AllCards.C4C);
		
		aHand.createRun(aHand.getUnmatchedCards());
		
		assertTrue(aHand.getMatchedSets().iterator().next().isRun());
	}
}
