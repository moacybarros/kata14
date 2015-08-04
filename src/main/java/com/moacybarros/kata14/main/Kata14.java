package com.moacybarros.kata14.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import com.moacybarros.kata14.dao.TrigramDAO;
import com.moacybarros.kata14.dao.impl.TrigramDAOMemoryImpl;
import com.moacybarros.kata14.model.Shingler;

/**
 * Main class, cadidated to implement solution for Kata14 published on http://codekata.com/kata/kata14-tom-swift-under-the-milkwood/
 * 
 * @author Moacy Barros
 *
 */
public class Kata14
{
	private static final Logger logger = Logger.getLogger(Kata14.class);
	private TrigramDAO dao = new TrigramDAOMemoryImpl();
	private static int MIN_NGRAM_ORDER = 3;
	private static int MAX_NGRAM_ORDER = 3;
	private static String WHITE_SPACE = " ";
	private static String SLASH = "/";
	private static String OUTPUT_FILE_NAME = "output.txt";

	/**
	 * Creates n-grams, order 3, from text documents and generate trigram indexes and generate a new text file
	 * 
	 * @param args - See help, it describe usage 
	 */
	public static void main(String[] args)
	{		
		logger.info("Execution started");
		// Print options
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar kata14-0.0.1-SNAPSHOT-jar-with-dependencies.jar", getOptions());
		
		// Parse options
		CommandLineParser parser = new GnuParser();
		try
		{
			CommandLine line = parser.parse(getOptions(), args);
			String indir = line.getOptionValue("indir");
			String outdir = line.getOptionValue("outdir");

			final Kata14 main = new Kata14();
			main.loadTrigrams(indir);
			main.writeFile(main.generateText(),outdir);
			
		} 
		
		catch (ParseException e)
		{
			logger.error("Parsing failed.  Reason: " + e.getMessage());
		}		
	}
	
	/**
	 * Build the options menu
	 * 
	 * @return options with menu items
	 */
	private static Options getOptions()
	{
		logger.info("Option menu creation.");
		// Define options
		Option indir = OptionBuilder.withArgName("path").hasArg().withDescription("The directory where the literature is stored.").isRequired().create("indir");
		Option outdir = OptionBuilder.withArgName("path").hasArg().withDescription("The directory where the new text will be stored.").isRequired().create("outdir");
		
		Options options = new Options();
		options.addOption(indir);
		options.addOption(outdir);
		
		return options;		
	}	
	
	/**
	 * load trigrams based on the files under dir path
	 * 
	 * @param dir path to the directory where the files to generate trigrams are
	 */
	private void loadTrigrams(final String dir) {
		logger.info("Trigram loading started.");
		// Create Shingler NGrams for order 3
		Shingler shingler = new Shingler(dir);
		shingler.execute(MIN_NGRAM_ORDER, MAX_NGRAM_ORDER, dao);	
	}
	
	/**
	 * Generate a new text based on trigram index
	 * 
	 * @return new text generated
	 */
	private StringBuilder generateText() {
		logger.info("Text generation started.");
		StringBuilder generatedText = new StringBuilder();

		if (dao.hasData()) {
			StringBuilder nextKey = new StringBuilder();

			String seed = dao.getRandomKey();
			String value = null;
			generatedText.append(seed).append(WHITE_SPACE);

			while ((value = dao.nextTrigram(seed)) != null) {
				generatedText.append(value).append(WHITE_SPACE);
				nextKey.setLength(0);
				seed = nextKey.append(seed.split("\\s")[1]).append(WHITE_SPACE)
						.append(value).toString();
			}

		} else {
			logger.info("There are input data to process.");
		}
		return generatedText;
	}
	
	/**
	 * Create new file with the content of textToPersist parameter on directory mentioned on path param
	 * 
	 * @param textToPersist	file content
	 * @param path	path to the bew file
	 */
	private void writeFile(final StringBuilder textToPersist, final String path) {
		logger.info("Persisting new text to file: "+ path + SLASH + OUTPUT_FILE_NAME);
		File file = new File(path + SLASH + OUTPUT_FILE_NAME);
		BufferedWriter writer = null;
		try {
			try {
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(textToPersist.toString());
			} catch (IOException e) {
				logger.error("Error during output file creation - " + e.getMessage());
			}
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error("Error during output file creation - " + e.getMessage());
				}
			}
		}
	}
}
