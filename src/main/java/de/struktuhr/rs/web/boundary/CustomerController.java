package de.struktuhr.rs.web.boundary;

import de.struktuhr.rs.entity.Customer;
import de.struktuhr.rs.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * User: mathiasboehm
 * Date: 19.12.14
 * Time: 19:27
 */
@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Customer getCustomer(@PathVariable("id") String id) {
        return customerService.getCustomer(id);
    }
}
