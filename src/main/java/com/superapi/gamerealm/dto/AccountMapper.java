package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account dtoToEntity(AccountDTO dto) {
        if (dto == null) {
            return null;
        }

        Account entity = new Account();
       entity.setPassword(dto.getPassword());
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setTribe(dto.getTribe());

        return entity;
    }

    public AccountDTO entityToDto(Account entity) {
        if (entity == null) {
            return null;
        }

        AccountDTO dto = new AccountDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setTribe(entity.getTribe());

        return dto;
    }
}

    // You can add more mapping methods if needed for other use cases


