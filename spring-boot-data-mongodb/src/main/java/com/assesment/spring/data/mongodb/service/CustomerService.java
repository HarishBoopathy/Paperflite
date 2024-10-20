package com.assesment.spring.data.mongodb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.assesment.spring.data.mongodb.model.CustomerEntity;
import com.assesment.spring.data.mongodb.repository.CustomerRepository;

@Service
public class CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

	@Autowired
	private CustomerRepository customerRepository;  

	/* Logic of retrieving paginated customer data */
	public Page<CustomerEntity> getCustomersByAccountId(String accountId, int page, int limit) { 

		logger.info("Fetching customers for accountId: {}, page: {}, limit: {}", accountId, page, limit);

		try {
			Pageable pageable = PageRequest.of(page, limit);
			return customerRepository.findByAccountId_Id(accountId, pageable);

		}catch (Exception e) {
			logger.error("Error fetching customers for accountId:", accountId, e);
			throw new RuntimeException("Unable to fetch customers");
		}
	}

	/* Migration to delete customers with non-existent accounts */
	public void removeCustomersWithoutAccount() { 
		int pageSize = 25;  // Page size to process records

		try {
			for (int page = 0; ; page++) {
				Pageable pageable = PageRequest.of(page, pageSize);
				Page<CustomerEntity> customerPage = customerRepository.findAll(pageable); 

				if (customerPage.isEmpty()) {
					break; // customerPage null check
				}

				// Process each customer using a for-each loop
				for (CustomerEntity customer : customerPage) {
					try {
						if (customer.getAccountId() == null) {
							logger.info("Customer id {} has no associated account - Deleting customer", customer.getId());
							customerRepository.delete(customer);
						}
					} catch (Exception e) {
						logger.error("Error processing customer with ID: {}", customer.getId(), e);
					}
				}

				if (!customerPage.hasNext()) {
					break; // No more pages to process
				}
			}
		} catch (Exception e) {
			logger.error("Error during pagination or customer processing", e);
		}
	}

}
