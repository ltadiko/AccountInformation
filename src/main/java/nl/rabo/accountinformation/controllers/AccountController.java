package nl.rabo.accountinformation.controllers;

import nl.rabo.accountinformation.models.AccountRequest;
import nl.rabo.accountinformation.models.entity.AccountEntity;
import nl.rabo.accountinformation.services.AccountService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: Lakshmaiah Tatikonda
 * Account controller contains account crud operations
 */

@RestController
@RequestMapping("/api/open-banking/v1.0")
@Validated
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping(path = "/users/{userId}/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AccountEntity> getUserAccounts(@PathVariable @NotNull Long userId) {
        return accountService.getUserAccounts(userId);
    }

    @GetMapping(path = "/users/{userId}/accounts/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountEntity getUserAccounts(@PathVariable @NotNull Long userId,
                                         @PathVariable @NotNull Long accountId) {
        return accountService.getAccount(userId, accountId);
    }

    @PostMapping(path = "/users/{userId}/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public void addUser(@RequestBody @Valid AccountRequest accountRequest) {
        accountService.addAccount(accountRequest);
    }

    @DeleteMapping(path = "/users/{userId}/accounts/{accountId}")
    public void deleteUser(@PathVariable @NotNull Long accountId) {
        accountService.deleteAccount(accountId);
    }
}
