package com.sunbasedata.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
import com.sunbasedata.model.Customer;
import com.sunbasedata.services.CustomerService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/sunbasedata") 
public class CustomerController {

    @Autowired private CustomerService customerService;

    @PostMapping("/register-customer")
    public ResponseEntity<Customer> register(@Valid @RequestBody CustomerReqDto customerReqDto) {
        Customer registeredCustomer = customerService.registerCustomer(customerReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredCustomer);
    }


    @PutMapping("/update-customer/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Integer id, @Valid @RequestBody CustomerReqDto customerDetails) {
        Customer updatedCustomer = customerService.updateCustomer(id, customerDetails);
        return ResponseEntity.ok(updatedCustomer);
    }
 
    @GetMapping("/customers-list")
    public ResponseEntity<Page<Customer>> getAllCustomers(Pageable pageable) {
        Page<Customer> customers = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/get-customer/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("/delete-customer/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Integer id) {
       Customer customer =  customerService.deleteCustomer(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/search-customers")
	public ResponseEntity<PagedModel<EntityModel<Customer>>> searchCustomers(
	    @RequestParam(value = "searchTerm", required = false) String searchTerm,
	    @RequestParam(value = "city", required = false) String city,
	    @RequestParam(value = "state", required = false) String state,
	    @RequestParam(value = "email", required = false) String email,
	    @RequestParam(value = "page", defaultValue = "0") int page,
	    @RequestParam(value = "size", defaultValue = "10") int size,
	    @RequestParam(value = "sort", defaultValue = "firstName") String sort,
	    @RequestParam(value = "dir", defaultValue = "asc") String dir,
	    PagedResourcesAssembler<Customer> assembler) {
    	
    	
	    Sort.Direction direction = dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
	    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
	    
	    Page<Customer> customers = customerService.searchCustomers(searchTerm, city, state, email, pageable);
	    
	    // Corrected type of pagedModel
	    PagedModel<EntityModel<Customer>> pagedModel = assembler.toModel(customers);
	    
	    return ResponseEntity.ok(pagedModel);
	}
    
}
