package com.assesment.spring.data.mongodb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.assesment.spring.data.mongodb.model.CustomerEntity;

public interface CustomerRepository extends MongoRepository<CustomerEntity, String> {

	// To find customers by accountId.
	Page<CustomerEntity> findByAccountId_Id(String accountId, Pageable pageable); 

	// To find all customers
	Page<CustomerEntity> findAll(Pageable pageable);
}
