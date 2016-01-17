package de.struktuhr.rs.web.error.converter;

import de.struktuhr.rs.web.error.RestError;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Locale;

/**
 * User: mathiasboehm
 * Date: 21.12.14
 * Time: 14:14
 */
public class ResourceBundleExceptionToRestErrorConverter implements ExceptionToRestErrorConverter {

    protected final MessageSource messageSource;

    public ResourceBundleExceptionToRestErrorConverter(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public boolean canConvert(Exception e) {
        String key = createKey(e);
        return (findStatusCode(key) != null);
    }

    @Override
    public RestError convert(ServletWebRequest servletWebRequest, Exception e) {
        Locale locale = resolveLocale(servletWebRequest);
        String key = createKey(e);

        HttpStatus httpStatus = HttpStatus.valueOf(findStatusCode(key));
        String code = null;
        String message = findMessage(key, locale);
        String developerMessage = e.getMessage();

        return new RestError(httpStatus, code, message, developerMessage);
    }

    protected Integer findStatusCode(String key) {
        String s = messageSource.getMessage(key + ".status", null, null, Locale.GERMANY);
        Integer statusCode = null;
        if (s != null) {
            statusCode = new Integer(s);
        }
        return statusCode;
    }

    protected String findMessage(String key, Locale locale) {
        String status = messageSource.getMessage(createMessageKey(key), null, null, locale);
        return status;
    }

    protected Locale resolveLocale(ServletWebRequest servletWebRequest) {
        return servletWebRequest.getLocale();
    }


    protected String createKey(Exception e) {
        return e.getClass().getName();
    }


    protected String createMessageKey(String key) {
        return key + ".message";
    }

}
