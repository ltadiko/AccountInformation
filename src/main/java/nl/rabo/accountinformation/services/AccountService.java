package nl.rabo.accountinformation.services;

import nl.rabo.accountinformation.models.AccountRequest;
import nl.rabo.accountinformation.models.entity.AccountEntity;
import nl.rabo.accountinformation.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * @author : Lakshmaiah Tatikonda
 * Account service implements managing accounts business logic by communicating with backend database
 */
@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountEntity> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public AccountEntity getAccount(Long userId, Long accountId) {
        return accountRepository.findByUserIdAndAccountId(userId, accountId);
    }

    public void addAccount(AccountRequest accountRequest) {
        accountRepository.save(AccountEntity.builder()
                .accountNumber(accountRequest.getAccountNumber())
                .accountCardType(accountRequest.getAccountCardType())
                .accountStatus(accountRequest.getAccountStatus())
                .userId(accountRequest.getUserId())
                .createdDate(Timestamp.from(Instant.now()))
                .lastModifiedDate(Timestamp.from(Instant.now()))
                .build());
    }

    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }
}
