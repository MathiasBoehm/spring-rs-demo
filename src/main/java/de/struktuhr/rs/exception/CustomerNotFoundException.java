package de.struktuhr.rs.exception;

/**
 * User: mathiasboehm
 * Date: 19.12.14
 * Time: 19:38
 */
public class CustomerNotFoundException extends RuntimeException {

    private String customerId;

    public CustomerNotFoundException(String message, String customerId) {
        super(message);
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }
}
