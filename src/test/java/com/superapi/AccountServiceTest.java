package com.superapi;

import com.superapi.gamerealm.repository.AccountRepository;
import com.superapi.gamerealm.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    public void testAuthenticate_validCredentials_returnTrue() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";

        // Act
        boolean result = accountService.authenticate(username, password);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAuthenticate_invalidCredentials_returnFalse() {
        // Arrange
        String username = "testUser";
        String password = "wrongPassword";

        // Act
        boolean result = accountService.authenticate(username, password);

        // Assert
        assertFalse(result);
    }
}
