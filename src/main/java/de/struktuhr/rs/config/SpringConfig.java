package de.struktuhr.rs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.struktuhr.rs.web.error.converter.*;
import de.struktuhr.rs.web.error.converter.custom.CustomerNotFoundExceptionToRestErrorConverter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mathiasboehm
 * Date: 19.12.14
 * Time: 18:34
 */
@Configuration
@EnableWebMvc
@ComponentScan("de.struktuhr.rs")
//public class SpringConfig extends WebMvcConfigurerAdapter {
public class SpringConfig {

    @Bean
    public MessageSource errorsMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("nls.errors");
        return messageSource;
    }

    @Bean
    public ExceptionToRestErrorConverter restErrorResolver() {
        MessageSource messageSource = errorsMessageSource();
        List<ExceptionToRestErrorConverter> errorResolvers = new ArrayList<ExceptionToRestErrorConverter>();
        errorResolvers.add(new CustomerNotFoundExceptionToRestErrorConverter(messageSource));
        errorResolvers.add(new ResourceBundleExceptionToRestErrorConverter(messageSource));
        errorResolvers.add(new DefaultExceptionToRestErrorConverter(messageSource));

        return new DelegatingExceptionToRestErrorConverter(errorResolvers);
    }


    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper());
        return mappingJackson2HttpMessageConverter;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objMapper = new ObjectMapper();
        objMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objMapper;
    }

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        super.configureMessageConverters(converters);
//        converters.addAll(httpMessageConverters());
//    }

    @Bean
    public  List<HttpMessageConverter<?>> httpMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(mappingJackson2HttpMessageConverter());
        if (ClassUtils.isPresent("javax.xml.bind.Binder", SpringConfig.class.getClassLoader())) {
            converters.add(new Jaxb2RootElementHttpMessageConverter());
        }
        return converters;
    }
}
