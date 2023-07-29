package com.superapi.gamerealm.service;

import com.superapi.gamerealm.dto.AccountRequest;
import com.superapi.gamerealm.model.Account;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final VillageService villageService;

    public AccountService(AccountRepository accountRepository, VillageService villageService) {
        this.accountRepository = accountRepository;
        this.villageService = villageService;
    }

    public Account createAccount(AccountRequest accountRequest) {
        // Create a new account and save it to the database using the AccountRepository
        Account newAccount = new Account();
        // Set account details from the request (username, password, email, tribe, etc.)
        newAccount.setUsername(accountRequest.getUsername());
        newAccount.setPassword(accountRequest.getPassword());
        newAccount.setEmail(accountRequest.getEmail());
        newAccount.setTribe(accountRequest.getTribe());

        // Save the new account to the database
        Account savedAccount = accountRepository.save(newAccount);

        // Create a new village and associate it with the account using the VillageService
        Village newVillage = villageService.createVillage(savedAccount);
        savedAccount.setVillage(newVillage);

        // Return the newly created account
        return savedAccount;
    }


    public Optional<Account> getAccountByUsername(String username) {
   return accountRepository.findByUsername(username);
    }

    // Other methods related to account business logic
}
