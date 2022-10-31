package nl.rabo.accountinformation.models.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.rabo.accountinformation.models.enums.AccountCardType;
import nl.rabo.accountinformation.models.enums.AccountStatus;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "ACCOUNTS")
public class AccountEntity {
    @Id
    @Column(name = "ACCOUNT_ID")
    private long accountId;
    @Column(name = "USER_ID")
    private long userId;
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE_OF_ACCOUNT")
    private AccountCardType accountCardType;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;
    @Column(name = "LAST_MODIFIED_DATE")
    private Timestamp lastModifiedDate;

}
