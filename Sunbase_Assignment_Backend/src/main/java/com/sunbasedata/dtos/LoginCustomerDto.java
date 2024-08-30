package com.sunbasedata.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginCustomerDto {
	
    private String email;
    
    private String password;


}