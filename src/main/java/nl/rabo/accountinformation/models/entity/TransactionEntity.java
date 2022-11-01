package nl.rabo.accountinformation.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.rabo.accountinformation.models.enums.TransactionType;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "TRANSACTIONS")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTION_ID")
    private long transactionId;
    @Column(name = "ACCOUNT_ID")
    private long accountId;
    @Column(name = "TRANSACTION_TYPE")
    private TransactionType transactionType;
    @Column(name = "TRANSACTION_AMOUNT")
    private double transactionAmount;
    @Column(name = "TO_ACCOUNT_NUMBER")
    private String toAccountNumber;
    @Column(name = "ACCOUNT_BALANCE")
    private double accountBalance;
    @Column(name = "TRANSACTION_DATE")
    private Timestamp transactionDate;
}
