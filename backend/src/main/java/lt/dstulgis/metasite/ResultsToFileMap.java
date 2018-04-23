package lt.dstulgis.metasite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Container for results map and utility to store them in configured file.
 *
 * @author dstulgis
 */
public class ResultsToFileMap {

    private static final Logger log = LoggerFactory.getLogger(ResultsToFileMap.class);

    private Path outputFile;

    public ResultsToFileMap(Path outputFile) {
        this.outputFile = outputFile;
    }

    protected ConcurrentHashMap<String, Long> results = new ConcurrentHashMap<>();

    public void writeToFile() {
        String outputLines = results.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));
        try {
            log.debug("Writing words to {} file.", outputFile.getFileName());
            Files.write(outputFile, outputLines.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("Failed writing results to {}", outputFile.getFileName(), e);
        }
    }

    public Long get(String key) {
        return results.get(key);
    }

    public Long put(String key, Long value) {
        return results.put(key, value);
    }

    public void clear() {
        results.clear();
    }

    public Set<Map.Entry<String, Long>> entrySet() {
        return results.entrySet();
    }
}
