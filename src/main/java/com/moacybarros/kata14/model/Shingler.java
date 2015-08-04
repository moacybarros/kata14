package com.moacybarros.kata14.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.CanReadFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import com.moacybarros.kata14.dao.TrigramDAO;

/**
 * Class responsible to process the input files and generate the trigrams.
 * A shingle is another name for a "token based" n-gram
 * 
 * @author Moacy Barros
 *
 */
public class Shingler {
	Collection<File> files;

	private static final Logger logger = Logger
			.getLogger(Shingler.class.getName());

	/**
	 * Creates a shingler instance to populate DAO object.
	 * 
	 * @param dir - The directory of where source files are.
	 * 
	 * 
	 */
	public Shingler(String dir) {
		files = getFolderContents(dir);
	}

	/**
	 * Populates the storage/DAO with trigrams
	 * 
	 * @param min - The minimum n-gram size - must be at least 2.
	 * @param max - The maximum n-gram size.
	 */
	public void execute(int min, int max, TrigramDAO dao) {
		// Each document will have a ShingleThread parsing it
		ExecutorService shingleExecutor = Executors.newFixedThreadPool(files
				.size());

		for (Iterator<File> iter = files.iterator(); iter.hasNext();) {
			shingleExecutor.execute(new ShingleThread(iter.next(), min, max,dao));
		}

		shingleExecutor.shutdown();
		
		//Join point to synch all threads
		try {
			shingleExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			} catch (InterruptedException e) {
				logger.error("Error joining all threads before finish input process",e);
			}
	}

	/**
	 * Returns all the files in a directory.
	 * 
	 * @param dir - Path to the directory that contains the text documents to be parsed.
	 * 
	 * @return files located on the dir directory
	 */
	private Collection<File> getFolderContents(String dir) {
		// Collect all readable documents
		File file = new File(dir);
		Collection<File> files = FileUtils.listFiles(file,
				CanReadFileFilter.CAN_READ, DirectoryFileFilter.DIRECTORY);
		return files;
	}

	/**
	 * Thread implementation to process files in parallel. Shinglers generation are supported by Apache Lucene lib 
	 * 
	 * @author Moacy Barros
	 * 
	 * @see Runnable
	 * @see org.apache.lucene.analysis.SimpleAnalyzer
	 * @see org.apache.lucene.analysis.TokenStream;
	 * @see org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
	 * @see org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
	 * @see org.apache.lucene.util.Version;
	 *
	 */
	static class ShingleThread implements Runnable {
		File file;
		int min;
		int max;
		TrigramDAO index;

		/**
		 * Contructor with no storage object
		 * 
		 * @param file file to be processed
		 * @param min	minimum shingler size
		 * @param max	maximum shingler size
		 */
		public ShingleThread(File file, int min, int max) {
			this.file = file;
			this.min = min;
			this.max = max;
		}
		
		/**
		 * Contructor with storage object
		 * 
		 * @param file file to be processed
		 * @param min	minimum shingler size
		 * @param max	maximum shingler size
		 * @param output	storage object to keep generated trigram indexes
		 */
		public ShingleThread(File file, int min, int max, TrigramDAO output ) {
			this.file = file;
			this.min = min;
			this.max = max;
			this.index = output;
		}

		/**
		 * Thread start point
		 */
		public void run() {
			try {
				FileReader reader = new FileReader(file);

				// Parse the file into n-gram tokens
				SimpleAnalyzer simpleAnalyzer = new SimpleAnalyzer(Version.LUCENE_36);
				ShingleAnalyzerWrapper shingleAnalyzer = new ShingleAnalyzerWrapper(
						simpleAnalyzer, min, max," ",false,false);
				
				TokenStream stream = shingleAnalyzer.tokenStream("contents",
						reader);
				CharTermAttribute charTermAttribute = stream
						.getAttribute(CharTermAttribute.class);

				// Store them in the repository/TrigramDAO
				ArrayList<String> gram = new ArrayList<String>();
				while (stream.incrementToken()) {
					this.index.addTrigram(charTermAttribute.toString().split(" "));
					gram.clear();
				}
				shingleAnalyzer.close();
				logger.info(file.getName() + " completed.");
			} catch (IOException e) {
				logger.error("Parse Failed.  Reason: " + e.getMessage(), e);
			}
		}
	}
}
