package com.sunbasedata.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunbasedata.dtos.RemoteCustomerReqDto;
import com.sunbasedata.enums.Roles;
import com.sunbasedata.exceptions.CustomerException;
import com.sunbasedata.exceptions.RemoteApiException;
import com.sunbasedata.model.Customer;
import com.sunbasedata.model.Role;
import com.sunbasedata.repositories.CustomerRepository;
import com.sunbasedata.repositories.RoleRepository;

@Service
public class RemoteApiServiceImpl implements RemoteApiService {

    @Value("${security.remote-username}")
    private String remoteUserName;

    @Value("${security.remote-password}")
    private String remotePassword;

    @Value("${remote.api.auth.url}")
    private String AUTH_URL;

    @Value("${remote.api.get.customers.url}")
    private String FETCH_CUSTOMERS_URL;

    private static final Logger logger = LoggerFactory.getLogger(RemoteApiServiceImpl.class);

    @Autowired private RestTemplate restTemplate;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private RoleRepository roleRepository;

    @Override
    public String syncCustomersFromRemoteApi() throws RemoteApiException {
        try {
            List<RemoteCustomerReqDto> remoteCustomers = fetchCustomersFromRemoteApi();

            for (RemoteCustomerReqDto remoteCustomer : remoteCustomers) {
                processCustomer(remoteCustomer);
            }

            return "Customers synchronized successfully";
        } catch (Exception e) {
            logger.error("Error synchronizing customers from remote API", e);
            throw new RemoteApiException("Failed to synchronize customers from remote API", e);
        }
    }

    private void processCustomer(RemoteCustomerReqDto remoteCustomer) throws RemoteApiException {
        try {
            Optional<Customer> existingCustomer = customerRepository.findByUuid(remoteCustomer.getUuid());
            Optional<Role> optionalRole = roleRepository.findByName(Roles.USER);

            if (optionalRole.isEmpty()) {
                throw new RemoteApiException("Role not found in the database");
            }

            Customer customer = existingCustomer.orElseGet(Customer::new);
            customer.setUuid(remoteCustomer.getUuid());
            customer.setRole(optionalRole.get());
            customer.setFirstName(remoteCustomer.getFirst_name());
            customer.setLastName(remoteCustomer.getFirst_name());
            customer.setStreet(remoteCustomer.getStreet());
            customer.setAddress(remoteCustomer.getAddress());
            customer.setCity(remoteCustomer.getCity());
            customer.setState(remoteCustomer.getState());
            customer.setEmail(remoteCustomer.getEmail());
            customer.setPhone(remoteCustomer.getPhone());

            customerRepository.save(customer);

        } catch (Exception e) {
            logger.error("Error processing customer with UUID: {}", remoteCustomer.getUuid(), e);
            throw new RemoteApiException("Error processing customer data", e);
        }
    }

    private List<RemoteCustomerReqDto> fetchCustomersFromRemoteApi() throws RemoteApiException {
        String accessToken = genrateRemoteToken();
        if (accessToken == null) {
            throw new RemoteApiException("JWT token is not generated.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.add("Cookie", "JSESSIONID=A12F34192CE1774490930A05F101246D.jvm2");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<RemoteCustomerReqDto[]> response = restTemplate.exchange(FETCH_CUSTOMERS_URL, HttpMethod.GET, entity, RemoteCustomerReqDto[].class);
            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Client/Server error occurred while fetching customers from remote API: {}", e.getMessage());
            throw new RemoteApiException("Error fetching customers from remote API", e);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching customers from remote API", e);
            throw new RemoteApiException("Unexpected error fetching customers from remote API", e);
        }
    }

    private String genrateRemoteToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", "JSESSIONID=B6EEAFB8B83D2B019F44BB3924FEE4C1.jvm2");

        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("login_id", remoteUserName);
        jsonData.put("password", remotePassword);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(jsonData, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(AUTH_URL, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                return rootNode.path("access_token").asText();
            } else {
                logger.warn("Received non-OK response while generating JWT token: {}", response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error generating JWT token from remote API", e);
            return null;
        }
    }
}
