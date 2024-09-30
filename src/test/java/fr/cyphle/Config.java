package fr.cyphle;

public class Config {
    public static final String BASE_URL = "https://platform.stonal-test.io";
    public static final String REDIRECT_URI = "https://platform.stonal-test.io";
    public static final String CLIENT_ID = "powerbi-password-flow";
    public static final String CLIENT_SECRET = "D7gsFJbYgQ5cs8mqYi7J5YPTW8cSFIyD";
    public static final String SCOPE = "openid profile user";
    public static final String OIDC = "https://sso.stonal-test.io";
    public static final String AUTHORIZE_URL = OIDC + "/realms/stonal/protocol/openid-connect/oauth/authorize";
    public static final String TOKEN_URL = OIDC + "/realms/stonal/protocol/openid-connect/token";
    public static final String USERNAME = "admin-plateforme@lafoncierenumerique.com";
    public static final String PASSWORD = "Maradonna@1986";
}
