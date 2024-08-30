package com.sunbasedata.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sunbasedata.dtos.CustomerReqDto;
import com.sunbasedata.dtos.CustomerResponseDto;
import com.sunbasedata.dtos.LoginCustomerDto;
import com.sunbasedata.enums.Roles;
import com.sunbasedata.exceptions.CustomerException;
import com.sunbasedata.model.Customer;
import com.sunbasedata.model.Role;
import com.sunbasedata.repositories.CustomerRepository;
import com.sunbasedata.repositories.RoleRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired private PasswordEncoder passwordEncoder;
    
    @Autowired private AuthenticationManager authenticationManager;
    
    @Autowired private CustomerRepository customerRepository;
    
    @Autowired private RoleRepository roleRepository;

    @Override
    public Customer registerCustomer(CustomerReqDto customerReqDto) throws CustomerException {
        if (customerReqDto == null) {
            throw new CustomerException("Customer details cannot be null.");
        }

        Optional<Role> optionalRole = roleRepository.findByName(Roles.USER);
        if (optionalRole.isEmpty()) {
            throw new CustomerException("Role 'USER' not found.");
        }

        Customer customer = new Customer();
        customer.setRole(optionalRole.get());
        customer.setUuid("test" + UUID.randomUUID().toString().replace("-", ""));
        customer.setEmail(customerReqDto.getEmail());
        customer.setFirstName(customerReqDto.getFirstName());
        customer.setLastName(customerReqDto.getLastName());
        customer.setPassword(passwordEncoder.encode(customerReqDto.getPassword())); // Encrypt the password
        customer.setPhone(customerReqDto.getPhone());
        customer.setAddress(customerReqDto.getAddress());
        customer.setCity(customerReqDto.getCity());
        customer.setState(customerReqDto.getState());
        customer.setStreet(customerReqDto.getStreet());

        return customerRepository.save(customer);
    }

    @Override
    public Customer loginCustomer(LoginCustomerDto loginDetails) throws CustomerException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDetails.getEmail(), loginDetails.getPassword())
        );

        return customerRepository.findByEmail(loginDetails.getEmail())
                .orElseThrow(() -> new CustomerException("Customer not found for email: " + loginDetails.getEmail()));
    }

    @Override
    public Customer getCustomerById(Integer id) throws CustomerException {
    	
    	
        Customer customer= customerRepository.findById(id)
                .orElseThrow(() -> new CustomerException("Customer not found for ID: " + id));
        return customer;
        
    }

    @Override
    public Customer deleteCustomer(Integer id) throws CustomerException {
    	
    	Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerException("Customer not found for ID: " + id));

    	if(customer.getRole().getName() == Roles.ADMIN){
    		throw new CustomerException("You can't delete to Admin ");
    	}
        customerRepository.delete(customer);
        
        return customer;
    }

    @Override
    public Customer updateCustomer(Integer id, CustomerReqDto customerReqDto) throws CustomerException {
        if (customerReqDto == null) {
            throw new CustomerException("Customer data cannot be null.");
        }

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerException("Customer not found for ID: " + id));

        Optional<Role> optionalRole = roleRepository.findByName(Roles.USER);
        if (optionalRole.isEmpty()) {
            throw new CustomerException("Role 'USER' not found.");
        }

        customer.setRole(optionalRole.get());
        customer.setFirstName(customerReqDto.getFirstName());
        customer.setLastName(customerReqDto.getLastName());
        customer.setStreet(customerReqDto.getStreet());
        customer.setAddress(customerReqDto.getAddress());
        customer.setCity(customerReqDto.getCity());
        customer.setState(customerReqDto.getState());
        customer.setEmail(customerReqDto.getEmail());
        customer.setPhone(customerReqDto.getPhone());

        return customerRepository.save(customer);
    }

    @Override
    public Page<Customer> getAllCustomers(Pageable pageable) throws CustomerException {
    	CustomerResponseDto crd = new CustomerResponseDto();
        return customerRepository.findAll(pageable);
    }

    @Override
    public Page<Customer> searchCustomers(String searchTerm, String city, String state, String email, Pageable pageable) throws CustomerException {
    	Page<Customer> customers= customerRepository.searchAndFilterCustomers(searchTerm, city, state, email, pageable);
        if (customers.isEmpty()) {
            throw new CustomerException("No customers found matching the given criteria.");
        }
        return customers;
    }

}
