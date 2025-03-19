package com.assesment.spring.data.mongodb.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assesment.spring.data.mongodb.model.AccountEntity;
import com.assesment.spring.data.mongodb.model.CustomerEntity;
import com.assesment.spring.data.mongodb.repository.AccountRepository;
import com.assesment.spring.data.mongodb.repository.CustomerRepository;
import com.assesment.spring.data.mongodb.service.CustomerService;

@RestController
@RequestMapping("/api")  
public class CustomerController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AccountRepository accountRepository;

	/* 
	 * API end point that accepts the accountId as a path variable,
	 * page and limit as request parameters
	 * to get customers by accountId with pagination
	 * 
	 * @param accountId of customers 
	 * @return ResponseEntity of customers with that accountId
	 */
	
	@GetMapping("account/{accountId}/customers") 
	public ResponseEntity<Page<CustomerEntity>> getCustomersByAccountId( @PathVariable String accountId, 
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit) {

		logger.info("Received request to fetch customers for accountId:", accountId);
		Page<CustomerEntity> customers = customerService.getCustomersByAccountId(accountId, page, limit);
		logger.info("Fetched customers for the respective accountId:", customers.getTotalElements(), accountId);
		return ResponseEntity.ok(customers);

	}

	/* 
	 * API end point to create/add new customer 
	 * 
	 * @param CustomerEntity
	 * @return ResponseEntity Created Customer details
	 */
	
	@PostMapping("/customer")
	public ResponseEntity<?> createCustomer(@RequestBody CustomerEntity customer) {
		try {
			// Verify account existence
			AccountEntity account = accountRepository.findById(customer.getAccountId().getId())
					.orElseThrow(() -> new RuntimeException("Account not found with ID: " + customer.getAccountId().getId()));

			customer.setAccountId(account);									  // Set the associated account
			CustomerEntity savedCustomer = customerRepository.save(customer); // Save customer to the database

			logger.info("Customer created with ID: {}", savedCustomer.getId());
			return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Error creating customer: ", e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* 
	 * API end point to update existing customers 
	 * 
	 * @param customer Id
	 * @return ResponseEntity updated customer details
	 */
	
@PutMapping("customer/{id}")
public ResponseEntity<String> updateCustomer(@PathVariable String id, @RequestBody CustomerEntity updatedCustomer) {
    logger.info("Received request to update customer with ID: {}", id);

    try {
        Optional<CustomerEntity> optionalCustomer = customerRepository.findById(id);

        return optionalCustomer.map(existingCustomer -> {
            updateCustomerDetails(existingCustomer, updatedCustomer);
            customerRepository.save(existingCustomer);
            logger.info("Customer with ID: {} successfully updated", id);
            return ResponseEntity.ok("Customer updated successfully.");
        }).orElseGet(() -> {
            logger.warn("Customer with ID: {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Customer not found.");
        });
        
    } catch (Exception e) {
        logger.error("Error updating customer with ID: {}", id, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while updating the customer.");
    }
}

private void updateCustomerDetails(CustomerEntity existingCustomer, CustomerEntity updatedCustomer) {
    existingCustomer.setFirstName(updatedCustomer.getFirstName());
    existingCustomer.setLastName(updatedCustomer.getLastName());
    existingCustomer.setAccountId(updatedCustomer.getAccountId());
}

}
