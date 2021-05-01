package service;

import model.Customer;

import javax.management.InstanceAlreadyExistsException;
import java.util.*;

public final class CustomerService {
    Map<String, Customer> mapOfCustomers = new HashMap<>();
    private static final CustomerService customerService = new CustomerService();

    /**
     * Service method. Adds a new Customer entry in the data structure. Checks if
     * the custromer already exists and throws IllegalArgumentException.
     * @throws IllegalArgumentException if customer already exists
     * @param email
     * @param firstName
     * @param lastName
     */
    public void addCustomer (String email, String firstName, String lastName) throws InstanceAlreadyExistsException {
        Customer newCustomer = new Customer(firstName, lastName, email);
        for(Customer customer: getAllCustomers()){
            if(customer.equals(newCustomer)){
                throw new InstanceAlreadyExistsException("Customer email already exists in database");
            }
        }
        mapOfCustomers.put(email, newCustomer);
    }

    public Customer getCustomer(String customerEmail) {
        return mapOfCustomers.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers() {
        return mapOfCustomers.values();
    }

    public static CustomerService getInstance() {

        return customerService;
    }

}

