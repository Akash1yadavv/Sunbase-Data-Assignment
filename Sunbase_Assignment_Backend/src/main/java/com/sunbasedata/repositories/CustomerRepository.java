package com.sunbasedata.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sunbasedata.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	Optional<Customer> findByUuid(String uuid);
	Optional<Customer> findByEmail(String email);
	
	Page<Customer> findByFirstNameContainingOrLastNameContainingOrEmailContainingOrCityContainingOrPhoneContaining(
            String firstName, String lastName, String email, String city, String phone, Pageable pageable);

}
