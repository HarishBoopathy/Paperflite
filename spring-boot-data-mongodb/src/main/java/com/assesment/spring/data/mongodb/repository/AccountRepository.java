package com.assesment.spring.data.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.assesment.spring.data.mongodb.model.AccountEntity;

public interface AccountRepository extends MongoRepository<AccountEntity, String> {
}
