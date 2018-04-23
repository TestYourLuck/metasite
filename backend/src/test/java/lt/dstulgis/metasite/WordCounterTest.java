package lt.dstulgis.metasite;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class WordCounterTest {

    private File file;

    @Before
    public void onSetup() {
        file = new File(this.getClass().getClassLoader().getResource("testWords.txt").getPath());
    }

    @Test
    public void shouldCountWords() {
        Map<String, Long> results = WordCounter.matchAndAddWords(file);

        assertThat(results.size(), equalTo(40));    // 40 unique from total 50 words
        assertThat(results.get("the"), equalTo(4L));
        assertThat(results.get("languages"), equalTo(2L));
        assertThat(results.get("same"), equalTo(2L));
        assertThat(results.get("their"), equalTo(4L));
        assertThat(results.get("a"), equalTo(2L));
        assertThat(results.get("common"), equalTo(2L));
    }

}
