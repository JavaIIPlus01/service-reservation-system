package guru.bug.courses.srs.control.exception;

import java.io.Serializable;

public class ServiceException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Exception e) {
        super(message, e);
    }
}
