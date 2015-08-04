package com.moacybarros.kata14.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.moacybarros.kata14.dao.TrigramDAO;
import com.moacybarros.kata14.dao.impl.TrigramDAOMemoryImpl;
import com.moacybarros.kata14.main.Kata14;
import com.moacybarros.kata14.model.Trigram;


/**
 * Test case class responsible to test main class Kata14
 * 
 * @author Moacy Barros
 * 
 * @see com.moacybarros.kata14.main.Kata14
 *
 */
public class Kata14Test {

	TrigramDAO daoMock; 
	Trigram trigram ;
	Kata14 kata14;
	Method generateTextMethod;
	Method writeFileMethod;
	Field daoField;
	
	/**
	 * Setup method, executed before each test method
	 * 
	 * @see org.junit.Before
	 */
	@Before
	public void setup() {
		daoMock = EasyMock.createMock(TrigramDAOMemoryImpl.class);
		kata14 = new Kata14();	
	}
	
	/**
	 * Test text generation
	 * 
	 * @see com.moacybarros.kata14.main.Kata14#generateText()
	 * @see org.junit.Test
	 */
	@Test
	public void generateTextTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException{
		EasyMock.reset(daoMock);
		EasyMock.expect(daoMock.hasData()).andReturn(true);
        EasyMock.expect(daoMock.nextTrigram("No man")).andReturn("is");
        EasyMock.expect(daoMock.nextTrigram("man is")).andReturn("an");
        EasyMock.expect(daoMock.nextTrigram("is an")).andReturn("island");
        EasyMock.expect(daoMock.nextTrigram("an island")).andReturn(null);
        EasyMock.expect(daoMock.getRandomKey()).andReturn("No man");
        EasyMock.replay(daoMock);

      //retrieve private method via reflection
		generateTextMethod = kata14.getClass().getDeclaredMethod("generateText",new Class[] {});
		generateTextMethod.setAccessible(true);
		daoField = kata14.getClass().getDeclaredField("dao");
		daoField.setAccessible(true);
		daoField.set(kata14, daoMock);
		
	    StringBuilder stringB = (StringBuilder) generateTextMethod.invoke(kata14, new Class[] {});
		assertEquals("No man is an island ", stringB.toString());
	}
	
	/**
	 * Test text generation
	 * 
	 * @see com.moacybarros.kata14.main.Kata14#writeFile(final StringBuilder textToPersist, final String path)
	 * @see org.junit.Test
	 */
	@Test
	public void testWriteFile() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		StringBuilder content = new StringBuilder("Dumy file content");

		writeFileMethod = kata14.getClass().getDeclaredMethod("writeFile", new Class[] { StringBuilder.class , String.class });
		writeFileMethod.setAccessible(true);

		writeFileMethod.invoke(kata14, content, Kata14Test.class.getResource("/").getPath());
		
		assertTrue(new File(getClass().getResource("/output.txt").getFile()).exists());
	}
}
