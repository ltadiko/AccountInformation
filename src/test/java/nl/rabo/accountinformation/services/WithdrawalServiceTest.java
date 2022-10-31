package nl.rabo.accountinformation.services;

import nl.rabo.accountinformation.exceptions.BalanceNotEnoughException;
import nl.rabo.accountinformation.models.WithdrawalRequest;
import nl.rabo.accountinformation.models.entity.AccountEntity;
import nl.rabo.accountinformation.models.entity.BalanceEntity;
import nl.rabo.accountinformation.models.entity.WithdrawalEntity;
import nl.rabo.accountinformation.models.enums.AccountCardType;
import nl.rabo.accountinformation.models.enums.AccountStatus;
import nl.rabo.accountinformation.repository.BalanceRepository;
import nl.rabo.accountinformation.repository.WithdrawalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WithdrawalServiceTest {
    private WithdrawalService underTest;
    @Mock
    private WithdrawalRepository withdrawalRepository;
    @Mock
    private BalanceRepository balanceRepository;

    @Captor
    private ArgumentCaptor<WithdrawalEntity> withdrawalEntityArgumentCaptor;
    @Captor
    private ArgumentCaptor<BalanceEntity> balanceEntityArgumentCaptor;

    @BeforeEach
    void setup() {
        underTest = new WithdrawalService(withdrawalRepository, balanceRepository, 1.01);
    }

    @Test
    @DisplayName("Should add a debit transaction when balance is present in account and debit card")
    void addDebitTransactionSuccessfulWithDebitCard() {
        //given

        Timestamp timeNow = Timestamp.from(Instant.now());
        AccountEntity accountEntity = new AccountEntity(1, 1, "NL77rabo1234", AccountCardType.DEBIT_CARD, AccountStatus.ACTIVE, timeNow, timeNow);
        BalanceEntity balanceEntity = new BalanceEntity(1, 100, timeNow, accountEntity);
        when(balanceRepository.findByAccountId(1)).thenReturn(balanceEntity);
        WithdrawalRequest withdrawalRequest = new WithdrawalRequest(10.0, "RaboMaarssen1234");

        //when
        underTest.addWithdrawal(withdrawalRequest, 1, 1);

        //then
        verify(withdrawalRepository).save(withdrawalEntityArgumentCaptor.capture());
        WithdrawalEntity withdrawalEntity = withdrawalEntityArgumentCaptor.getValue();
        assertEquals(10, withdrawalEntity.getWithdrawalAmount());
        verify(balanceRepository).findByAccountId(1);
        verify(balanceRepository).save(balanceEntityArgumentCaptor.capture());
        BalanceEntity newBalanceEntity = balanceEntityArgumentCaptor.getValue();
        assertEquals(90, newBalanceEntity.getAccountBalance());
        verifyNoMoreInteractions(balanceRepository, withdrawalRepository);
    }


    @Test
    @DisplayName("Should add a debit transaction when balance is present in account and credit card")
    void addDebitTransactionSuccessfulWithCreditCard() {
        //given

        Timestamp timeNow = Timestamp.from(Instant.now());
        AccountEntity accountEntity = new AccountEntity(1, 1, "NL77rabo1234", AccountCardType.CREDIT_CARD, AccountStatus.ACTIVE, timeNow, timeNow);
        BalanceEntity balanceEntity = new BalanceEntity(1, 100, timeNow, accountEntity);
        when(balanceRepository.findByAccountId(1)).thenReturn(balanceEntity);
        WithdrawalRequest withdrawalRequest = new WithdrawalRequest(10.0, "RaboMaarssen1234");

        //when
        underTest.addWithdrawal(withdrawalRequest, 1, 1);

        //then
        verify(withdrawalRepository).save(withdrawalEntityArgumentCaptor.capture());
        WithdrawalEntity withdrawalEntity = withdrawalEntityArgumentCaptor.getValue();
        assertEquals(10.1, withdrawalEntity.getWithdrawalAmount());
        verify(balanceRepository).findByAccountId(1);
        verify(balanceRepository).save(balanceEntityArgumentCaptor.capture());
        BalanceEntity newBalanceEntity = balanceEntityArgumentCaptor.getValue();
        assertEquals(89.9, newBalanceEntity.getAccountBalance());
        verifyNoMoreInteractions(balanceRepository, withdrawalRepository);
    }


    @Test
    @DisplayName("Should add a debit transaction when balance is present in account and credit card")
    void addDebitTransactionUnSuccessfulWithDebitCard() {
        //given

        Timestamp timeNow = Timestamp.from(Instant.now());
        AccountEntity accountEntity = new AccountEntity(1, 1, "NL77rabo1234", AccountCardType.CREDIT_CARD, AccountStatus.ACTIVE, timeNow, timeNow);
        BalanceEntity balanceEntity = new BalanceEntity(1, 100, timeNow, accountEntity);
        when(balanceRepository.findByAccountId(1)).thenReturn(balanceEntity);
        WithdrawalRequest withdrawalRequest = new WithdrawalRequest(10.0, "Nl1234567890");

        //when
        underTest.addWithdrawal(withdrawalRequest, 1, 1);

        //then
        verify(withdrawalRepository).save(withdrawalEntityArgumentCaptor.capture());
        WithdrawalEntity withdrawalEntity = withdrawalEntityArgumentCaptor.getValue();
        assertEquals(10.1, withdrawalEntity.getWithdrawalAmount());
        verify(balanceRepository).findByAccountId(1);
        verify(balanceRepository).save(balanceEntityArgumentCaptor.capture());
        BalanceEntity newBalanceEntity = balanceEntityArgumentCaptor.getValue();
        assertEquals(89.9, newBalanceEntity.getAccountBalance());
        verifyNoMoreInteractions(balanceRepository, withdrawalRepository);
    }

    @Test
    @DisplayName("Should not add a debit transaction when balance is less than transaction amount")
    void addDebitTransactionUnSuccessfulWithCreditCard() {
        //given

        Timestamp timeNow = Timestamp.from(Instant.now());
        AccountEntity accountEntity = new AccountEntity(1, 1, "NL77rabo1234", AccountCardType.CREDIT_CARD, AccountStatus.ACTIVE, timeNow, timeNow);
        BalanceEntity balanceEntity = new BalanceEntity(1, 9.9, timeNow, accountEntity);
        when(balanceRepository.findByAccountId(1)).thenReturn(balanceEntity);
        WithdrawalRequest withdrawalRequest = new WithdrawalRequest(10.0, "Nl1234567890");

        //when
        assertThrows(BalanceNotEnoughException.class, () -> underTest.addWithdrawal(withdrawalRequest, 1, 1));

        //then
        verify(balanceRepository).findByAccountId(1);
        verifyNoMoreInteractions(balanceRepository, withdrawalRepository);
    }


}
