package com.sunbasedata.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sunbasedata.model.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	Optional<Customer> findByUuid(String uuid);
	Optional<Customer> findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE " +
            "(COALESCE(:searchTerm, '') = '' OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.city) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
            "(COALESCE(:city, '') = '' OR c.city = :city) AND " +
            "(COALESCE(:state, '') = '' OR c.state = :state) AND " +
            "(COALESCE(:email, '') = '' OR c.street = :email)")
    Page<Customer> searchAndFilterCustomers(@Param("searchTerm") String searchTerm, @Param("city") String city,@Param("state") String state, @Param("email") String email, Pageable pageable);
}
