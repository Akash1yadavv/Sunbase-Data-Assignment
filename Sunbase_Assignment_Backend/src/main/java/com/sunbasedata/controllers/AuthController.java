package com.sunbasedata.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunbasedata.dtos.LoginCustomerDto;
import com.sunbasedata.dtos.LoginResponseDto;
import com.sunbasedata.model.Customer;
import com.sunbasedata.services.CustomerService;
import com.sunbasedata.services.JwtService;

@RestController
@RequestMapping("/api/sunbasedata/auth") 
public class AuthController {
	@Autowired private CustomerService customerService;
    @Autowired private JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(@RequestBody LoginCustomerDto loginUserDto) {
    	
        Customer authenticatedUser = customerService.loginCustomer(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
	
	
}
