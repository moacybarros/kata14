package com.moacybarros.kata14.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;


/**
 * Test case classe responsible to test Trigram abstraction class
 * 
 * @author Moacy Barros
 * 
 * @see com.moacybarros.kata14.model.Trigram
 *
 */
public class TrigramTest {
	Trigram trigramA;
	Trigram trigramB;

	// keys
	private String key1;
	private String key2;
	private String key3;
	private String key4;

	/**
	 * Setup method, executed before each test method
	 * 
	 * @see org.junit.Before
	 */
	@Before
	public void setup() {		
		// some keyss for "No man is an island"
		key1 = "no man";
		key2 = "man is";
		key3 = "is an";
		key4 = "an island";
		


		trigramA = new Trigram(key1, "is");
		trigramA.addValue("need");
		trigramB = new Trigram(key2, "an");
		trigramB.addValue("a");
	}

	/**
	 * Test offset/cursor rotation
	 * 
	 * @see com.moacybarros.kata14.model.Trigram#incrementOffset()
	 * @see org.junit.Test
	 */
	@Test
	public void testOffset() {
		assertEquals(0, trigramA.getOffset());
		trigramA.incrementOffset();
		assertEquals(1, trigramA.getOffset());
		// cursor/offset rotation execution
		trigramA.incrementOffset();
		assertEquals(0, trigramA.getOffset());
	}

	/**
	 * Test next value retrieving
	 * 
	 * @see com.moacybarros.kata14.model.Trigram#nextValue()
	 * @see org.junit.Test
	 */
	@Test
	public void testNextValue() {
		assertEquals("is", trigramA.nextValue());
		assertEquals("is", trigramA.nextValue());
		trigramA.incrementOffset();
		assertEquals("need", trigramA.nextValue());
		trigramA.incrementOffset();
		assertEquals("is", trigramA.nextValue());

		assertFalse("foo key".equalsIgnoreCase(trigramA.nextValue()));

		ArrayList<String> trigram = new ArrayList<String>(1);
		trigram.add("no");
		trigram.add("man");
		trigram.add("is");
		trigramA.setValues(trigram);
		trigramA.incrementOffset();
		assertEquals("man", trigramA.nextValue());
		trigramA.incrementOffset();
		assertEquals("is", trigramA.nextValue());
		trigramA.incrementOffset();
		assertEquals("no", trigramA.nextValue());
		trigramA.incrementOffset();
		assertEquals("man", trigramA.nextValue());
	}

	/**
	 * Test getKey method
	 * 
	 * @see com.moacybarros.kata14.model.Trigram#getKey()
	 * @see org.junit.Test
	 */
	@Test
	public void testGetKey() {
		assertEquals("no man", trigramA.getKey());
		assertEquals("man is", trigramB.getKey());

		trigramA.setKey("foo key");
		assertEquals("foo key", trigramA.getKey());
	}

}
