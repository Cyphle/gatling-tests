package fr.cyphle.documentations;

import fr.cyphle.utils.OAuthConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.Base64;

import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class LoadDocumentationsTest extends Simulation {
    HttpProtocolBuilder httpProtocol = http
        .baseUrl(OAuthConfig.BASE_URL)
        .acceptHeader("application/json")
        .userAgentHeader("Mozilla/5.0");

    ScenarioBuilder scenario = scenario("Search documents")
        .exec(http("Request Token from password flow")
            .post(OAuthConfig.TOKEN_URL)
            .asFormUrlEncoded()
            .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((OAuthConfig.CLIENT_ID + ":" + OAuthConfig.CLIENT_SECRET).getBytes()))
            .formParam("grant_type", "password")
            .formParam("username", OAuthConfig.USERNAME)
            .formParam("password", OAuthConfig.PASSWORD)
            .check(status().is(200))
            .check(jsonPath("$.access_token").saveAs("accessToken"))
        )
        .pause(1)
    /*
    III. Simuler un user
1. Aller sur platform.stonal-test.io
2. Gérer la redirection vers sso.stonal-test.io?...
3. Entrer login + password
4. Récupérer le cookie de session
5. Appeler les endpoints que le front appel
     */
}
