package lt.dstulgis.metasite;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class ResultsToFileMapTest {

    private ResultsToFileMap testSubject;

    private File outputFile;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void onSetup() {
        try {
            outputFile = folder.newFile();
        } catch (IOException e) {
            fail(e.getMessage());
        }

        testSubject = new ResultsToFileMap(Paths.get(outputFile.toURI()));
        testSubject.put("test1", 1L);
        testSubject.put("test2", 4L);
        testSubject.put("test3", 2L);
    }

    @Test
    public void shouldWriteAddedResults() {
        String[] possibleResults = new String[]{"test1: 1", "test2: 4", "test3: 2"};

        testSubject.writeToFile();
        List<String> resultLines = readResults();

        assertThat(resultLines.size(), equalTo(3));
        assertThat(resultLines.get(0), isIn(possibleResults));
        assertThat(resultLines.get(1), isIn(possibleResults));
        assertThat(resultLines.get(2), isIn(possibleResults));
    }

    @Test
    public void shouldOverrideResults() {
        testSubject.writeToFile();
        List<String> resultLines = readResults();

        assertThat(resultLines.size(), equalTo(3));

        testSubject.clear();
        testSubject.put("word", 9L);

        testSubject.writeToFile();
        resultLines = readResults();

        assertThat(resultLines.size(), equalTo(1));
        assertThat(resultLines.get(0), equalTo("word: 9"));
    }

    private List<String> readResults() {
        try {
            return Files.readAllLines(Paths.get(outputFile.toURI()));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return Collections.EMPTY_LIST;
    }

}
