package lt.dstulgis.metasite.app;

import lt.dstulgis.metasite.errors.UploadingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Responses mapper for application exceptions.
 *
 * @author dstulgis
 */
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(UploadingException.class)
    public ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
        if (ex instanceof UploadingException) {
            return ((UploadingException) ex).getResponse();
        }
        return new ResponseEntity<>("Unhandled exception: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
