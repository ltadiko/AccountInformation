package nl.rabo.accountinformation.services;

import lombok.extern.slf4j.Slf4j;
import nl.rabo.accountinformation.exceptions.BalanceNotEnoughException;
import nl.rabo.accountinformation.models.WithdrawalRequest;
import nl.rabo.accountinformation.models.entity.BalanceEntity;
import nl.rabo.accountinformation.models.entity.WithdrawalEntity;
import nl.rabo.accountinformation.models.enums.AccountCardType;
import nl.rabo.accountinformation.repository.BalanceRepository;
import nl.rabo.accountinformation.repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * @author : Lakshmaiah Tatikonda
 * Withdrawal service implements managing withdrawal business logic by communicating with backend database
 */
@Service
@Slf4j
public class WithdrawalService {
    private final WithdrawalRepository withdrawalRepository;
    private final BalanceRepository balanceRepository;
    private final double creditCardTransactionCharges;


    @Autowired
    public WithdrawalService(WithdrawalRepository withdrawalRepository,
                             BalanceRepository balanceRepository,
                             @Value("${withdrawal.creditCardTransactionCharges}") double creditCardTransactionCharges) {
        this.withdrawalRepository = withdrawalRepository;
        this.balanceRepository = balanceRepository;
        this.creditCardTransactionCharges = creditCardTransactionCharges;
    }

    public List<WithdrawalEntity> getWithdrawals(Long accountId, Date fromDate, Date toDate) {
        Timestamp fromTimestamp = fromDate != null ? new Timestamp(fromDate.getTime()) : Timestamp.from(Instant.now().minus(3, ChronoUnit.MONTHS));
        Timestamp toTimestamp = toDate != null ? new Timestamp(toDate.getTime()) : Timestamp.from(Instant.now());
        return withdrawalRepository.findByAccountIdAndWithdrawalDateBetween(accountId, fromTimestamp, toTimestamp);
    }

    public void addWithdrawal(WithdrawalRequest withdrawalRequest, final long userId, final long accountId) {
        BalanceEntity balanceEntity = balanceRepository.findByAccountId(accountId);
        double newAccountBalance;
        AccountCardType accountCardType = balanceEntity.getAccountEntity().getAccountCardType();
        double withdrawalAmount = deriveTransactionAmount(withdrawalRequest, accountCardType);
        if (balanceEntity.getAccountBalance() < withdrawalAmount) {
            log.info("Balance is not enough in account id {} for user id {}", accountId, userId);
            throw new BalanceNotEnoughException("Balance is not enough to make the transaction");
        }
        newAccountBalance = balanceEntity.getAccountBalance() - withdrawalAmount;

        withdrawalRepository.save(WithdrawalEntity.builder()
                .accountId(accountId)
                .withdrawalAmount(withdrawalAmount)
                .fromMachineNumber(withdrawalRequest.getFromMachineNumber())
                .accountBalance(newAccountBalance).withdrawalDate(Timestamp.from(Instant.now())).build());
        balanceEntity.setAccountBalance(newAccountBalance);
        balanceRepository.save(balanceEntity);
    }

    private double deriveTransactionAmount(WithdrawalRequest withdrawalRequest, AccountCardType accountCardType) {
        double originalTransactionAmount = withdrawalRequest.getWithdrawalAmount();
        return AccountCardType.DEBIT_CARD == accountCardType ? originalTransactionAmount : creditCardTransactionCharges * originalTransactionAmount;
    }
}
