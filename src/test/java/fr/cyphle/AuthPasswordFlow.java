package fr.cyphle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.Base64;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class AuthPasswordFlow extends Simulation {
    private Deserializer deserializer = Deserializer.get();

    HttpProtocolBuilder httpProtocol = http
        .baseUrl(Config.BASE_URL)
        .acceptHeader("application/json")
        .contentTypeHeader("application/x-www-form-urlencoded")
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
            .transformResponse((response, session) -> {
                var accessToken = deserializer.readValue(response.body().string(), AccessToken.class);
                System.out.println(accessToken);
                return response;
            })
        )
        .pause(1);

    {
        // Define the load simulation
        setUp(
            scenario.injectOpen(atOnceUsers(1))  // Start with 1 user for testing purposes
        ).protocols(httpProtocol);
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