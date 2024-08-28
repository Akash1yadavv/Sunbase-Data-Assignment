package com.sunbasedata.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sunbasedata.dtos.CustomerReqDto;
import com.sunbasedata.dtos.LoginCustomerDto;
import com.sunbasedata.dtos.LoginResponseDto;
import com.sunbasedata.model.Customer;
import com.sunbasedata.services.CustomerService;
import com.sunbasedata.services.JwtService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/customers") 
public class CustomerController {

    @Autowired private CustomerService customerService;
    @Autowired private JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @PostMapping("/register")
    public ResponseEntity<Customer> register(@Valid @RequestBody CustomerReqDto customerReqDto) {
        Customer registeredCustomer = customerService.registerCustomer(customerReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredCustomer);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(@RequestBody LoginCustomerDto loginUserDto) {
    	
        Customer authenticatedUser = customerService.loginCustomer(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Integer id, @Valid @RequestBody CustomerReqDto customerDetails) {
        Customer updatedCustomer = customerService.updateCustomer(id, customerDetails);
        return ResponseEntity.ok(updatedCustomer);
    }

    @GetMapping
    public ResponseEntity<Page<Customer>> getAllCustomers(Pageable pageable) {
        Page<Customer> customers = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Integer id) {
       Customer customer =  customerService.deleteCustomer(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Customer>> searchCustomers(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        Page<Customer> customers = customerService.searchCustomers(keyword, pageable);
        return ResponseEntity.ok(customers);
    }
    
    


}
