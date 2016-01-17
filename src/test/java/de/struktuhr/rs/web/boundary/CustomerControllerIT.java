package de.struktuhr.rs.web.boundary;

import de.struktuhr.rs.service.CustomerService;
import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

/**
 * User: mathiasboehm
 * Date: 17.01.16
 * Time: 11:32
 */
public class CustomerControllerIT {

    interface LocalCustomerService {
        @RequestLine("GET /customers/{id}")
        LocalCustomer getCustomer(@Param("id") String id);
    }

    private LocalCustomerService cut;


    @Test
    public void checkSimpleGet() {
        LocalCustomer localCustomer = cut.getCustomer("3");
        assertNotNull(localCustomer);
        System.out.println(localCustomer);
    }

    @Test
    public void checkNotFound() {
        Exception caughtException = null;
        try {
            cut.getCustomer("4"); // does not exist
        }
        catch (Exception e) {
            caughtException = e;
        }
        assertNotNull(caughtException);
        System.out.println(caughtException);

    }

    @Test
    public void checkSimulateServerError() {
        Exception caughtException = null;
        try {
            cut.getCustomer("99"); // simulates a server error
        }
        catch (Exception e) {
            caughtException = e;
        }
        assertNotNull(caughtException);
        System.out.println(caughtException);

    }

    @Before
    public void setup() {
        cut = createCustomerService();
    }

    private LocalCustomerService createCustomerService() {
        Decoder decoder = new GsonDecoder();
        return Feign.builder()
                .decoder(decoder)
                .encoder(new GsonEncoder())
                .errorDecoder(new ServiceRuntimeErrorEncoder(decoder))
                .target(LocalCustomerService.class, "http://localhost:8080/rs");
    }

    static class LocalCustomer {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Customer{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    static class ServiceRuntimeErrorEncoder implements ErrorDecoder {

        final Decoder decoder;
        final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

        ServiceRuntimeErrorEncoder(Decoder decoder) {
            this.decoder = decoder;
        }

        public Exception decode(String methodKey, Response response) {
            try {
                return (Exception) decoder.decode(response, ServiceRuntimeError.class);
            } catch (IOException fallbackToDefault) {
                return defaultDecoder.decode(methodKey, response);
            }
        }
    }

    static class ServiceRuntimeError extends RuntimeException {
        private String status;
        private String code;
        private String message;
        private String developerMessage;

        public String getStatus() {
            return status;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String getMessage() {
            return message;
        }

        public String getDeveloperMessage() {
            return developerMessage;
        }

        @Override
        public String toString() {
            return "ServiceRuntimeError{" +
                    "status='" + status + '\'' +
                    ", code='" + code + '\'' +
                    ", message='" + message + '\'' +
                    ", developerMessage='" + developerMessage + '\'' +
                    '}';
        }
    }
}
