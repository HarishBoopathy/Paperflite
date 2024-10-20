package com.assesment.spring.data.mongodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assesment.spring.data.mongodb.service.CustomerService;

@RestController
@RequestMapping("/migration")
public class MigrationController {

	@Autowired
	private CustomerService customerService;

	/* 
	 * API to trigger the migration process 
	 * 
	 * @return ResponseEntity after deleting non-existent accounts 
	 */
	
	@PostMapping("/removeInvalidCustomers") 
	public ResponseEntity<String> migrateCustomers()  {
		customerService.removeCustomersWithoutAccount();
		return ResponseEntity.ok("Migration completed successfully.");
	}

} 
