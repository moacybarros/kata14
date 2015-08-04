package com.moacybarros.kata14.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.moacybarros.kata14.dao.TrigramDAO;
import com.moacybarros.kata14.dao.impl.TrigramDAOMemoryImpl;



/**
 * Test case class responsible to test class TrigramDaoMemoryImplTest
 * 
 * @author Moacy Barros
 * 
 * @see com.moacybarros.kata14.dao.TrigramDAO
 * @see com.moacybarros.kata14.dao.TrigramDaoMemoryImplTest
 *
 */
public class TrigramDaoMemoryImplTest {

	private TrigramDAO trigramDao;

	// keys
	private String key1;

	/**
	 * Setup method, executed before each test method
	 * 
	 * @see org.junit.Before
	 */
	@Before
	public void setup()  {
		trigramDao = new TrigramDAOMemoryImpl();
		
		//memory trigrams storage initialization
//		trigramDao = new TrigramDAOMemoryImpl();
//		trigramDao.addTrigram(new String[]{"i","wish","i"});
//		trigramDao.addTrigram(new String[]{"wish","i","may"});
//		trigramDao.addTrigram(new String[]{"i","may","i"});
//		trigramDao.addTrigram(new String[]{"may","i","i"});
		trigramDao = new TrigramDAOMemoryImpl();
		trigramDao.addTrigram(new String[]{"no","man","is"});
		trigramDao.addTrigram(new String[]{"man","is","an"});
		trigramDao.addTrigram(new String[]{"is","an","island"});
		trigramDao.addTrigram(new String[]{"an","island","no"});

		// key
		key1 = "no man";

	}

	/**
	 * Cleanup method, Executed after each test method
	 * 
	 * @see org.junit.After
	 */
	@After
	public void tearDown() {
		trigramDao = null;
		key1 = null;
	}

	/**
	 * Test hasData() method for positive scenario
	 * 
	 * @see com.moacybarros.kata14.dao.TrigramDAO#hasData()
	 * @see com.moacybarros.kata14.dao.TrigramDaoMemoryImplTest
	 * @see org.junit.Test
	 */
	@Test
	public void tesHasData() {
		assertTrue(trigramDao.hasData());
	}
	
	/**
	 * Test hasData() method for empty trigram collection
	 * 
	 * @see com.moacybarros.kata14.dao.TrigramDAO#hasData()
	 * @see com.moacybarros.kata14.dao.TrigramDaoMemoryImplTest
	 * @see org.junit.Test
	 */
	@Test
	public void testHasData() {
		trigramDao = new TrigramDAOMemoryImpl();
		assertFalse(trigramDao.hasData());
	}

	/**
	 * Test nextTrigram() method, get next trigram valuer
	 * 
	 * @see com.moacybarros.kata14.dao.TrigramDAO#nextTrigram()
	 * @see com.moacybarros.kata14.dao.TrigramDaoMemoryImplTest
	 * @see org.junit.Test
	 */
	@Test
	public void testNextTrigram() throws IllegalArgumentException,
			IllegalAccessException {

		assertEquals("is", trigramDao.nextTrigram(key1));
		assertEquals("is", trigramDao.nextTrigram(key1));
		assertEquals("is", trigramDao.nextTrigram(key1));
	}

	/**
	 * Test getRandomKey() method user to simulate a fair trigram selection
	 * 
	 * @see com.moacybarros.kata14.dao.TrigramDAO#getRandomKey()
	 * @see com.moacybarros.kata14.dao.TrigramDaoMemoryImplTest
	 * @see org.junit.Test
	 */
	@Test
	public void testGetRandomKey() {
		assertNotNull(trigramDao.getRandomKey());
		assertTrue(trigramDao.getRandomKey().matches(
				"no man|man is|is an|an island"));
	}

}
