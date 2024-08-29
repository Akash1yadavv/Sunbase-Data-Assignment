package com.sunbasedata.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sunbasedata.dtos.CustomerReqDto;
import com.sunbasedata.dtos.CustomerResponseDto;
import com.sunbasedata.dtos.LoginCustomerDto;
import com.sunbasedata.exceptions.CustomerException;
import com.sunbasedata.model.Customer;


public interface CustomerService {
	
	public Customer registerCustomer(CustomerReqDto customerReqDto)throws CustomerException;

	public Customer loginCustomer(LoginCustomerDto loginDetails) throws CustomerException;
	
    public Customer updateCustomer(Integer id, CustomerReqDto customerReqDto)throws CustomerException;
	
	public Customer getCustomerById(Integer id)throws CustomerException;
	
	public Customer deleteCustomer(Integer id)throws CustomerException;
	
	public Page<Customer> getAllCustomers(Pageable pageable)throws CustomerException;
	
	public Page<Customer> searchCustomers(String searchTerm, String city, String state, String email, Pageable pageable) throws CustomerException;

}
