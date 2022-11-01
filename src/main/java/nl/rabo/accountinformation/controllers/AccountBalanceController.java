package nl.rabo.accountinformation.controllers;

import nl.rabo.accountinformation.models.BalanceRequest;
import nl.rabo.accountinformation.models.entity.BalanceEntity;
import nl.rabo.accountinformation.services.BalanceService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: Lakshmaiah Tatikonda
 * Account balance controller contains balances related apis.
 */

@RestController
@RequestMapping("/api/open-banking/v1.0")
@Validated
public class AccountBalanceController {
    private final BalanceService balanceService;

    public AccountBalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    /**
     * @param accountId : unique identifier of the account
     * @return : Balance information of the account
     */
    @GetMapping(path = "/accounts/{accountId}/balances", produces = MediaType.APPLICATION_JSON_VALUE)
    public BalanceEntity getAccountBalance(@PathVariable @NotNull Long accountId) {
        return balanceService.getBalance(accountId);
    }

    /**
     * @param userId : unique identifier (primary key) of the user
     * @return returns all user accounts balances
     */
    @GetMapping(path = "/users/{userId}/balances", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BalanceEntity> getUserAccountsAndBalances(@PathVariable @NotNull Long userId) {
        return balanceService.getUserAccountAndBalances(userId);
    }

    /**
     * @return : returns all accounts and balances
     */
    @GetMapping(path = "/accounts/balances", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BalanceEntity> getAllAccountBalances() {
        return balanceService.getAllAccountBalances();
    }

    /**
     * @param balanceRequest : contains balance amount to be updates for the specific account
     */
    @PostMapping(path = "/users/{userId}/accounts/{accountId}/balances", produces = MediaType.APPLICATION_JSON_VALUE)
    public void addBalance(@RequestBody @Valid BalanceRequest balanceRequest) {
        balanceService.addBalance(balanceRequest);
    }

}
