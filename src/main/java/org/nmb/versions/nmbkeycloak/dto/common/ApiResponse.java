package org.nmb.versions.nmbkeycloak.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonPropertyOrder({"RespStatus", "respCode", "message", "respBody"})
public class ApiResponse<T> {

    private int respCode;

    private T respBody;

    private String title;

    private String message;


    @JsonIgnore
    Object metaData;

    @JsonIgnore
    public static <T> ApiResponse<T> success(String message) {
        return success(null, message);
    }

    public static <T> ApiResponse<T> success(T responseBody, String message) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setRespCode(2000);
        apiResponse.setRespBody(responseBody);
        apiResponse.setMessage(message);
        return apiResponse;
    }

    public static <T> ApiResponse<T> success(String message, int respCode) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setRespCode(respCode);
        apiResponse.setMessage(message);
        apiResponse.setRespBody(null);
        return apiResponse;
    }

    public static <T> ApiResponse<T> success(T responseBody, String title, String message) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setRespCode(2000);
        apiResponse.setRespBody(responseBody);
        apiResponse.setMessage(message);
        apiResponse.setTitle(title);
        return apiResponse;
    }

    public static <T> ApiResponse<T> success(T responseBody, int respCode, String message) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setRespCode(respCode);
        apiResponse.setRespBody(responseBody);
        apiResponse.setMessage(message);
        return apiResponse;
    }


    @JsonIgnore
    public static <T> ApiResponse<T> failure(String message) {
        return failure(null, message);
    }

    @JsonIgnore
    public static <T> ApiResponse<T> failure(String title, String message) {
        ApiResponse<T> ApiResponse = new ApiResponse<>();
        ApiResponse.setRespCode(5000);
        ApiResponse.setRespBody(null);
        ApiResponse.setTitle(title);
        ApiResponse.setMessage(message);
        return ApiResponse;
    }

    @JsonIgnore
    public static <T> ApiResponse<T> failure(T responseBody, String message) {
        ApiResponse<T> ApiResponse = new ApiResponse<>();
        ApiResponse.setRespCode(5000);
        ApiResponse.setRespBody(responseBody);
        ApiResponse.setMessage(message);
        return ApiResponse;
    }


    @JsonIgnore
    public static <T> ApiResponse<T> failure(String message, int errorCode) {
        ApiResponse<T> ApiResponse = new ApiResponse<>();
        ApiResponse.setRespCode(errorCode);
        ApiResponse.setMessage(message);
        return ApiResponse;
    }

    @JsonIgnore
    public static <T> ApiResponse<T> failure(String title, String message, int errorCode) {
        ApiResponse<T> ApiResponse = new ApiResponse<>();
        ApiResponse.setRespCode(errorCode);
        ApiResponse.setTitle(title);
        ApiResponse.setMessage(message);
        return ApiResponse;
    }


    @JsonIgnore
    public static <T> ApiResponse<T> notFound(String message) {
        ApiResponse<T> ApiResponse = new ApiResponse<>();
        ApiResponse.setRespCode(4004);
        ApiResponse.setRespBody(null);
        ApiResponse.setTitle("Record Not Found");
        ApiResponse.setMessage(message);
        return ApiResponse;
    }

    @JsonIgnore
    public static <T> ApiResponse<T> invalid(String message) {
        ApiResponse<T> ApiResponse = new ApiResponse<>();
        ApiResponse.setRespCode(4000);
        ApiResponse.setRespBody(null);
        ApiResponse.setTitle("Incorrect Information Provided");
        ApiResponse.setMessage(message);
        return ApiResponse;
    }





    @JsonIgnore
    public boolean hasFailed() {
        return !(isSuccess());
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.getRespCode() == 2000;
    }


    @Setter
    @Getter
    @Builder
    public static class AppAction {
        String code;
        String title;
        String narration;
        boolean isAutomatic;
        Object metaData;
    }


}
