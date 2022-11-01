package nl.rabo.accountinformation.services;

import nl.rabo.accountinformation.models.BalanceRequest;
import nl.rabo.accountinformation.models.entity.BalanceEntity;
import nl.rabo.accountinformation.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * @author : Lakshmaiah Tatikonda
 * Balance service implements balances business logic by takinf request from controller and communicating with backend database
 */
@Service
public class BalanceService {
    private final BalanceRepository balanceRepository;

    @Autowired
    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public BalanceEntity getBalance(Long accountId) {
        return balanceRepository.findByAccountId(accountId);
    }

    public List<BalanceEntity> getUserAccountAndBalances(Long userId) {
        return balanceRepository.findByAccountEntityUserId(userId);
    }

    public List<BalanceEntity> getAllAccountBalances() {
        return balanceRepository.findAll();
    }

    public void addBalance(BalanceRequest balanceRequest) {
        balanceRepository.save(BalanceEntity.builder()
                .accountBalance(balanceRequest.getAccountBalance())
                .accountId(balanceRequest.getAccountId())
                .lastModifiedDate(Timestamp.from(Instant.now()))
                .build());
    }
}
