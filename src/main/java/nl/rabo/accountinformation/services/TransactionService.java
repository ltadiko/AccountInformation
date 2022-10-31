package nl.rabo.accountinformation.services;

import lombok.extern.slf4j.Slf4j;
import nl.rabo.accountinformation.exceptions.BalanceNotEnoughException;
import nl.rabo.accountinformation.models.TransactionRequest;
import nl.rabo.accountinformation.models.entity.BalanceEntity;
import nl.rabo.accountinformation.models.entity.TransactionEntity;
import nl.rabo.accountinformation.models.enums.AccountCardType;
import nl.rabo.accountinformation.models.enums.TransactionType;
import nl.rabo.accountinformation.repository.BalanceRepository;
import nl.rabo.accountinformation.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;
    private final double creditCardTransactionCharges;


    @Autowired
    public TransactionService(TransactionRepository transactionRepository, BalanceRepository balanceRepository, @Value("${transaction.creditCardTransactionCharges}") Double creditCardTransactionCharges) {
        this.transactionRepository = transactionRepository;
        this.balanceRepository = balanceRepository;
        this.creditCardTransactionCharges = creditCardTransactionCharges;
    }

    public List<TransactionEntity> getTransactions(Long accountId, Date fromDate, Date toDate) {
        Timestamp fromTimestamp = fromDate != null ? new Timestamp(fromDate.getTime()) : Timestamp.from(Instant.now().minus(3, ChronoUnit.MONTHS));
        Timestamp toTimestamp = toDate != null ? new Timestamp(toDate.getTime()) : Timestamp.from(Instant.now());
        return transactionRepository.findByAccountIdAndTransactionDateBetween(accountId, fromTimestamp, toTimestamp);
    }

    public void addTransaction(TransactionRequest transactionRequest, final long userId, final long accountId) {
        BalanceEntity balanceEntity = balanceRepository.findByAccountId(accountId);
        double newAccountBalance;
        double calculatedTransactionAmount = 0;

        if (TransactionType.DEBIT == transactionRequest.getTransactionType()) {
            AccountCardType accountCardType = balanceEntity.getAccountEntity().getAccountCardType();
            calculatedTransactionAmount = deriveTransactionAmount(transactionRequest, accountCardType);
            if (balanceEntity.getAccountBalance() < calculatedTransactionAmount) {
                log.info("Balance is not enough in account id {} for user id {}", accountId, userId);
                throw new BalanceNotEnoughException("Balance is not enough to make the transaction");
            }
            newAccountBalance = balanceEntity.getAccountBalance() - calculatedTransactionAmount;
        } else {
            calculatedTransactionAmount = transactionRequest.getTransactionAmount();
            newAccountBalance = balanceEntity.getAccountBalance() + transactionRequest.getTransactionAmount();
        }
        transactionRepository.save(TransactionEntity.builder()
                .accountId(accountId)
                .transactionType(transactionRequest.getTransactionType())
                .transactionAmount(calculatedTransactionAmount)
                .toAccountNumber(transactionRequest.getToAccountNumber())
                .accountBalance(newAccountBalance)
                .transactionDate(Timestamp.from(Instant.now()))
                .build());
        balanceEntity.setAccountBalance(newAccountBalance);
        balanceRepository.save(balanceEntity);
    }

    private double deriveTransactionAmount(TransactionRequest transactionRequest, AccountCardType accountCardType) {
        double originalTransactionAmount = transactionRequest.getTransactionAmount();
        return AccountCardType.DEBIT_CARD == accountCardType ? originalTransactionAmount : creditCardTransactionCharges * originalTransactionAmount;
    }

}
