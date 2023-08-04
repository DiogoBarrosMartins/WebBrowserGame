package com.superapi.gamerealm.service;

import com.superapi.gamerealm.dto.AccountDTO;
import com.superapi.gamerealm.dto.AccountMapper;
import com.superapi.gamerealm.dto.VillageDTO;
import com.superapi.gamerealm.model.Account;
import com.superapi.gamerealm.model.Message;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.repository.AccountRepository;
import com.superapi.gamerealm.repository.VillageRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final VillageService villageService;
    private final ModelMapper modelMapper;
    private final AccountMapper accountMapper;

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final VillageRepository villageRepository;


    @Autowired
    public AccountService(AccountRepository accountRepository,
                          VillageService villageService, VillageRepository v, AccountMapper accountMapper, ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.villageService = villageService;
        this.villageRepository = v;
        this.modelMapper = modelMapper;
        this.accountMapper = accountMapper;
    }

    public AccountDTO createAccount(AccountDTO accountDTO) {
        Account account = accountMapper.dtoToEntity(accountDTO);
        account = accountRepository.save(account);
        logger.info("Saved account: {}", account);

        Village village = villageService.createVillageForAccount(account);
        logger.info("Saved village: {}", village);

        return accountMapper.entityToDto(account);
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

    public List<VillageDTO> findAllVillagesByAccountId(Long accountId) {
        return villageService.findAllVillagesByAccountId(accountId);
    }

    public void purgePlayerAccounts() {
        accountRepository.deleteAll();
    }

    public void deleteAccount(Long accountId) {
        for (VillageDTO village : findAllVillagesByAccountId(accountId)) {
            villageService.deleteVillage(village.getId());
        }
        accountRepository.deleteById(accountId);
    }

    public Optional<AccountDTO> getAccountByAccountId(Long accountId) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        return optionalAccount.map(account -> modelMapper.map(account, AccountDTO.class));
    }

    public void sendMessage(String senderUsername, String recipientUsername, String message) {
        Optional<Account> optionalSender = accountRepository.findByUsername(senderUsername);
        Optional<Account> optionalRecipient = accountRepository.findByUsername(recipientUsername);

        if (optionalSender.isPresent() && optionalRecipient.isPresent()) {
            Account sender = optionalSender.get();
            Account recipient = optionalRecipient.get();

            Message messageEntity = new Message(sender, recipient, message);
            sender.getSentMessages().add(messageEntity);
            recipient.getReceivedMessages().add(messageEntity);

            accountRepository.save(sender);
            accountRepository.save(recipient);
        }
    }

    private AccountDTO convertToDTO(Account account) {
        return modelMapper.map(account, AccountDTO.class);
    }


}

