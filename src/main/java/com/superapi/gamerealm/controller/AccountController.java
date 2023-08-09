package com.superapi.gamerealm.controller;

import com.superapi.gamerealm.dto.AccountDTO;
import com.superapi.gamerealm.model.LoginRequest;
import com.superapi.gamerealm.service.AccountService;
import com.superapi.gamerealm.service.CombatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final CombatService combatService;

    public AccountController(AccountService accountService, CombatService combatService) {
        this.accountService = accountService;
        this.combatService = combatService;
    }

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) {
        AccountDTO createdAccount = accountService.createAccount(accountDTO);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = accountService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        System.out.println(isAuthenticated + "isAuth");
        if (isAuthenticated) {
            return ResponseEntity.ok("Authenticated successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<AccountDTO> getAccountByUsername(@PathVariable String username) {
        return accountService.getAccountByUsername(username)
                .map(accountDTO -> new ResponseEntity<>(accountDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/id/{accountId}")
    public ResponseEntity<AccountDTO> getAccountByAccountId(@PathVariable Long accountId) {
        return accountService.getAccountByAccountId(accountId)
                .map(accountDTO -> new ResponseEntity<>(accountDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}