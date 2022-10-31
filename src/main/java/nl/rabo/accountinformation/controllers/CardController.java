package nl.rabo.accountinformation.controllers;

import nl.rabo.accountinformation.models.CardRequest;
import nl.rabo.accountinformation.models.entity.CardEntity;
import nl.rabo.accountinformation.services.CardService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: Lakshmaiah Tatikonda
 * Account card controller contains card apis
 */

@RestController
@RequestMapping("/api/open-banking/v1.0")
@Validated
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping(path = "/users/{userId}/accounts/{accountId}/cards", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CardEntity> getUserAccountCards(@PathVariable @NotNull Long userId,
                                                @PathVariable @NotNull Long accountId) {
        return cardService.getCards(accountId);
    }

    @GetMapping(path = "/users/{userId}/cards", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CardEntity> getUserAccountAndBalances(@PathVariable @NotNull Long userId) {
        return cardService.getUserAccountAndCards(userId);
    }

    @PostMapping(path = "/users/{userId}/accounts/{accountId}/cars", produces = MediaType.APPLICATION_JSON_VALUE)
    public void addUser(@RequestBody @Valid CardRequest cardRequest,
                        @PathVariable @NotNull Long accountId) {
        cardService.addCard(cardRequest, accountId);
    }

}
