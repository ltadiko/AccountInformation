package nl.rabo.accountinformation.models.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "BALANCES")
public class BalanceEntity {
    @Id
    @Column(name = "ACCOUNT_ID")
    private long accountId;
    @Column(name = "ACCOUNT_BALANCE")
    @Setter
    private double accountBalance;
    @Column(name = "LAST_MODIFIED_DATE")
    private Timestamp lastModifiedDate;

    @OneToOne
    @MapsId
    @JoinColumn(name = "ACCOUNT_ID")
    private AccountEntity accountEntity;
}
