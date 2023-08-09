package com.superapi;

import com.superapi.gamerealm.Main;
import com.superapi.gamerealm.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    public void testAuthenticate_validCredentials_return200() throws Exception {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        Mockito.when(accountService.authenticate(username, password)).thenReturn(true);

        mockMvc.perform(
                        post("/accounts/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Authenticated successfully"));

    }
    @Test
    public void testAuthenticate_invalidCredentials_return400() throws Exception {
        // Arrange
        String username = "testUser";
        String password = "wrongPassword";

        Mockito.when(accountService.authenticate(username, password)).thenReturn(false);

        mockMvc.perform(
                        post("/accounts/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid username or password"));
    }

}

