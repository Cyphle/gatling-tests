package fr.cyphle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.Base64;
import java.util.Objects;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.bodyString;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class AuthPasswordFlow extends Simulation {
    private Deserializer deserializer = Deserializer.get();

    HttpProtocolBuilder httpProtocol = http
        .baseUrl(Config.BASE_URL)
        .acceptHeader("application/json")
//        .contentTypeHeader("application/x-www-form-urlencoded")
        .userAgentHeader("Mozilla/5.0");

    ScenarioBuilder scenario = scenario("OAuth2 Password Flow Scenario")
        .exec(http("Request Token from password flow")
            .post(Config.TOKEN_URL)
            .asFormUrlEncoded()
            .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((Config.CLIENT_ID + ":" + Config.CLIENT_SECRET).getBytes()))
            .formParam("grant_type", "password")
            .formParam("username", Config.USERNAME)
            .formParam("password", Config.PASSWORD)
            .check(status().is(200))
            .check(jsonPath("$.access_token").saveAs("accessToken"))
        )
        .pause(1)

        .exec(http("Search documents")
            .post("https://api.stonal-test.io/document-storage/organizations/UNOFI/documents/search")
            .header("Authorization", "Bearer #{accessToken}")
            .header("Content-type", "application/json")
            .queryParam("pageNumber", 0)
            .queryParam("pageSize", 100)
            .queryParam("sortOrder", "ASC")
            .queryParam("columnToSort", "identifier")
            .body(StringBody("{\"documentationIdentifiers\": [\"303ccee5-a5be-445d-99e0-9235c623979b\"]}"))
            .check(status().is(200))
            .check(bodyString().saveAs("responseBody"))
        )
        .pause(1)

        .exec(session -> {
            String responseBody = session.getString("responseBody");
            System.out.println("Response Body: " + responseBody);  // Print the response to console
            return session;
        });

    {
        setUp(scenario.injectOpen(atOnceUsers(1)))
            .protocols(httpProtocol);
    }
}

/*
I. Doc storage expose un ingress
-> on peut l'appeler directement avec un header Authorizaiton Bearer xxx
1. exécuter le flow password OAuth2 et récupérer l'access token

II. Remplacer API gateway
1. exécuter le flow authorization code OAuth2 et récupérer l'access token
2. appeler doc storage directement

III. Simuler un user
1. Aller sur platform.stonal-test.io
2. Gérer la redirection vers sso.stonal-test.io?...
3. Entrer login + password
4. Récupérer le cookie de session
5. Appeler les endpoints que le front appel
 */