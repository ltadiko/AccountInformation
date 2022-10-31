package nl.rabo.accountinformation.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.rabo.accountinformation.models.enums.CardStatus;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardRequest {
    private String cardNumber;
    private String pin;
    private String cvv;
    private CardStatus cardStatus;
    private Timestamp expiryDate;
}
