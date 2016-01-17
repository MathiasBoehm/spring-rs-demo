package de.struktuhr.rs.web.error.converter;

import de.struktuhr.rs.web.error.RestError;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * User: mathiasboehm
 * Date: 19.12.14
 * Time: 21:10
 */
public interface ExceptionToRestErrorConverter {

    /**
     * Check, if current Converter can convert given Exception into a RestError instance
     * @param e
     * @return
     */
    boolean canConvert(Exception e);

    /**
     * Converts an given Exception into a RestError instance.
     *
     * @param servletWebRequest
     * @param e
     * @return
     */
    RestError convert(ServletWebRequest servletWebRequest, Exception e);
}
