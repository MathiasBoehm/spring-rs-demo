package de.struktuhr.rs.web.error;

import de.struktuhr.rs.web.error.converter.ExceptionToRestErrorConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Handles an exception which occured during processing a rest service call and render a formatted error.
 *
 * Some code fragments and ideas are borrowed from Lez Hazlewood and Jakub Jirutka
 * See: http://www.stormpath.com/blog/spring-mvc-rest-exception-handling-best-practices-part-1
 * See: http://github.com/jirutka/spring-rest-exception-error
 *
 * User: mathiasboehm
 * Date: 19.12.14
 * Time: 19:59
 */
@Component
public class RestReponseStatusExceptionResolver extends AbstractHandlerExceptionResolver {

    @Autowired
    private List<HttpMessageConverter<?>> httpMessageConverters;

    @Autowired
    private ExceptionToRestErrorConverter exceptionToRestErrorConverter;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

        ModelAndView modelAndView = null;

        ServletWebRequest webRequest = new ServletWebRequest(httpServletRequest, httpServletResponse);

        // Resolve error
        RestError restError = exceptionToRestErrorConverter.convert(webRequest, e);

        if (restError != null) {
            httpServletResponse.setStatus(restError.getStatus().value());

            // try to convert the error into a message and write the results back to the client
            try {
                modelAndView = handleResponseBody(restError, webRequest);
            }
            catch (Exception localEx) {
                logger.error("Getting ModelAndView for Exception [" + e + "] resulted in an exception." + localEx);
            }
        }

        return modelAndView;
    }


    private ModelAndView handleResponseBody(RestError restError, ServletWebRequest webRequest) throws ServletException, IOException {

        HttpInputMessage inputMessage = new ServletServerHttpRequest(webRequest.getRequest());

        List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        if (acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }

        MediaType.sortByQualityValue(acceptedMediaTypes);

        HttpOutputMessage outputMessage = new ServletServerHttpResponse(webRequest.getResponse());

        Class<?> bodyType = restError.getClass();

        List<HttpMessageConverter<?>> converters = httpMessageConverters;

        if (converters != null) {
            for (MediaType acceptedMediaType : acceptedMediaTypes) {
                for (HttpMessageConverter messageConverter : converters) {
                    if (messageConverter.canWrite(bodyType, acceptedMediaType)) {
                        messageConverter.write(restError, acceptedMediaType, outputMessage);

                        return new ModelAndView();
                    }
                }
            }
        }

        if (logger.isWarnEnabled()) {
            logger.warn("Could not find HttpMessageConverter that supports return type [" + bodyType +"] and " + acceptedMediaTypes);
        }

        return null;
    }

}
