package com.superapi.gamerealm;

import com.superapi.gamerealm.dto.AccountRequest;
import com.superapi.gamerealm.model.Account;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.repository.AccountRepository;
import com.superapi.gamerealm.service.AccountService;
import com.superapi.gamerealm.service.VillageService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private VillageService villageService;

    @InjectMocks
    private AccountService accountService;


    @Test
    public void testCreateAccount() {
        // Test scenario
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUsername("testUser");
        accountRequest.setPassword("testPassword");
        accountRequest.setEmail("test@example.com");
        accountRequest.setTribe("Test Tribe");

        // Create a mock Account object to be saved by the repository
        Account newAccount = new Account();
        newAccount.setUsername(accountRequest.getUsername());
        newAccount.setPassword(accountRequest.getPassword());
        newAccount.setEmail(accountRequest.getEmail());
        newAccount.setTribe(accountRequest.getTribe());

        // Mock behavior of accountRepository.save()
        when(accountRepository.save(eq(newAccount))).thenReturn(newAccount);
        // Create a mock Village object to be returned by the villageService
        Village newVillage = new Village();
        newVillage.setId(1L); // Assuming the village gets an ID when saved
        // Mock behavior of villageService.createVillage()
        when(villageService.createVillage(any(Account.class))).thenReturn(newVillage);

        // Call the method to be tested
        Account createdAccount = accountService.createAccount(accountRequest);

        // Verify that accountRepository.save() was called with the correct account
        verify(accountRepository).save(newAccount);

        // Verify that villageService.createVillage() was called with the correct account
        verify(villageService).createVillage(newAccount);

        // Verify that the createdAccount is the same as the one returned by the repository
        assertEquals(newAccount, createdAccount);

        // Verify that the createdAccount has the associated village
        assertEquals(newVillage, createdAccount.getVillage());
    }

    @Test
    public void testGetAccountByUsername() {
        // Test scenario
        String testUsername = "testUser";

        // Create a mock Account object to be returned by the repository
        Account mockAccount = new Account();
        mockAccount.setUsername(testUsername);
        // Mock behavior of accountRepository.findByUsername()
        when(accountRepository.findByUsername(testUsername)).thenReturn(Optional.of(mockAccount));

        // Call the method to be tested
        Optional<Account> result = accountService.getAccountByUsername(testUsername);

        // Verify that accountRepository.findByUsername() was called with the correct username
        verify(accountRepository).findByUsername(testUsername);

        // Verify that the result is as expected
        assertEquals(Optional.of(mockAccount), result);
    }
}
