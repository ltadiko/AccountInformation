package nl.rabo.accountinformation.repository;

import nl.rabo.accountinformation.models.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
    List<CardEntity> findByAccountEntityUserId(Long userId);

    List<CardEntity> findByAccountId(Long accountId);
}
