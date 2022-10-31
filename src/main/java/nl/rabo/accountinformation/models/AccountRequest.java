package nl.rabo.accountinformation.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.rabo.accountinformation.models.enums.AccountCardType;
import nl.rabo.accountinformation.models.enums.AccountStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountRequest {
    @NotNull
    private long userId;
    @NotEmpty
    private String accountNumber;
    @NotNull
    private AccountCardType accountCardType;
    @NotNull
    private AccountStatus accountStatus;
}
