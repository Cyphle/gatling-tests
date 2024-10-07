package fr.cyphle.utils;

public class OAuthConfig {
    public static final String BASE_URL = "https://baseurl.com";

    public static final String OIDC = "https://openid.server.com";
    public static final String AUTHORIZE_URL = OIDC + "/realms/myrealm/protocol/openid-connect/auth";
    public static final String TOKEN_URL = OIDC + "/realms/myrealm/protocol/openid-connect/token";

    public static final String PASSWORD_FLOW_CLIENT_ID = "client_id_password_flow";
    public static final String PASSWORD_FLOW_CLIENT_SECRET = "client_secret_password_flow";
    public static final String CODE_FLOW_CLIENT_ID = "client_id_code_flow";
    public static final String CODE_FLOW_CLIENT_SECRET = "client_secret_code_flow";
    public static final String SCOPE = "openid";

    public static final String USERNAME = "johndoe@gatling.test";
    public static final String PASSWORD = "passpass";
}
