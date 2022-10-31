package nl.rabo.accountinformation.controllers;

import nl.rabo.accountinformation.exceptions.UserNotFoundException;
import nl.rabo.accountinformation.models.UserRequest;
import nl.rabo.accountinformation.models.entity.UserEntity;
import nl.rabo.accountinformation.models.enums.UserStatus;
import nl.rabo.accountinformation.services.UserService;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(value = "classpath:application.yml")
@AutoConfigureMockMvc
class UserControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;


    @Test
    @DisplayName("SHOULD return list of users")
    void getUsers() throws Exception {
        // given
        Timestamp timeNow = Timestamp.from(Instant.now());
        when(userService.getUsers()).thenReturn(Arrays.asList(new UserEntity(1, "voornaam", "acthernaam", UserStatus.ACTIVE, "test@gmail.com", timeNow, timeNow),
                new UserEntity(2, "firstname", "last name", UserStatus.INACTIVE, "test@gmail.com", timeNow, timeNow)));
        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/open-banking/v1.0/users")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andReturn()
                .getResponse();
        // then
        List<UserEntity> users = JsonConverterUtil.convertFromJson(response.getContentAsString(), List.class);
        assertEquals(2, users.size());
        assertEquals(200, response.getStatus());
        verify(userService).getUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("SHOULD return empty when users are not added")
    void getUsersWhenUsersAreNotPresent() throws Exception {
        // given
        when(userService.getUsers()).thenReturn(Collections.EMPTY_LIST);
        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/open-banking/v1.0/users")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andReturn()
                .getResponse();
        // then
        List<UserEntity> users = JsonConverterUtil.convertFromJson(response.getContentAsString(), List.class);
        assertEquals(0, users.size());
        assertEquals(200, response.getStatus());
        verify(userService).getUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("SHOULD return one user details")
    void getUser() throws Exception {
        // given
        Timestamp timeNow = Timestamp.from(Instant.now());
        when(userService.getUser(1L)).thenReturn(new UserEntity(1, "voornaam", "acthernaam", UserStatus.ACTIVE, "test@gmail.com", timeNow, timeNow));
        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/open-banking/v1.0/users/1", "1")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andReturn()
                .getResponse();
        // then
        UserEntity user = JsonConverterUtil.convertFromJson(response.getContentAsString(), UserEntity.class);
        assertEquals(1, user.getUserId());
        assertEquals("voornaam", user.getFirstName());
        assertEquals("acthernaam", user.getLastName());
        assertEquals("test@gmail.com", user.getEmail());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertEquals(200, response.getStatus());
        verify(userService).getUser(1L);
        verifyNoMoreInteractions(userService);
    }


    @Test
    @DisplayName("SHOULD return 404 not found when user id is not configured")
    void getUserWhenNotFound() throws Exception {
        // given
        doThrow(UserNotFoundException.class).when(userService).getUser(1L);
        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/open-banking/v1.0/users/{userid}", "1")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andReturn()
                .getResponse();
        // then
        assertEquals(404, response.getStatus());
        verify(userService).getUser(1L);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("SHOULD return 400 Bad request when user id is not configured")
    void getUserWhenParameterIsNotPassed() throws Exception {
        // given
        doThrow(UserNotFoundException.class).when(userService).getUser(1L);
        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/open-banking/v1.0/users/\"\"")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andReturn()
                .getResponse();
        // then
        assertEquals(400, response.getStatus());
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("SHOULD be able to add user")
    void addUser() throws Exception {
        // given
        Timestamp timeNow = Timestamp.from(Instant.now());
        UserRequest userRequest = new UserRequest("firstName", "lastname", UserStatus.ACTIVE, "testcaseone@gmail.com");

        // when

        MockHttpServletResponse response = mockMvc
                .perform(post("/api/open-banking/v1.0/users")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(JsonConverterUtil.convertToJson(userRequest)))
                .andReturn()
                .getResponse();
        // then
        assertEquals(200, response.getStatus());
        verify(userService).addUser(any());
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("SHOULD return Bad request when Mandatory fields are missing")
    void addUserWhenMandatoryFieldsMissing() throws Exception {
        // given
        UserRequest userRequest = new UserRequest("", "lastname", UserStatus.ACTIVE, "testcaseone@gmail.com");
        String userRequestJSON = JsonConverterUtil.convertToJson(userRequest);
        // when

        MockHttpServletResponse response = mockMvc
                .perform(post("/api/open-banking/v1.0/users")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"firstName\":\"\",\"lastName\":\"lastname\",\"status\":\"ACTIVE\",\"email\":\"testcaseone@gmail.com\"}"))
                        .andReturn()
                        .getResponse();
        // then
        assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("SHOULD return Bad request when first name is more than 40 characters")
    void addUserWhenMandatoryFieldIsLonger() throws Exception {
        // given
        // when

        MockHttpServletResponse response = mockMvc
                .perform(post("/api/open-banking/v1.0/users")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"firstName\":\"aaaabbbbb12345678901234567890123456789012345678901\",\"lastName\":\"lastname\",\"status\":\"ACTIVE\",\"email\":\"testcaseone@gmail.com\"}"))
                .andReturn()
                .getResponse();
        // then
        assertEquals(400, response.getStatus());
    }
}
