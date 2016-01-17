package de.struktuhr.rs.web.error.converter;

import org.springframework.context.MessageSource;

/**
 * User: mathiasboehm
 * Date: 19.12.14
 * Time: 21:15
 */
public class DefaultExceptionToRestErrorConverter extends ResourceBundleExceptionToRestErrorConverter {

    private final String DEFAULT_KEY = "default";

    public DefaultExceptionToRestErrorConverter(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    protected String createKey(Exception e) {
        return DEFAULT_KEY;
    }
}
