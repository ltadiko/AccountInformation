package nl.rabo.accountinformation.utils;


import nl.rabo.accountinformation.exceptions.JsonConversionException;
import nl.rabo.accountinformation.models.UserRequest;
import nl.rabo.accountinformation.models.entity.UserEntity;
import nl.rabo.accountinformation.models.enums.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class JsonConverterUtilTest {

    @Test
    @DisplayName("Should convert Name to Json")
    void convertToJson() {
        // given
        UserRequest userEntity = new UserRequest("firstName", "lastname", UserStatus.ACTIVE, "testcaseone@gmail.com");
        // when
        String json = JsonConverterUtil.convertToJson(userEntity);

        // then
        assertEquals("{\"firstName\":\"firstName\",\"lastName\":\"lastname\",\"status\":\"ACTIVE\",\"email\":\"testcaseone@gmail.com\"}", json);
    }

    @Test
    @DisplayName("Should throw JsonConversionException while converting mock object")
    void exceptionConvToJson() {
        UserEntity notSerilizableObj = mock(UserEntity.class);

        assertThrows(JsonConversionException.class, () -> JsonConverterUtil.convertToJson(notSerilizableObj));
    }

    @Test
    @DisplayName("Should instantiate json to given object")
    void convertFromJson() {
        // given
        String json = "{\n" +
                "        \"userId\": 1,\n" +
                "        \"firstName\": \"Lakshman\",\n" +
                "        \"lastName\": \"Test\",\n" +
                "        \"status\": \"ACTIVE\",\n" +
                "        \"email\": \"tlaxman88@gmail.com\",\n" +
                "        \"createdDate\": \"2022-10-29T19:23:54.179+00:00\",\n" +
                "        \"lastModifiedDate\": \"2022-10-29T19:23:54.179+00:00\"\n" +
                "    }";

        // when
        UserEntity userEntity = JsonConverterUtil.convertFromJson(json, UserEntity.class);

        // then
        assertEquals(1, userEntity.getUserId());
        assertEquals("Lakshman", userEntity.getFirstName());
        assertEquals("Test", userEntity.getLastName());

    }

    @Test
    @DisplayName("Should throw JsonConversionException on converting invalid json")
    void exceptionTestFromJson() {
        String input = "invalid json";

        assertThrows(JsonConversionException.class, () -> JsonConverterUtil.convertFromJson(input, UserEntity.class));
    }

}
