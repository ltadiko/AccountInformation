package nl.rabo.accountinformation.controllers;

import nl.rabo.accountinformation.models.TransactionRequest;
import nl.rabo.accountinformation.models.entity.TransactionEntity;
import nl.rabo.accountinformation.models.enums.TransactionType;
import nl.rabo.accountinformation.services.TransactionService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(value = "classpath:application.yml")
@AutoConfigureMockMvc
class TransactionControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionService transactionService;

    @Test
    @DisplayName("SHOULD be able to add user")
    void addTransaction() throws Exception {
        // given
        Timestamp timeNow = Timestamp.from(Instant.now());
        TransactionRequest transactionRequest = new TransactionRequest(TransactionType.DEBIT, 10.0, "Nl1234567890");

        // when

        MockHttpServletResponse response = mockMvc.perform(post("/api/open-banking/v1.0/users/{userId}/accounts/{accountId}/transactions", 1, 1)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(JsonConverterUtil.convertToJson(transactionRequest)))
                .andReturn()
                .getResponse();
        // then
        assertEquals(200, response.getStatus());
        verify(transactionService).addTransaction(any(), eq(1L), eq(1L));
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    @DisplayName("SHOULD return Bad request when amount is not valid")
    void addTransactionWhenTransactionAmountIsNegative() throws Exception {
        // given
        Timestamp timeNow = Timestamp.from(Instant.now());
        TransactionRequest transactionRequest = new TransactionRequest(TransactionType.DEBIT, -10.0, "Nl1234567890");

        // when

        MockHttpServletResponse response = mockMvc.perform(post("/api/open-banking/v1.0/users/{userId}/accounts/{accountId}/transactions", 1, 1)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(JsonConverterUtil.convertToJson(transactionRequest)))
                .andReturn()
                .getResponse();
        // then
        assertEquals(400, response.getStatus());
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    @DisplayName("SHOULD return transaction details")
    void getTransactions() throws Exception {
        // given
        Timestamp timeNow = Timestamp.from(Instant.now());
        List<TransactionEntity> transactionEntityList = Arrays.asList(new TransactionEntity(1, 1, TransactionType.DEBIT, 10, "NL77rabo1234", 100, timeNow),
                new TransactionEntity(1, 1, TransactionType.CREDIT, 10, "NL77rabo1234", 110, timeNow));
        when(transactionService.getTransactions(anyLong(), any(), any())).thenReturn(transactionEntityList);
        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/open-banking/v1.0/users/{userid}/accounts/{accountId}/transactions?fromDate=01.01.2021&toDate=01.12.2022", "1", "1")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andReturn()
                .getResponse();
        // then
        List<TransactionEntity> transactionEntities = JsonConverterUtil.convertFromJson(response.getContentAsString(), List.class);
        assertEquals(2, transactionEntities.size());
        assertEquals(200, response.getStatus());
        verify(transactionService).getTransactions(anyLong(), any(), any());
        verifyNoMoreInteractions(transactionService);
    }
}
