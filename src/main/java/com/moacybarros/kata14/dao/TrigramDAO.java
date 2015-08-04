package com.moacybarros.kata14.dao;

import java.util.Map;

import com.moacybarros.kata14.model.Trigram;

/**
 * DAO Interface created to allow low coupling between application and storage mechanism
 * 
 * @author Moacy Barros
 * 
 *
 */
public interface TrigramDAO {

	/**
	 * Method responsible for 
	 * 
	 * @param trigram String[] with expected 3 String tokens, a Trigram
	 */
	public void addTrigram(String[] trigram);

	/**
	 * Retrieve Trigram by key
	 * 
	 * @param key key used to map values for the given trigram
	 * 
	 * @return Trigram which matches to the given key
	 * 
	 * @see com.moacybarros.kata14.model.Trigram
	 */
	public Trigram getTrigramByKey(String key);

	/**
	 * Retrieve trigram Map, which map keys to possible values
	 * 
	 * @return Map<String, Trigram> map of trigrams
	 * 
	 * @see com.moacybarros.kata14.model.Trigram
	 */
	public Map<String, Trigram> getTrigramMap();
	
	/**
	 * Check if index storage has data(), {@link com.moacybarros.kata14.dao.impl.TrigramDAOMemoryImpl#trigramMap}
	 * 
	 * @return boolean true if there is index entries, false if index storage is empty
	 */
	public boolean hasData();
	
	/**
	 * Select a pseudo random key
	 * 
	 * @return key selected randomly
	 */
	public String getRandomKey();
	
	/**
	 * Return next trigram value related to the given trigram key 
	 * 
	 * @param key key used to select the trigram
	 * 
	 * @return value next trigram value
	 */
	public String nextTrigram(final String key);

}
