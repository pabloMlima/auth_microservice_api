package com.hubsi.authmicroservice.infrastructure.constants;

public class RoutesConstants {

    private RoutesConstants() {
    }

    public static final String API_V1 = "/api/v1";
    public static final String USER = "/user";
    public static final String AUTH = API_V1 + "/auth/login";
    public static final String SWAGGER_UI = "/swagger-ui/**";
    public static final String SWAGGER_UI_HTML = "/swagger-ui.html";
    public static final String SWAGGER_API_DOCS_V3 = "/v3/api-docs/**";
    public static final String SWAGGER_WEBJARS = "/webjars/**";
    public static final String ACTUATOR = "/actuator/**";
    public static final String SWAGGER_API_DOCS = "/api-docs/**";
    public static final String USER_REGISTER = API_V1+"/user/register";
    public static final String USER_RESET_PASSWORD = API_V1 + "/user/reset-password";
    public static final String USER_UPDATE_PASSWORD = "/update-password";
    public static final String USER_UPDATE_PASSWORD_SECURITY = API_V1 + USER + USER_UPDATE_PASSWORD;
}
