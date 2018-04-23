package lt.dstulgis.metasite.app;

import lt.dstulgis.metasite.WordCountingScheduler;
import lt.dstulgis.metasite.errors.UploadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Endpoints for actions related to words counting service.
 * <ul>
 *   <li>Uploading files for words counting.</li>
 *   <li>Retrieving results.</li>
 * </ul>
 */
@RestController
public class WordCounterEndpoint {

    private static final Logger log = LoggerFactory.getLogger(WordCounterEndpoint.class);

    private static final long SIZE_LIMIT = 160000000L;  // 20 MB limit
    private static final String SIZE_LIMIT_STRING = (SIZE_LIMIT / 8 / 1024 / 1024) + "MB";  // 20 MB limit

    @Autowired
    private WordCountingScheduler service;

    /**
     * Meant only for checking application status.
     *
     * @return greeting response.
     */
    @RequestMapping(value = "/echo", method = RequestMethod.GET)
    public @ResponseBody String echo() {
        return "Hello!";
    }

    /**
     * Validates given files and passes them to words counting service.
     *
     * @param files text files to be parsed.
     */
    @RequestMapping(value = "/countWords", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity countWords(@RequestBody MultipartFile[] files) {
        List<File> tempFiles = new ArrayList<>();
        Observable.from(files).subscribeOn(Schedulers.io())
                .doOnNext(file -> validateFile(file))
                .map(file -> toTempFile(file))
                .toBlocking()
                .subscribe(file -> tempFiles.add(file));
        service.countWords(tempFiles.toArray(new File[]{}));
        return new ResponseEntity("Uploading... Results should be available at /metasite/results when completed.", HttpStatus.OK);
    }

    /**
     * @return List of results if they exist.
     */
    @RequestMapping(value = "/results", method = RequestMethod.GET)
    public @ResponseBody List<String> getResults() {
        return service.getResults();
    }

    private File toTempFile(MultipartFile file) {
        try {
            File tempFile = Files.createTempFile("UPLOADED", file.getOriginalFilename()).toFile();
            log.info("File is being uploaded: {}, size: {}", file.getOriginalFilename(), (file.getSize()/8) + "B");
            file.transferTo(tempFile);
            return tempFile;
        } catch (IOException e) {
            throw new UploadingException(new ResponseEntity("", HttpStatus.BAD_REQUEST), e);
        }
    }

    private void validateFile(MultipartFile file) {
        // validate
        if (!MediaType.TEXT_PLAIN_VALUE.equals(file.getContentType())) {
            throw new UploadingException(new ResponseEntity(
                    "Aborting word count since " + file.getOriginalFilename() + " is not text file.", HttpStatus.UNSUPPORTED_MEDIA_TYPE));
        }
        if (file.getSize() > SIZE_LIMIT) {
            throw new UploadingException(new ResponseEntity(
                    "Aborting word count. Files are larger than hard limit: " + SIZE_LIMIT_STRING, HttpStatus.PAYLOAD_TOO_LARGE));
        }
    }

}
