package lt.dstulgis.metasite;

import lt.dstulgis.metasite.errors.ComputationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads counts words from given files and stores results into DB.
 *
 * @author dstulgis
 */
public class WordCounter {

    private static final Logger log = LoggerFactory.getLogger(WordCounter.class);

    private static final Pattern pattern = Pattern.compile("(\\w+)");

    /**
     * Counts words from given file.
     * File validation must be done before calling this service.
     *
     * @param file - text file to reads word from.
     */
    public static Map<String, Long> matchAndAddWords(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File must be passed for words counting.");
        }
        log.debug("Counting words from {}.", file.getName());

        HashMap<String, Long> wordCounts = new HashMap<>();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while((line = reader.readLine()) != null) {
                matchAndAddWords(wordCounts, line);
            }
        } catch (IOException e) {
            throw new ComputationException("Failed to read file for counting words.", e);
        }
        return wordCounts;
    }

    private static void matchAndAddWords(HashMap<String, Long> wordCounts, String line) {
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group().toLowerCase();
            Optional<Long> possibleValue = Optional.ofNullable(wordCounts.get(word));
            wordCounts.put(word, possibleValue.isPresent() ? possibleValue.get() + 1 : 1);
        }
    }
}
