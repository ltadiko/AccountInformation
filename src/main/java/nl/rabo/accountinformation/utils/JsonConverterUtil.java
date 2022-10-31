package nl.rabo.accountinformation.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabo.accountinformation.exceptions.JsonConversionException;

import java.io.IOException;

/**
 * JsonConverterUtil helps to convert object to JSON string
 */
public final class JsonConverterUtil {

    private JsonConverterUtil() {
    }

    /**
     * converts any object to json string
     *
     * {@link ObjectMapper#writeValueAsString(Object)}
     *
     * @param object objects which needs json string
     * @return json string
     */
    public static String convertToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonConversionException("Exception while converting to JSON", e);
        }

    }

    /**
     * converts any json to an object of the type that is given
     *
     * @param <T> the type of the object that should be returned
     * @param jsonString the string with json
     * @param clazz      the class to which the json should be converted
     * @return an instantiated object of the class type that is given with the information from the json string
     */
    public static <T> T convertFromJson(String jsonString, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw new JsonConversionException("Failed to convert from Json", e);
        }
    }

}
