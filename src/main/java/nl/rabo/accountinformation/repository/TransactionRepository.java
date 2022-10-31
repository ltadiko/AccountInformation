package nl.rabo.accountinformation.repository;

import nl.rabo.accountinformation.models.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findByAccountIdAndTransactionDateBetween(long accountId, Timestamp fromDate, Timestamp endDate);
}
