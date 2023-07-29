package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountDTO mapToResponseDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setEmail(account.getEmail());
        dto.setTribe(account.getTribe());
        dto.setUsername(account.getUsername());
        return dto;
    }

    // You can add more mapping methods if needed for other use cases

}
