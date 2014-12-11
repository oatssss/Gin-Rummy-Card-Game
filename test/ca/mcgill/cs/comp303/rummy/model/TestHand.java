package ca.mcgill.cs.comp303.rummy.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import ca.mcgill.cs.comp303.rummy.model.CardSet.SetType;
import ca.mcgill.cs.comp303.rummy.testutils.AllCards;

/**
 * Test class for the Hand class.
 */
public class TestHand
{
//	
//	/**
//	 * Verify createGroup() is functioning as expected.
//	 */
//	@Test
//	public void testCreateGroup()
//	{	
//		Hand aHand = new Hand();
//		aHand.add(AllCards.CAC);
//		aHand.add(AllCards.CAD);
//		aHand.add(AllCards.CAH);
//		aHand.add(AllCards.CAS);
//		
//		aHand.createGroup(aHand.getUnmatchedCards());
//		
//		assertTrue(aHand.getMatchedSets().iterator().next().isGroup());
//	}
//	
//	/**
//	 * Verify createRun() is functioning as expected.
//	 */
//	@Test
//	public void testCreateRun()
//	{	
//		Hand aHand = new Hand();
////		aHand.add(AllCards.CAC);
//		aHand.add(AllCards.C2C);
//		aHand.add(AllCards.C3C);
//		aHand.add(AllCards.C4C);
//		
//		aHand.createRun(aHand.getUnmatchedCards());
//		
//		assertTrue(aHand.getMatchedSets().iterator().next().isRun());
//	}
//	
//	@Test
//	public void testUnmatched()
//	{
//		Hand aHand = new Hand();
//		aHand.add(AllCards.CAC);
//		aHand.add(AllCards.CAD);
//		aHand.add(AllCards.CAH);
//		aHand.add(AllCards.CAS);
//		aHand.add(AllCards.C2C);
//		aHand.add(AllCards.C3C);
//		aHand.add(AllCards.C4C);
//		HashSet<Card> match = new HashSet<Card>();
//		match.add(AllCards.CAC);
//		match.add(AllCards.CAD);
//		match.add(AllCards.CAH);
//		aHand.createGroup(match);
//		
//		assertEquals(4, aHand.getUnmatchedCards().size());
//	}
	@Test
	public void testGetAllPossible()
	{
		Hand lHand = new Hand();
        lHand.add( AllCards.CAS );
        lHand.add( AllCards.C2S );
        lHand.add( AllCards.C3S );
        lHand.add( AllCards.C4S );
        lHand.add( AllCards.C4D );
        lHand.add( AllCards.C4C );
        lHand.add( AllCards.C2H );
        lHand.add( AllCards.C3H );
        lHand.add( AllCards.C4H );
        lHand.add( AllCards.C2D );
        HashSet<HashSet<Card>> all = lHand.getAllPossibleMatches(lHand.getUnmatchedCards());
        assertEquals(10, all.size());
	}
	
	@Test
	public void testOptimal()
	{
		Hand lHand = new Hand();
        lHand.add( AllCards.CAS );
        lHand.add( AllCards.C2S );
        lHand.add( AllCards.C3S );
        lHand.add( AllCards.C4S );
        lHand.add( AllCards.C4D );
        lHand.add( AllCards.C4C );
        lHand.add( AllCards.C2H );
        lHand.add( AllCards.C3H );
        lHand.add( AllCards.C4H );
        lHand.add( AllCards.C2D );
        HashSet<HashSet<Card>> all = lHand.getAllPossibleMatches(lHand.getUnmatchedCards());
        HashSet<HashSet<Card>> optimal = lHand.optimalMatches(all);
        lHand.autoMatch();
        System.out.println(lHand.getMatchedSets());
	}
}
