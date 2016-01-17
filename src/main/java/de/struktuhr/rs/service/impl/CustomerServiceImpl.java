package de.struktuhr.rs.service.impl;

import de.struktuhr.rs.entity.Customer;
import de.struktuhr.rs.exception.CustomerNotFoundException;
import de.struktuhr.rs.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: mathiasboehm
 * Date: 19.12.14
 * Time: 19:24
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<String, Customer> customers;

    public CustomerServiceImpl() {
        customers = new LinkedHashMap<>();
        customers.put("1", new Customer("1", "Peter"));
        customers.put("2", new Customer("2", "Paul"));
        customers.put("3", new Customer("3", "Mary"));
    }

    @Override
    public Customer getCustomer(String id) {
        Customer customer;
        if ("99".equals(id)) {
            throw new RuntimeException("Something bad happend with id " + id);
        }
        else {
            customer = customers.get(id);
            if (customer == null) {
                throw new CustomerNotFoundException("This customer with id " + id + " cannot be found", id);
            }
        }
        return customer;
    }
}
