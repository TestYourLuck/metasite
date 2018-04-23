package lt.dstulgis.metasite.config;

import lt.dstulgis.metasite.WordCountingScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author dstulgis
 */
@Configuration
@ComponentScan(basePackages = {"lt.dstulgis.metasite"})
public class SpringConfig {

    private WordCountingScheduler instance;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public WordCountingScheduler getWordCountingScheduler() {
        if (instance == null) {
            instance = new WordCountingScheduler();
        }
        return instance;
    }

}
