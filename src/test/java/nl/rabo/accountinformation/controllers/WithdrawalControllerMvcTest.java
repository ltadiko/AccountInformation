package nl.rabo.accountinformation.controllers;

import nl.rabo.accountinformation.models.WithdrawalRequest;
import nl.rabo.accountinformation.models.entity.TransactionEntity;
import nl.rabo.accountinformation.models.entity.WithdrawalEntity;
import nl.rabo.accountinformation.services.WithdrawalService;
import nl.rabo.accountinformation.utils.JsonConverterUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(value = "classpath:application.yml")
@AutoConfigureMockMvc
class WithdrawalControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WithdrawalService withdrawalService;

    @Test
    @DisplayName("SHOULD be able to add user")
    void addTransaction() throws Exception {
        // given
        Timestamp timeNow = Timestamp.from(Instant.now());
        WithdrawalRequest withdrawalRequest = new WithdrawalRequest(10.0, "Nl1234567890");

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/api/open-banking/v1.0/users/{userId}/accounts/{accountId}/withdrawals", 1, 1)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(JsonConverterUtil.convertToJson(withdrawalRequest)))
                .andReturn()
                .getResponse();
        // then
        assertEquals(200, response.getStatus());
        verify(withdrawalService).addWithdrawal(any(), eq(1L), eq(1L));
        verifyNoMoreInteractions(withdrawalService);
    }

    @Test
    @DisplayName("SHOULD return Bad request when amount is not valid")
    void addTransactionWhenTransactionAmountIsNegative() throws Exception {
        // given
        Timestamp timeNow = Timestamp.from(Instant.now());
        WithdrawalRequest withdrawalRequest = new WithdrawalRequest(-10.0, "Nl1234567890");

        // when

        MockHttpServletResponse response = mockMvc.perform(post("/api/open-banking/v1.0/users/{userId}/accounts/{accountId}/withdrawals", 1, 1)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(JsonConverterUtil.convertToJson(withdrawalRequest)))
                .andReturn()
                .getResponse();
        // then
        assertEquals(400, response.getStatus());
        verifyNoMoreInteractions(withdrawalService);
    }

    @Test
    @DisplayName("SHOULD return withdrawal details")
    void getWithDrawals() throws Exception {
        // given
        Timestamp timeNow = Timestamp.from(Instant.now());
        List<WithdrawalEntity> transactionEntityList = Arrays.asList(new WithdrawalEntity(1, 1, 10, "NL77rabo1234", 100, timeNow),
                new WithdrawalEntity(1, 1, 10, "NL77rabo1234", 110, timeNow));
        when(withdrawalService.getWithdrawals(anyLong(), any(), any())).thenReturn(transactionEntityList);
        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/open-banking/v1.0/users/{userid}/accounts/{accountId}/withdrawals?fromDate=01.01.2021&toDate=01.12.2022", "1", "1")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andReturn()
                .getResponse();
        // then
        List<TransactionEntity> transactionEntities = JsonConverterUtil.convertFromJson(response.getContentAsString(), List.class);
        assertEquals(2, transactionEntities.size());
        assertEquals(200, response.getStatus());
        verify(withdrawalService).getWithdrawals(anyLong(), any(), any());
        verifyNoMoreInteractions(withdrawalService);
    }
}
