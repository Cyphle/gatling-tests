package fr.cyphle.documentations;

import fr.cyphle.utils.OAuthConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.Objects;

import static fr.cyphle.utils.OAuthConfig.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class LoadDocumentationsTest {
    public static HttpProtocolBuilder loadDocumentationsProtocol = http
        .baseUrl(OAuthConfig.BASE_URL)
        .acceptHeader("text/html,application/xhtml+xml,application/xml")
        .userAgentHeader("Mozilla/5.0");

    public static ScenarioBuilder loadDocumentationScenario = scenario("Load documentations")
        .exec(http("Request Token from authorization code flow")
            .get(AUTHORIZE_URL)
            .asFormUrlEncoded()
            .queryParam("client_id", CLIENT_ID)
            .queryParam("redirect_uri", REDIRECT_URI)
            .queryParam("response_type", "code")
            .queryParam("scope", SCOPE)
            .check(status().is(302))
            .check(header("Location").saveAs("authRedirectUrl"))
        )
        .pause(1)

        .exec(
            http("Redirect to login/consent page")
                .get("${authRedirectUrl}")
                .check(status().is(200)) // Expect the login or consent form
        )
        .pause(2)

        .exec(session -> {
            System.out.println("Login page: " + session.get("authRedirectUrl"));
            return session;
        })

        .exec(
            http("Submit login form")
                .post("${authRedirectUrl}")
                .formParam("username", USERNAME)
                .formParam("password", PASSWORD)
                .check(status().is(302))
                .check(header("Location").saveAs("authCodeRedirectUrl"))
        )

        .exec(session -> {
            System.out.println("Code: " + session.get("authCodeRedirectUrl"));
            var authCode = Objects.requireNonNull(session.getString("authCodeRedirectUrl")).split("code=")[1].split("&")[0];
            session.set("authorizationCode", authCode);
            return session;
        })
        .pause(1)

        .exec(
            http("Token Request")
                .post(TOKEN_URL)
                .formParam("client_id", CLIENT_ID)
                .formParam("grant_type", "authorization_code")
                .formParam("code", "${authorizationCode}")
                .formParam("redirect_uri", REDIRECT_URI)
                .check(status().is(200)) // Expect successful token response
                .check(jsonPath("$.access_token").saveAs("accessToken")) // Save the access token
        )
        .exec(session -> {
            System.out.println("Access Token: " + session.getString("accessToken"));
            return session;
        });
}
