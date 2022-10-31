package nl.rabo.accountinformation.repository;

import nl.rabo.accountinformation.models.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    AccountEntity findByUserIdAndAccountId(Long userId, Long accountId);
    List<AccountEntity> findByUserId(Long userId);
}
