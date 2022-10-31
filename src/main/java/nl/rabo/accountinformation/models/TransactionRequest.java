package nl.rabo.accountinformation.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nl.rabo.accountinformation.models.enums.TransactionType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionRequest {
    @NotNull
    private TransactionType transactionType;
    @NotNull
    @Min(0)
    private Double transactionAmount;
    @NotEmpty
    private String toAccountNumber;
}
