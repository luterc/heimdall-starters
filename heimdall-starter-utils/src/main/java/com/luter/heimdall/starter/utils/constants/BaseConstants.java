package com.luter.heimdall.starter.utils.constants;

public final class BaseConstants {
    public static final String CACHE_PERMISSION_KEY = "sys_permissions";
    public static final String HEADER_CURRENTUSER_PARAM = "user";
    public static final String HEADER_USER_ID_PARAM = "user_id";
    public static final String HEADER_USER_NAME_PARAM = "username";
    public static final String HEADER_CLIENTID_PARAM = "client_id";
    public static final String HEADER_TRACE_ID_PARAM = "trace_id";
    public static final String HEADER_AUTHORIZATION_NAME = "Authorization";
    public static final String QUERY_AUTHORIZATION_NAME = "access_token";
    public static final String HEADER_AUTHORIZATION_PREFIX = "Bearer ";
    public static final String BLACK_JWTTOKENS_REFIX = "black_jwt_tokens:";
    public static final String WHITE_JWTTOKENS_REFIX = "white_jwt_tokens:";
    public static final String CLIENT_DETAIL_CACHE_KEY = "oauth_client_details";

    public static final String REDIS_TOKEN_AUTH = "auth:";
    public static final String REDIS_CLIENT_ID_TO_ACCESS = "client_id_to_access:";
    public static final String REDIS_UNAME_TO_ACCESS = "uname_to_access:";
    public static final Integer DEFAULT_TOKEN_EXPIRE_TIME = 60 * 60;
    public static final String TOKEN_STORE_TYPE_JWT = "JWT";

    public static final String TOKEN_STORE_TYPE_REDIS = "REDIS";

    public static final String JWT_SECRET = "aabbccdd";
    public static final String JWT_PAYLOAD = "payload";
}
