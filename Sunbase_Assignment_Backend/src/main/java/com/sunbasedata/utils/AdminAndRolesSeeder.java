package com.sunbasedata.utils;

import com.sunbasedata.dtos.CustomerReqDto;
import com.sunbasedata.enums.Roles;
import com.sunbasedata.model.Customer;
import com.sunbasedata.model.Role;
import com.sunbasedata.repositories.CustomerRepository;
import com.sunbasedata.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
public class AdminAndRolesSeeder implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired private RoleRepository roleRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private PasswordEncoder passwordEncoder;

	@Value("${security.project.admin-username}")
    private String adminUserName;
    @Value("${security.project.admin-password}")
    private String adminPassword;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createAdministrator();
        this.loadRoles();
    }

    private void createAdministrator() {
        CustomerReqDto customerDto = new CustomerReqDto();
        customerDto.setEmail(adminUserName);
        customerDto.setPassword(adminPassword);

        Optional<Role> optionalRole = roleRepository.findByName(Roles.ADMIN);
        
        Optional<Customer> optionalUser = customerRepository.findByEmail(customerDto.getEmail());

        if (optionalRole.isEmpty()) {
        	loadRoles();
        	optionalRole = roleRepository.findByName(Roles.ADMIN);
        }

        if (!optionalUser.isPresent()) {
            Customer customer = new Customer();
            customer.setRole(optionalRole.get());
            customer.setUuid("test" + UUID.randomUUID().toString().replace("-", ""));
            customer.setEmail(customerDto.getEmail());
            customer.setFirstName("Admin");
            customer.setPassword(passwordEncoder.encode(customerDto.getPassword())); // Encrypt the password
            customer.setPhone("12345678");
            customer.setAddress("sector 4");
            customer.setCity("Delhi");
            customer.setState("Delhi");
            customer.setStreet("street 2");

            customerRepository.save(customer);
        }

//        System.out.println("Admin user created successfully.");
    }

    private void loadRoles() {
        Roles[] roleNames = new Roles[] { Roles.USER, Roles.ADMIN };
        
        Arrays.stream(roleNames).forEach((roleName) -> {
            Optional<Role> optionalRole = roleRepository.findByName(roleName);
            
            if (!optionalRole.isPresent()) {
                Role roleToCreate = new Role();
                roleToCreate.setName(roleName);
                
                roleRepository.save(roleToCreate);
            }
        });
    }
}
