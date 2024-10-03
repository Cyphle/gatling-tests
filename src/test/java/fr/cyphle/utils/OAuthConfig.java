package fr.cyphle.utils;

public class OAuthConfig {
    public static final String BASE_URL = "https://platform.stonal-test.io";

    public static final String OIDC = "https://sso.stonal-test.io";
    public static final String AUTHORIZE_URL = OIDC + "/realms/stonal/protocol/openid-connect/auth";
    public static final String TOKEN_URL = OIDC + "/realms/stonal/protocol/openid-connect/token";

    public static final String PASSWORD_FLOW_CLIENT_ID = "powerbi-password-flow";
    public static final String PASSWORD_FLOW_CLIENT_SECRET = "D7gsFJbYgQ5cs8mqYi7J5YPTW8cSFIyD";
    public static final String CODE_FLOW_CLIENT_ID = "api-gateway";
    public static final String CODE_FLOW_CLIENT_SECRET = "D7gsFJbYgQ5cs8mqYi7J5YPTW8cSFIyD";
    public static final String SCOPE = "openid";

    public static final String USERNAME = "admin-plateforme@lafoncierenumerique.com";
    public static final String PASSWORD = "n2C.v947RN%_UlTYWLqKmX@GPj[yVu8h";
}
