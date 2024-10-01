package fr.cyphle.documentations;

import fr.cyphle.utils.OAuthConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class LoadDocumentationsTest {
    public static HttpProtocolBuilder loadDocumentationsProtocol = http
        .baseUrl(OAuthConfig.BASE_URL)
        .acceptHeader("application/json")
        .userAgentHeader("Mozilla/5.0");

    public static ScenarioBuilder loadDocumentationScenario = scenario("Load documentations")
        .exec(http("Request Token from authorization code flow")
            .get(OAuthConfig.BASE_URL)
            .asFormUrlEncoded()
            .check(status().is(200))
            .check(header("Location").saveAs("redirection"))
        )
        .pause(1)

        .exec(session -> {
            System.out.printf("Redirection: " + session.getString("redirection"));
            return session;
        });

    /*
    III. Simuler un user
1. Aller sur platform.stonal-test.io
2. Gérer la redirection vers sso.stonal-test.io?...
3. Entrer login + password
4. Récupérer le cookie de session
5. Appeler les endpoints que le front appel
     */
}
