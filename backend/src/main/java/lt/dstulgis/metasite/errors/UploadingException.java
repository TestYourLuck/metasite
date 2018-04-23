package lt.dstulgis.metasite.errors;

import org.springframework.http.ResponseEntity;

/**
 * Used for breaking upload early and store reason as {@link ResponseEntity} for response.
 *
 * @author dstulgis
 */
public class UploadingException extends RuntimeException {

    private ResponseEntity response;

    public UploadingException(ResponseEntity response) {
        super(response.getStatusCode().getReasonPhrase());
        this.response = response;
    }

    public UploadingException(ResponseEntity response, Throwable e) {
        super(response.getStatusCode().getReasonPhrase(), e);
        this.response = response;
    }

    public ResponseEntity getResponse() {
        return response;
    }
}
