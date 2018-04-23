package lt.dstulgis.metasite.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * DTO representing multi-file upload for words counting.
 */
public class WordCountRequest {

    private MultipartFile[] files;

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }
}
