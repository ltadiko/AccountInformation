package nl.rabo.accountinformation.controllers;

import nl.rabo.accountinformation.models.WithdrawalRequest;
import nl.rabo.accountinformation.models.entity.WithdrawalEntity;
import nl.rabo.accountinformation.services.WithdrawalService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/open-banking/v1.0")
@Validated
public class WithdrawalController {
    private final WithdrawalService withdrawalService;

    public WithdrawalController(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }


    @GetMapping(path = "/users/{userId}/accounts/{accountId}/withdrawals", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WithdrawalEntity> getTransactions(@PathVariable @NotNull Long userId,
                                                  @PathVariable @NotNull Long accountId,
                                                  @RequestParam @Nullable @DateTimeFormat(pattern = "dd.MM.yyyy") Date fromDate,
                                                  @RequestParam @Nullable @DateTimeFormat(pattern = "dd.MM.yyyy") Date toDate) {
        return withdrawalService.getWithdrawals(accountId, fromDate, toDate);
    }


    @PostMapping(path = "/users/{userId}/accounts/{accountId}/withdrawals", produces = MediaType.APPLICATION_JSON_VALUE)
    public void postWithdrawal(@RequestBody @Valid WithdrawalRequest withdrawalRequest,
                               @PathVariable @NotNull Long userId,
                               @PathVariable @NotNull Long accountId) {
        withdrawalService.addWithdrawal(withdrawalRequest, userId, accountId);
    }

}
