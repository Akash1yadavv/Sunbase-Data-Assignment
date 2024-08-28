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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunbasedata.dtos.RemoteCustomerReqDto;
import com.sunbasedata.exceptions.RemoteApiException;
import com.sunbasedata.model.Customer;
import com.sunbasedata.repositories.CustomerRepository;

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
    
	private static final Logger logger = LoggerFactory.getLogger(RemoteApiService.class);
     

    @Autowired private RestTemplate restTemplate;
	@Autowired private CustomerRepository customerRepository;
	

	@Override
    public String syncCustomersFromRemoteApi() throws RemoteApiException {
		
        List<RemoteCustomerReqDto> remoteCustomers = fetchCustomersFromRemoteApi();// Fetch customers from the remote API
        
        for (RemoteCustomerReqDto remoteCustomer : remoteCustomers) {
            
            Optional<Customer> existingCustomer = customerRepository.findByUuid(remoteCustomer.getUuid());// Check if the customer already exists in the local database
//            System.out.println(remoteCustomer.getUuid());
            if (existingCustomer.isPresent()) {
//            	System.out.println("=============================================================");
                Customer customerToUpdate = existingCustomer.get();// Update existing customer
                customerToUpdate.setFirstName(remoteCustomer.getFirst_name());
                customerToUpdate.setLastName(remoteCustomer.getFirst_name());
//                customerToUpdate.setPassword(remoteCustomer.getPassword());// 
                customerToUpdate.setStreet(remoteCustomer.getStreet());
                customerToUpdate.setAddress(remoteCustomer.getAddress());
                customerToUpdate.setCity(remoteCustomer.getCity());
                customerToUpdate.setState(remoteCustomer.getState());
                customerToUpdate.setEmail(remoteCustomer.getEmail());
                customerToUpdate.setPhone(remoteCustomer.getPhone());
                
                customerRepository.save(customerToUpdate);
            } else {
//            	System.out.println("-------------------------------------------------------------");
                Customer newCustomer = new Customer();
                newCustomer.setUuid(remoteCustomer.getUuid());
                newCustomer.setFirstName(remoteCustomer.getFirst_name());
                newCustomer.setLastName(remoteCustomer.getFirst_name());
//                newCustomer.setPassword(remoteCustomer.getPassword());
                newCustomer.setStreet(remoteCustomer.getStreet());
                newCustomer.setAddress(remoteCustomer.getAddress());
                newCustomer.setCity(remoteCustomer.getCity());
                newCustomer.setState(remoteCustomer.getState());
                newCustomer.setEmail(remoteCustomer.getEmail());
                newCustomer.setPhone(remoteCustomer.getPhone());
                
                customerRepository.save(newCustomer);
                
            }
        }
        return "Customers synchronized successfully";
    }

	

	private List<RemoteCustomerReqDto> fetchCustomersFromRemoteApi() throws RemoteApiException {
        
        String accessToken = genrateRemoteToken();
        if(accessToken==null)
        	throw new RemoteApiException("JTW token is not generated...." );
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.add("Cookie", "JSESSIONID=A12F34192CE1774490930A05F101246D.jvm2");
        
        HttpEntity<String> entity = new HttpEntity<>(headers);

        List<RemoteCustomerReqDto> listOfCustomers = new ArrayList<>();
        try {
        	ResponseEntity<RemoteCustomerReqDto[]> response = restTemplate.exchange(FETCH_CUSTOMERS_URL, HttpMethod.GET, entity, RemoteCustomerReqDto[].class);
            listOfCustomers=Arrays.asList(response.getBody());
            
        } catch (RemoteApiException e) {
            logger.error("Error occured while fetching remote API........", e);
            e.printStackTrace();
        }
        return listOfCustomers;
    }

    private String genrateRemoteToken() {
    	
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.add("Cookie", "JSESSIONID=B6EEAFB8B83D2B019F44BB3924FEE4C1.jvm2");
	
	    Map<String, String> jsonData = new HashMap<>();
	    jsonData.put("login_id",remoteUserName );
	    jsonData.put("password", remotePassword);
	
	    // Create the HttpEntity object with headers and JSON body
	    HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(jsonData, headers);
	    String accessToken = null;
	    try {
	        // Make the POST request
	        ResponseEntity<String> response = restTemplate.exchange( AUTH_URL, HttpMethod.POST, requestEntity, String.class);
	

	        if (response.getStatusCode() == HttpStatus.OK) {
		        ObjectMapper objectMapper = new ObjectMapper();	// Check response status and output the result
	            JsonNode rootNode = objectMapper.readTree(response.getBody());
	            accessToken = rootNode.path("access_token").asText();
	        }
	    } catch (Exception e) {
	    	logger.error("Error generating jwt token from remote API", e);
	        e.printStackTrace();
	    }
	    return accessToken;
    }


}