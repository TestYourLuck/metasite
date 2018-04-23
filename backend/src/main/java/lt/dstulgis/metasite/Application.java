package lt.dstulgis.metasite;

import lt.dstulgis.metasite.config.SpringConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringConfig.class, args);
    }

}
