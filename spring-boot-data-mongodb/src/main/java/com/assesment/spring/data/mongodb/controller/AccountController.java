package com.assesment.spring.data.mongodb.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.assesment.spring.data.mongodb.model.AccountEntity;
import com.assesment.spring.data.mongodb.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class AccountController {

  @Autowired
  private AccountRepository accountRepository;

  @GetMapping("/accounts")
  public ResponseEntity<List<AccountEntity>> getAllAccounts() {
    try {
      List<AccountEntity> accountEntities = new ArrayList<>();

      accountEntities = accountRepository.findAll();

      if (accountEntities.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(accountEntities, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/accounts/{id}")
  public ResponseEntity<AccountEntity> getAccountById(@PathVariable("id") String id) {
    Optional<AccountEntity> account = accountRepository.findById(id);

    if (account.isPresent()) {
      return new ResponseEntity<>(account.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

}
