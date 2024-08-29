package com.sunbasedata.controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunbasedata.services.RemoteApiService;

@RestController
@RequestMapping("/api/sunbasedata") 
public class RemoteApiController {

	@Autowired RemoteApiService remoteApiService;
	private static final Logger logger = LoggerFactory.getLogger(RemoteApiController.class);
	
    @PutMapping("/sync-customers")
    public ResponseEntity<String> syncCustomersFromRemoteApi() {
        try {
            String str = remoteApiService.syncCustomersFromRemoteApi();
            return ResponseEntity.ok(str);
        } catch (Exception e) {
            logger.error("Error synchronizing customers from remote API", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error synchronizing customers from remote API");
        }
    }

}
