package nl.rabo.accountinformation.controllers;

import nl.rabo.accountinformation.models.TransactionRequest;
import nl.rabo.accountinformation.models.entity.TransactionEntity;
import nl.rabo.accountinformation.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author: Lakshmaiah Tatikonda
 * Transaction controller contains transactions rest operations
 */

@RestController
@RequestMapping("/api/open-banking/v1.0")
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping(path = "/users/{userId}/accounts/{accountId}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TransactionEntity> getTransactions(@PathVariable @NotNull Long userId,
                                                   @PathVariable @NotNull Long accountId,
                                                   @RequestParam @Nullable @DateTimeFormat(pattern = "dd.MM.yyyy") Date fromDate,
                                                   @RequestParam @Nullable @DateTimeFormat(pattern = "dd.MM.yyyy") Date toDate) {
        return transactionService.getTransactions(accountId, fromDate, toDate);
    }


    @PostMapping(path = "/users/{userId}/accounts/{accountId}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public void postTransaction(@RequestBody @Valid TransactionRequest transactionRequest,
                                @PathVariable @NotNull Long userId,
                                @PathVariable @NotNull Long accountId) {
        transactionService.addTransaction(transactionRequest, userId, accountId);
    }

}
