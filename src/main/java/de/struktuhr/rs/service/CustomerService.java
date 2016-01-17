package de.struktuhr.rs.service;

import de.struktuhr.rs.entity.Customer;

/**
 * User: mathiasboehm
 * Date: 19.12.14
 * Time: 19:23
 */
public interface CustomerService {


    Customer getCustomer(String id);
}
