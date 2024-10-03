package fr.cyphle.documentations;

import fr.cyphle.utils.OAuthConfig;
import io.gatling.http.response.ByteArrayResponseBody;
import io.gatling.http.response.Response;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.netty.handler.codec.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static fr.cyphle.utils.OAuthConfig.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class LoadDocumentationsTest {
    private static String code = "";

    public static HttpProtocolBuilder loadDocumentationsProtocol = http
        .baseUrl(OAuthConfig.BASE_URL)
        .acceptHeader("text/html,application/xhtml+xml,application/xml")
        .userAgentHeader("Mozilla/5.0");

    public static ScenarioBuilder loadDocumentationScenario = scenario("Load documentations")
        .exec(http("Request Token from authorization code flow")
            .get("https://sso.stonal-test.io/realms/stonal/protocol/openid-connect/auth")
            .queryParam("client_id", CODE_FLOW_CLIENT_ID)
            .queryParam("redirect_uri", BASE_URL + "/api/login/oauth2/code/keycloak")
            .queryParam("response_type", "code")
            .queryParam("scope", SCOPE)
            .check(status().is(200))
            .check(css("form#kc-form-login", "action").saveAs("loginAction"))
        )
        .pause(1)

        .exec(http("Submit login form")
                .post("#{loginAction}")
                .formParam("username", USERNAME)
                .formParam("password", PASSWORD)
                .transformResponse(((response, session) -> {
                    System.out.println("location: " + response.header("Location"));
                    var code = response
                        .header("Location")
                        .filter(location -> location.contains("code="))
                        .get()
                        .split("code=")[1].split("&")[0];
//                    session.set("authorizationCode", code.split("code=")[1].split("&")[0]);
//                    session.set("test", "Hello world");

                    System.out.println("code: " + code);
                    LoadDocumentationsTest.code = code;

//                    return response;
                    return new Response(
                        response.request(),
                        response.startTimestamp(),
                        response.endTimestamp(),
                        response.status(),
                        response.headers().add("Test", code),
                        new ByteArrayResponseBody("Hello world".getBytes(), StandardCharsets.UTF_8),
                        response.checksums(),
                        response.isHttp2()
                    );

                }))
                .check(bodyString().saveAs("body"))
                .check(header("Test").saveAs("test"))
//                .checkIf(((response, session) -> {
//                    var code = response
//                        .header("Location")
//                        .filter(location -> location.contains("code="))
//                        .get();
//                    System.out.println("Code: " + code);
//                    return response.status().code() == 200;
//                })).then(header("status"))
//            .check(status().is(200))
        )
        // Conditionally save or process the header

        .exec(session -> {
//            System.out.println("Status: " + session.get("loginStatusCode"));
            System.out.println("Body: " + session.get("body"));
//            System.out.println("Redirection: " + session.get("authorizationCode"));
            System.out.println("Test: " + session.get("test"));
            System.out.println("Static code:  " + LoadDocumentationsTest.code);
            return session;
        });
//
//
//        .exec(session -> {
//            System.out.println("Code: " + session.get("authCodeRedirectUrl"));
//            var authCode = Objects.requireNonNull(session.getString("authCodeRedirectUrl")).split("code=")[1].split("&")[0];
//            session.set("authorizationCode", authCode);
//            return session;
//        })
//        .pause(1)
//
//        .exec(
//            http("Token Request")
//                .post(TOKEN_URL)
//                .formParam("client_id", CLIENT_ID)
//                .formParam("grant_type", "authorization_code")
//                .formParam("code", "${authorizationCode}")
//                .formParam("redirect_uri", REDIRECT_URI)
//                .check(status().is(200)) // Expect successful token response
//                .check(jsonPath("$.access_token").saveAs("accessToken")) // Save the access token
//        )
//        .exec(session -> {
//            System.out.println("Access Token: " + session.getString("accessToken"));
//            return session;
//        });
}
