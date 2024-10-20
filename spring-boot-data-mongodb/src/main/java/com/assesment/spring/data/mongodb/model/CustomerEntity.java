package com.assesment.spring.data.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customer")
public class CustomerEntity {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    @DBRef
    private AccountEntity accountId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public AccountEntity getAccountId() {
        return accountId;
    }

    public void setAccountId(AccountEntity account) {
        this.accountId = account;
    }
}
