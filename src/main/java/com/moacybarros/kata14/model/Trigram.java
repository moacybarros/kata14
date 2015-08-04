package com.moacybarros.kata14.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Abstraction for a n-gram which can of any order, for our case we just need trigrams, n-gram of order 3.
 * 
 * @author Moacy Barros
 *
 */
public class Trigram {
	private String key;
	private List<String> values;
	private int offset;
	private static final Logger logger = Logger.getLogger(Trigram.class);

	/**
	 * Constructor which receive only one value
	 * 
	 * @param key n-gram key
	 * @param value	n-gram first value
	 */
	public Trigram(final String key, final String value) {
	this.key = key;
	this.values = new ArrayList<String>();
	this.values.add(value);
	this.offset = 0;
	}
	
	/**
	 * Return n-gram key
	 * 
	 * @return ngram key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Set n-gram key
	 * 
	 * @param key n-gram key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Return the next value
	 * 
	 * @return next value
	 */
	public String nextValue() {
		String result = null;
		try {
			result = values.get(this.offset);
		} catch (ArrayIndexOutOfBoundsException outOfIndexException) {
			logger.warn("No values tor given key:" + this.key);
		}
		return result;
	}
	
	/**
	 * Return all values as a list of Strings
	 * 
	 * @return values list of strings
	 */
	public List<String> getValues() {
		return values;
	}

	/**
	 * Set a list of values
	 *
	 * @param values list of values
	 */
	public void setValues(List<String> values) {
		this.values = values;
	}

	/**
	 * Add a single value to the shingle list of values
	 * 
	 * @param value values to be added
	 */
	public void addValue(String value) {
		this.values.add(value);
	}
	
	/**
	 * Return current offset value
	 * 
	 * @return offset value
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Move offset to the next value or to the first value if the current value is the last one
	 * 
	 */
	public void incrementOffset() {
		this.offset = (this.offset == this.values.size() - 1) ? 0
				: this.offset + 1;
	}

	@Override
	public String toString() {
		return "Trigram [key=" + key + ", values=" + values + ", offset="
				+ offset + "]";
	}
	
	
}
