package nl.rabo.accountinformation.services;

import nl.rabo.accountinformation.models.CardRequest;
import nl.rabo.accountinformation.models.entity.CardEntity;
import nl.rabo.accountinformation.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * @author : Lakshmaiah Tatikonda
 * cards service implements managing cards business logic by communicating with backend database
 */
@Service
public class CardService {
    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public void addCard(CardRequest cardRequest, Long accountId) {
        cardRepository.save(
                CardEntity.builder()
                        .accountId(accountId)
                        .cardNumber(cardRequest.getCardNumber())
                        .pin(cardRequest.getPin())
                        .cvv(cardRequest.getCvv())
                        .cardStatus(cardRequest.getCardStatus())
                        .expiryDate(cardRequest.getExpiryDate())
                        .lastModifiedDate(Timestamp.from(Instant.now()))
                        .build()
        );


    }

    public List<CardEntity> getUserAccountAndCards(Long userId) {
        return cardRepository.findByAccountEntityUserId(userId);
    }

    public List<CardEntity> getCards(Long accountId) {
        return cardRepository.findByAccountId(accountId);
    }
}
