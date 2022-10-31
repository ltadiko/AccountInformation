package nl.rabo.accountinformation.repository;

import nl.rabo.accountinformation.models.entity.BalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceRepository extends JpaRepository<BalanceEntity, Long> {
    BalanceEntity findByAccountId(long accountId);

    List<BalanceEntity> findByAccountEntityUserId(long userId);
}
