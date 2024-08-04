package org.nmb.versions.nmbkeycloak.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.log4j.Log4j2;

import java.text.SimpleDateFormat;
import java.util.*;


@Log4j2
public class JHelper {


    /**
     * JSON Helpers
     */
    public static String toJson(Object object) {

        if (object == null) {
            return null;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            return objectMapper.writeValueAsString(object);
        } catch (Exception exception) {
            log.error("Could not convert object to JSON String {} {}", exception.getMessage(), object);
            return "null";
        }
    }

    public static <T> T fromJson(String jsonString, Class<T> classOfT) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            return objectMapper.readValue(jsonString, classOfT);
        } catch (Exception exception) {
            log.error("Could not convert object FROM JSON String {} {}", exception.getMessage(), jsonString);
            throw new RuntimeException("Failed to convert json string to java object");
        }
    }

    public static <T> T fromJson(String jsonString, TypeReference<T> typeReference) throws RuntimeException {
        T data;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
            data = objectMapper.readValue(jsonString, typeReference);
        } catch (Exception exception) {
            log.error("Could not convert to object FROM JSON String {} {}", exception.getMessage(), jsonString);
            throw new RuntimeException("Failed to convert json string to java object");
        }
        return data;
    }



    public static HashMap<String, String> toMap(Object object) {
        String json = toJson(object);
        return fromJson(json, new TypeReference<>() {});
    }


    /**
     * Returns specified n last characters
     */
    public static String getAccountSuffix(String account, int charCount) {
        return account.substring((account.length()) - charCount);
    }

    public static String formatCurrentDate(String format) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * String helpers
     **/
    public static boolean isEmpty(String value) {
        if (value == null) {
            return true;
        }
        return value.trim().isEmpty();
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static boolean areEmpty(String value1, String value2) {
        if (value1 == null || value2 == null) {
            return true;
        }
        return value1.trim().isEmpty() || value2.trim().isEmpty();
    }

    public static boolean areNotEmpty(String value1, String value2) {
        return !areEmpty(value1, value2);
    }

    public static boolean areEmpty(String value1, String value2, String value3) {
        if (value1 == null || value2 == null || value3 == null) {
            return true;
        }
        return value1.trim().isEmpty() || value2.trim().isEmpty() || value3.trim().isEmpty();
    }

    public static boolean areNotEmpty(String value1, String value2, String value3) {
        return !areEmpty(value1, value2, value3);
    }

    public static String render(String baseString, Object... arguments) {
        for (Object arg : arguments) {
            baseString = baseString.replaceFirst("\\{}", arg == null ? "null" : String.valueOf(arg));
        }
        return baseString;
    }

    public static String getMessage(Exception exception, String defaultMessage) {
        try {
            if (exception instanceof RuntimeException) {
                return defaultMessage + " " + exception.getMessage();
            }

            return exception.getClass().getSimpleName() + " " + defaultMessage;
        } catch (Exception e) {
            return defaultMessage;
        }
    }

    public static String phraseException(Exception exception, String defaultMessage) {
        try {
            if (exception instanceof RuntimeException) {
                return defaultMessage + " " + exception.getMessage();
            }
            return exception.getClass().getSimpleName() + " " + defaultMessage;
        } catch (Exception e) {
            return defaultMessage;
        }
    }

}
