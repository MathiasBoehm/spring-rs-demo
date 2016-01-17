package de.struktuhr.rs.web.error.converter.custom;

import de.struktuhr.rs.exception.CustomerNotFoundException;
import de.struktuhr.rs.web.error.RestError;
import de.struktuhr.rs.web.error.converter.ResourceBundleExceptionToRestErrorConverter;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Locale;

/**
 * User: mathiasboehm
 * Date: 21.12.14
 * Time: 14:53
 */
public class CustomerNotFoundExceptionToRestErrorConverter extends ResourceBundleExceptionToRestErrorConverter {

    public CustomerNotFoundExceptionToRestErrorConverter(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public boolean canConvert(Exception e) {
        return e instanceof CustomerNotFoundException;
    }

    @Override
    public RestError convert(ServletWebRequest servletWebRequest, Exception e) {
        Locale locale = resolveLocale(servletWebRequest);
        String key = createKey(e);

        HttpStatus httpStatus = HttpStatus.valueOf(findStatusCode(key));
        String code = "4040";
        String customerId = ((CustomerNotFoundException) e).getCustomerId();
        String message = messageSource.getMessage(createMessageKey(key), new Object[] {customerId}, null, locale);
        String developerMessage = e.getMessage();

        return new RestError(httpStatus, code, message, developerMessage);

    }
}
