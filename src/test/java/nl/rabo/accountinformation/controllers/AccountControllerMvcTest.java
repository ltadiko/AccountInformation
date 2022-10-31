package nl.rabo.accountinformation.controllers;

import nl.rabo.accountinformation.exceptions.AccountNotFoundException;
import nl.rabo.accountinformation.models.AccountRequest;
import nl.rabo.accountinformation.models.entity.AccountEntity;
import nl.rabo.accountinformation.models.enums.AccountCardType;
import nl.rabo.accountinformation.models.enums.AccountStatus;
import nl.rabo.accountinformation.services.AccountService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(value = "classpath:application.yml")
@AutoConfigureMockMvc
class AccountControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;

    @Test
    @DisplayName("SHOULD return one user details")
    void getAccount() throws Exception {
        // given
        Timestamp timeNow = Timestamp.from(Instant.now());
        AccountEntity accountEntity = new AccountEntity(1, 1, "1234567890", AccountCardType.DEBIT_CARD, AccountStatus.ACTIVE, timeNow, timeNow);
        when(accountService.getAccount(1L, 1L)).thenReturn(accountEntity);
        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/open-banking/v1.0/users/{userId}/accounts/{accountId}", "1", "1")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andReturn()
                .getResponse();
        // then
        AccountEntity account = JsonConverterUtil.convertFromJson(response.getContentAsString(), AccountEntity.class);
        assertEquals(1, account.getUserId());
        assertEquals(1, account.getAccountId());
        assertEquals("1234567890", account.getAccountNumber());
        assertEquals(AccountCardType.DEBIT_CARD, account.getAccountCardType());
        assertEquals(200, response.getStatus());
        verify(accountService).getAccount(1L, 1L);
        verifyNoMoreInteractions(accountService);
    }


    @Test
    @DisplayName("SHOULD return 404 not found when user id is not configured")
    void getAccountWhenNotFound() throws Exception {
        // given
        Timestamp timeNow = Timestamp.from(Instant.now());
        doThrow(AccountNotFoundException.class).when(accountService).getAccount(1L, 1L);

        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/open-banking/v1.0/users/{userId}/accounts/{accountId}", "1", "1")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andReturn()
                .getResponse();
        // then
        assertEquals(404, response.getStatus());
        verify(accountService).getAccount(1L, 1L);
        verifyNoMoreInteractions(accountService);
    }

    @Test
    @DisplayName("SHOULD return 400 Bad request when user id is not configured")
    void getAccountWhenParameterIsNotPassed() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/open-banking/v1.0/users/\"\"/accounts/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andReturn()
                .getResponse();
        // then
        assertEquals(400, response.getStatus());
        verifyNoMoreInteractions(accountService);

    }

    @Test
    @DisplayName("SHOULD be able to add user")
    void addAccount() throws Exception {
        // given
        AccountRequest accountRequest = new AccountRequest(1, "NLRABO123457890", AccountCardType.DEBIT_CARD, AccountStatus.ACTIVE);

        // when

        MockHttpServletResponse response = mockMvc
                .perform(post("/api/open-banking/v1.0/users/1/accounts")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(JsonConverterUtil.convertToJson(accountRequest)))
                .andReturn()
                .getResponse();
        // then
        assertEquals(200, response.getStatus());
        verify(accountService).addAccount(any());
        verifyNoMoreInteractions(accountService);
    }

    @Test
    @DisplayName("SHOULD return Bad request when Mandatory fields are missing")
    void addUserWhenMandatoryFieldsMissing() throws Exception {
        // given
        AccountRequest accountRequest = new AccountRequest(1, "", AccountCardType.DEBIT_CARD, AccountStatus.ACTIVE);

        // when

        MockHttpServletResponse response = mockMvc
                .perform(post("/api/open-banking/v1.0/users")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(JsonConverterUtil.convertToJson(accountRequest)))
                .andReturn()
                .getResponse();
        // then
        assertEquals(400, response.getStatus());
    }


}
