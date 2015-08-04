package com.moacybarros.kata14.dao.impl;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import com.moacybarros.kata14.dao.TrigramDAO;
import com.moacybarros.kata14.model.Trigram;

public class TrigramDAOMemoryImpl implements TrigramDAO{

	private static final Logger logger = Logger.getLogger(TrigramDAOMemoryImpl.class);
	private final Map<String, Trigram> trigramMap = new ConcurrentHashMap<String, Trigram>();
	private Random randomIntSource = new Random();
	
	
	/**
	 * Method responsible for 
	 * 
	 * @param trigram String[] with expected 3 String tokens, a Trigram
	 */
	public void addTrigram(String[] trigram){
		logger.debug("Adding new trigram: " +trigram);
		String key = trigram[0]+" "+trigram[1];
		String value = trigram[2];
		if(trigramMap.containsKey(key)){
			trigramMap.get(key).getValues().add(value);
		}else{
			trigramMap.put(key, new Trigram(key,value));
		}
	}
	
	/**
	 * Retrieve Trigram by key
	 * 
	 * @param key key used to map values for the given trigram
	 * 
	 * @return Trigram which matches to the given key
	 * 
	 * @see com.moacybarros.kata14.model.Trigram
	 */
	public Trigram getTrigramByKey(String key){
		if(trigramMap.containsKey(key)){
			return trigramMap.get(key);
		}else{
			return null;
		}		
	}
	
	/**
	 * Return next trigram value related to the given trigram key 
	 * 
	 * @param key key used to select the trigram
	 * 
	 * @return value next trigram value
	 */
	public String nextTrigram(final String key) {
		logger.debug("Return current trigram cursor index and update cursor to the next value.");
		String result = null;
		if (trigramMap.containsKey(key)) {
			Trigram trigram = ((Trigram) trigramMap.get(key));
			result = trigram.nextValue();
			trigram.incrementOffset();
		} else {
			// end of chain, meaning the text generation is finished
			logger.warn("There are no values for given key:" + key);
		}
		return result;
	}

	/**
	 * Retrieve trigram Map, which map keys to possible values
	 * 
	 * @return Map<String, Trigram> map of trigrams
	 * 
	 * @see com.moacybarros.kata14.model.Trigram
	 */
	public Map<String, Trigram> getTrigramMap() {
		return trigramMap;
	}
	
	/**
	 * Check if index storage has data(), {@link com.moacybarros.kata14.dao.impl.TrigramDAOMemoryImpl#trigramMap}
	 * 
	 * @return boolean true if there is index entries, false if index storage is empty
	 */
	public boolean hasData() {
		logger.debug("Cheking if there is data to process.");
		return !this.trigramMap.isEmpty();
	}
	
	/**
	 * Select a pseudo random key
	 * 
	 * @return key selected randomly
	 */
	public String getRandomKey() {
		logger.debug("Get a key value randomly from the trigramn keys list");
		Object[] values = trigramMap.values().toArray();
		Object randomValue = values[randomIntSource.nextInt(values.length)];
		String randomKey = ((Trigram) randomValue).getKey();

		return randomKey;

	}
	
}
