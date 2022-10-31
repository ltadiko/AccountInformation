package nl.rabo.accountinformation.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WithdrawalRequest {
    @NotNull
    @Min(0)
    private Double withdrawalAmount;
    @NotEmpty
    private String fromMachineNumber;
}
