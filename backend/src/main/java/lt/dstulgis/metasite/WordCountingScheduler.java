package lt.dstulgis.metasite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Schedules threads for multi-file word counting.
 * File validation must be done before calling this service.
 *
 * @author dstulgis
 */
public class WordCountingScheduler {

    private static final Logger log = LoggerFactory.getLogger(WordCountingScheduler.class);

    @Value(value = "${outputDir}")
    private String outputDir;

    private ResultsToFileMap resultsForAGWords;
    private ResultsToFileMap resultsForHNWords;
    private ResultsToFileMap resultsForOUWords;
    private ResultsToFileMap resultsForVZWords;

    private boolean initialized = false;

    /**
     * Counts words in given text files.
     *
     * @param files list of text files to run computation on
     */
    public void countWords(File ... files) {
        // Update initial state for multiple runs and clear possible old results
        this.init();
        log.debug("Initializing...");
        CountingServiceStatus.updateStatus(false);
        resultsForAGWords.clear();
        resultsForHNWords.clear();
        resultsForOUWords.clear();
        resultsForVZWords.clear();

        // Count words
        log.debug("Counting words.");
        Observable.from(files)
                .subscribeOn(Schedulers.computation())
                .doOnNext(file -> file.deleteOnExit())
                .map(file -> WordCounter.matchAndAddWords(file))
                .toBlocking()
                .subscribe(wordsMap -> concatResults(wordsMap));

        // Write results to files
        log.debug("Collecting results.");
        Observable.just(resultsForAGWords, resultsForHNWords, resultsForOUWords, resultsForVZWords)
                .subscribeOn(Schedulers.io())
                .doOnNext(result -> result.writeToFile())
                .toCompletable()
                .doAfterTerminate(() -> CountingServiceStatus.updateStatus(true))
                .subscribe();
    }

    /**
     * Initializes results containers.
     * This is done not in class construction time since spring property placeholder by default injects after beans creation.
     */
    private void init() {
        if (!initialized) {
            resultsForAGWords = new ResultsToFileMap(Paths.get(outputDir, "A-G.txt"));
            resultsForHNWords = new ResultsToFileMap(Paths.get(outputDir, "H-N.txt"));
            resultsForOUWords = new ResultsToFileMap(Paths.get(outputDir, "O-U.txt"));
            resultsForVZWords = new ResultsToFileMap(Paths.get(outputDir, "V-Z.txt"));
        }
    }

    /**
     * @return list of result strings in format of "word: count".
     */
    public List<String> getResults() {
        if (CountingServiceStatus.isCompleted()) {
            return Stream.of(resultsForAGWords, resultsForHNWords, resultsForOUWords, resultsForVZWords)
                    .flatMap(map -> map.entrySet().stream())
                    .map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.toList());
        }
        log.debug("Results were requested before computation completion.");
        return Collections.EMPTY_LIST;
    }

    private void concatResults(Map<String, Long> wordsMap) {
        wordsMap.entrySet().stream().forEach(entry -> {
            switch (findBounds(entry.getKey())) {
                case 1: concatResult(entry, resultsForAGWords);
                    break;
                case 2: concatResult(entry, resultsForHNWords);
                    break;
                case 3: concatResult(entry, resultsForOUWords);
                    break;
                case 4: concatResult(entry, resultsForVZWords);
                    break;
                default: throw new IllegalArgumentException("Word is starting with unknown character: " + entry.getKey());
            }
        });
    }

    private int findBounds(String word) {
        char firstLetter = word.charAt(0);
        if (firstLetter >= 'a' && firstLetter <= 'g') {
            return 1;
        } else if (firstLetter >= 'h' && firstLetter <= 'n') {
            return 2;
        } else if (firstLetter >= 'o' && firstLetter <= 'u') {
            return 3;
        } else if (firstLetter >= 'v' && firstLetter <= 'z') {
            return 4;
        } else {
            return -1;
        }
    }

    private void concatResult(Map.Entry<String,Long> entry, ResultsToFileMap resultsForWords) {
        Long existingValue = Optional.ofNullable(resultsForWords.get(entry.getKey())).orElse(0L);
        resultsForWords.put(entry.getKey(), existingValue + entry.getValue());
    }

}
