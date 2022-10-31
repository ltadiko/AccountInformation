package nl.rabo.accountinformation.repository;

import nl.rabo.accountinformation.models.entity.WithdrawalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface WithdrawalRepository extends JpaRepository<WithdrawalEntity, Long> {
    List<WithdrawalEntity> findByAccountIdAndWithdrawalDateBetween(long accountId, Timestamp fromDate, Timestamp endDate);

}
