package de.struktuhr.rs.web.error;

import org.springframework.http.HttpStatus;

/**
 * User: mathiasboehm
 * Date: 19.12.14
 * Time: 19:55
 */
public class RestError {

    private final HttpStatus status;
    private final String code;
    private final String message;
    private final String developerMessage;

    public RestError(HttpStatus status, String code, String message, String developerMessage) {
        if(status == null) {
            throw new IllegalArgumentException("status must not be null");
        }
        this.status = status;
        this.code = code;
        this.message = message;
        this.developerMessage = developerMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

}
