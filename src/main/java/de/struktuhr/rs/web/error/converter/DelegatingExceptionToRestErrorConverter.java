package de.struktuhr.rs.web.error.converter;

import de.struktuhr.rs.web.error.RestError;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.List;

/**
 * User: mathiasboehm
 * Date: 21.12.14
 * Time: 13:57
 */
public class DelegatingExceptionToRestErrorConverter implements ExceptionToRestErrorConverter {

    private final List<ExceptionToRestErrorConverter> exceptionToRestErrorConverters;

    public DelegatingExceptionToRestErrorConverter(List<ExceptionToRestErrorConverter> exceptionToRestErrorConverters) {
        if (exceptionToRestErrorConverters == null) {
            throw new IllegalArgumentException("exceptionToRestErrorConverters must not be null");
        }
        this.exceptionToRestErrorConverters = exceptionToRestErrorConverters;
    }

    @Override
    public boolean canConvert(Exception e) {
        return (findResolver(e) != null);
    }

    @Override
    public RestError convert(ServletWebRequest servletWebRequest, Exception e) {
        RestError restError = null;

        ExceptionToRestErrorConverter resolver = findResolver(e);
        if (resolver != null) {
            restError = resolver.convert(servletWebRequest, e);
        }

        return restError;
    }

    private ExceptionToRestErrorConverter findResolver(Exception e) {
        ExceptionToRestErrorConverter foundResolver = null;
        for(ExceptionToRestErrorConverter resolver : exceptionToRestErrorConverters) {
            if (resolver.canConvert(e)) {
                foundResolver = resolver;
                break;
            }
        }
        return foundResolver;
    }
}
