package ca.mcgill.cs.comp303.rummy.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ca.mcgill.cs.comp303.rummy.testutils.AllCards;

/**
 * Test class for the Card class.
 */
public class TestCard
{
	/**
	 * Tests equals() for the Card class.
	 */
	@Test
	public void testEquals()
	{
		assertTrue(AllCards.CAC.equals(AllCards.CAC));
	}
	
	/**
	 * Tests compareTo() for the Card class.
	 */
	@Test
	public void testComparator()
	{
		assertTrue(AllCards.CAC.compareTo(AllCards.C2C) < 0);
	}
}
