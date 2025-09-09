package com.ofss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        customerRepository.findAll().forEach(customerList::add);
        return customerList;
    }


    public Optional<Customer> getCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    // PUT
    public Customer updateCustomer(Integer id, Customer customerDetails) {
        return customerRepository.findById(id).map(customer -> {
            customer.setCustName(customerDetails.getCustName());
            customer.setPhoneNo(customerDetails.getPhoneNo());
            customer.setEmail(customerDetails.getEmail());
            customer.setAddress(customerDetails.getAddress());
            return customerRepository.save(customer);
        }).orElse(null);
    }

    // PATCH
    public Customer patchCustomer(Integer id, Map<String, Object> updates) {
        return customerRepository.findById(id).map(customer -> {
            if (updates.containsKey("custName")) {
                customer.setCustName((String) updates.get("custName"));
            }
            if (updates.containsKey("phoneNo")) {
                customer.setPhoneNo((String) updates.get("phoneNo"));
            }
            if (updates.containsKey("email")) {
                customer.setEmail((String) updates.get("email"));
            }
            if (updates.containsKey("address")) {
                customer.setAddress((String) updates.get("address"));
            }
            return customerRepository.save(customer);
        }).orElse(null);
    }

    public boolean deleteCustomer(Integer id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
