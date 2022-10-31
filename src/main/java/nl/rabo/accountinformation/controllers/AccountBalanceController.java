package nl.rabo.accountinformation.controllers;

import nl.rabo.accountinformation.models.entity.BalanceEntity;
import nl.rabo.accountinformation.services.BalanceService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: Lakshmaiah Tatikonda
 * Account balance controller contains balances apis
 */

@RestController
@RequestMapping("/api/open-banking/v1.0")
@Validated
public class AccountBalanceController {
    private final BalanceService balanceService;

    public AccountBalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping(path = "/users/{userId}/accounts/{accountId}/balances", produces = MediaType.APPLICATION_JSON_VALUE)
    public BalanceEntity getUserAccounts(@PathVariable @NotNull Long userId,
                                         @PathVariable @NotNull Long accountId) {
        return balanceService.getBalance(accountId);
    }

    @GetMapping(path = "/users/{userId}/balances", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BalanceEntity> getUserAccountAndBalances(@PathVariable @NotNull Long userId) {
        return balanceService.getUserAccountAndBalances(userId);
    }

    @GetMapping(path = "/accounts/balances", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BalanceEntity> getAllAccountBalances() {
        return balanceService.getAllAccountBalances();
    }
}
