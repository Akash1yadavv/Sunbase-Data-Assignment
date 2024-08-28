package com.sunbasedata.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sunbasedata.dtos.CustomerReqDto;
import com.sunbasedata.dtos.LoginCustomerDto;
import com.sunbasedata.exceptions.CustomerException;
import com.sunbasedata.model.Customer;
import com.sunbasedata.repositories.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {


    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
	@Autowired private CustomerRepository customerRepository;
	
	@Override
	public Customer registerCustomer(CustomerReqDto customerReqDto) throws CustomerException{
		
		 // Validate the customer request data
	    if (customerReqDto == null) {
	        throw new CustomerException("Please fill all details.");
	    }

	    // Create a new Customer entity from the DTO
	    Customer customer = new Customer();
	    
	    customer.setUuid("test"+UUID.randomUUID().toString().replace("-", ""));
	    customer.setEmail(customerReqDto.getEmail());
	    customer.setFirstName(customerReqDto.getFirstName());
	    customer.setLastName(customerReqDto.getLastName());
	    customer.setPassword(passwordEncoder.encode(customerReqDto.getPassword())); // Encrypt the password
	    customer.setPhone(customerReqDto.getPhone());
	    customer.setAddress(customerReqDto.getAddress());
	    customer.setCity(customerReqDto.getCity());
	    customer.setState(customerReqDto.getState());
	    customer.setStreet(customerReqDto.getStreet());

	    // Save the new customer to the repository
	    try {
	        return customerRepository.save(customer);
	    } catch (Exception e) {
	        throw new CustomerException("An error occurred while registering the customer", e);
	    }
	}
	
	@Override
	public Customer loginCustomer(LoginCustomerDto loginDetails) throws CustomerException {
		
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDetails.getUuid(),loginDetails.getPassword())
				);

        return customerRepository.findByUuid(loginDetails.getUuid())
        		.orElseThrow(() -> new CustomerException("Customer not found for UUID: " + loginDetails.getUuid()));
	}
	@Override
	public Customer getCustomerById(Integer id) throws CustomerException {
	    return customerRepository.findById(id)
	            .orElseThrow(() -> new CustomerException("Customer not found for this id: " + id));
	}

	@Override
	public Customer deleteCustomer(Integer id) throws CustomerException {
	    Customer customer = customerRepository.findById(id)
	            .orElseThrow(() -> new CustomerException("Customer not found for this id: " + id));
	    
	    try {
	       customerRepository.delete(customer);
	       return customer;
	    } catch (Exception e) {
	        throw new CustomerException("An error occurred while deleting customer with id: " + id, e);
	    }
	}

	@Override
	public Customer updateCustomer(Integer id, CustomerReqDto customerReqDto) throws CustomerException {
	    // Check if customerReqDto is null
	    if (customerReqDto == null) {
	        throw new CustomerException("Customer data cannot be null.");
	    }

	    Customer customer = customerRepository.findById(id)
	            .orElseThrow(() -> new CustomerException("Customer not found for this id: " + id));

	    // Update customer details with data from customerReqDto
	    customer.setFirstName(customerReqDto.getFirstName());
	    customer.setLastName(customerReqDto.getLastName());
	    customer.setStreet(customerReqDto.getStreet());
	    customer.setAddress(customerReqDto.getAddress());
	    customer.setCity(customerReqDto.getCity());
	    customer.setState(customerReqDto.getState());
	    customer.setEmail(customerReqDto.getEmail());
	    customer.setPhone(customerReqDto.getPhone());

	    try {
	        return customerRepository.save(customer);
	    } catch (Exception e) {
	        throw new CustomerException("An error occurred while updating the customer with id: " + id, e);
	    }
	}

	@Override
    public Page<Customer> getAllCustomers(Pageable pageable) throws CustomerException {
        try {
            return customerRepository.findAll(pageable);
        } catch (Exception e) {
            throw new CustomerException("Error fetching customers.", e);
        }
    }
	
	@Override
    public Page<Customer> searchCustomers(String keyword, Pageable pageable) throws CustomerException {
        try {
            return customerRepository.findByFirstNameContainingOrLastNameContainingOrEmailContainingOrCityContainingOrPhoneContaining(
                    keyword, keyword, keyword, keyword, keyword, pageable);
        } catch (Exception e) {
            throw new CustomerException("An Error occurred when searching customers.", e);
        }
    }

	
}
