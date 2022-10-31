package nl.rabo.accountinformation.models.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "WITHDRAWALS")
public class WithdrawalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WITHDRAWAL_ID")
    private long withdrawalId;
    @Column(name = "ACCOUNT_ID")
    private long accountId;
    @Column(name = "WITHDRAWAL_AMOUNT")
    private double withdrawalAmount;
    @Column(name = "FROM_MACHINE_NUMBER")
    private String fromMachineNumber;
    @Column(name = "ACCOUNT_BALANCE")
    private double accountBalance;
    @Column(name = "WITHDRAWAL_DATE")
    private Timestamp withdrawalDate;
}
