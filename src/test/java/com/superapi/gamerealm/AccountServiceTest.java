
/**
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VillageService villageService;

    private AccountService accountService;

    @BeforeEach
    void setup() {
        accountService = new AccountService(accountRepository, villageService);
    }

    @AfterEach
    void cleanup() {
        accountRepository.deleteAll();
    }

    @Test
    void testCreateAccount() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUsername("newUser");
        accountRequest.setPassword("newPassword");
        accountRequest.setEmail("newuser@example.com");
        accountRequest.setTribe("newTribe");

        Account createdAccount = accountService.createAccount(accountRequest);

        assertEquals(accountRequest.getUsername(), createdAccount.getUsername());
        assertEquals(accountRequest.getPassword(), createdAccount.getPassword());
        assertEquals(accountRequest.getEmail(), createdAccount.getEmail());
        assertEquals(accountRequest.getTribe(), createdAccount.getTribe());

        // The ID will be automatically generated by the JPA entity manager
        assertTrue(createdAccount.getId() > 0);

    }

    @Test
    void testGetAccountByUsername() {
        String username = "existingUser";
        String password = "existingPassword";

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUsername(username);
        accountRequest.setPassword(password);
        Account createdAccount = accountService.createAccount(accountRequest);

        Account retrievedAccount = accountService.getAccountByUsername(username).orElse(null);

        assertEquals(createdAccount, retrievedAccount);
    }

    @Test
    void testGetAccountByUsername_NonExistentUser() {
        String username = "nonExistentUser";

        assertFalse(accountService.getAccountByUsername(username).isPresent());
    }
}


**/