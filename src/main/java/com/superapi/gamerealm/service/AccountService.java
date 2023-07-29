package com.superapi.gamerealm.service;

import com.superapi.gamerealm.dto.AccountDTO;
import com.superapi.gamerealm.model.Account;
import com.superapi.gamerealm.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AccountService(AccountRepository accountRepository, ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }

    public AccountDTO createAccount(AccountDTO accountDTO) {
        Account account = modelMapper.map(accountDTO, Account.class);
        Account createdAccount = accountRepository.save(account);
        return modelMapper.map(createdAccount, AccountDTO.class);
    }

    public Optional<AccountDTO> getAccountByUsername(String username) {
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);
        return optionalAccount.map(account -> modelMapper.map(account, AccountDTO.class));
    }


    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AccountDTO convertToDTO(Account account) {
        return modelMapper.map(account, AccountDTO.class);
    }

    public void purgePlayerAccounts() {
        accountRepository.deleteAll();
    }

    // Other account-related methods
}
