package nl.rabo.accountinformation.models.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import nl.rabo.accountinformation.models.enums.CardStatus;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "CARDS")
public class CardEntity {
    @Id
    @Column(name = "CARD_ID")
    private long cardId;
    @Column(name = "ACCOUNT_ID")
    private long accountId;
    @Column(name = "CARD_NUMBER")
    @Setter
    private String cardNumber;
    @Column(name = "PIN")
    @Setter
    private String pin;
    @Column(name = "CVV")
    @Setter
    private String cvv;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private CardStatus cardStatus;
    @Column(name = "EXPIRY_DATE")
    private Timestamp expiryDate;
    @Column(name = "LAST_MODIFIED_DATE")
    private Timestamp lastModifiedDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false, insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AccountEntity accountEntity;
}



