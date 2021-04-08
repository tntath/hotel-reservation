package service;

import model.Customer;
import java.util.*;

public class CustomerService {
    Map<String, Customer> mapOfCustomers = new HashMap<>();
    private static final CustomerService customerService = new CustomerService();

    public void addCustomer(String email, String firstName, String lastName) {
        mapOfCustomers.put(email, new Customer(firstName, lastName, email));
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

