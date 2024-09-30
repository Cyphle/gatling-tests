package fr.cyphle.search;

import fr.cyphle.utils.OAuthConfig;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.Base64;

import static fr.cyphle.utils.Helpers.collectionToJsonListParam;
import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.details;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class SearchDocumentsTest extends Simulation {
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

        .exec(http("Search documents")
            .post(SearchConfig.SEARCH_URL)
            .header("Authorization", "Bearer #{accessToken}")
            .header("Content-type", "application/json")
            .queryParam("pageNumber", SearchConfig.PAGE_NUMBER)
            .queryParam("pageSize", SearchConfig.PAGE_SIZE)
            .queryParam("sortOrder", SearchConfig.SORT_ORDER)
            .queryParam("columnToSort", SearchConfig.COLUMN_TO_SORT)
            .body(StringBody("{\"documentationIdentifiers\": " + collectionToJsonListParam(SearchConfig.DOCUMENTATION_IDENTIFIERS) + "}"))
            .check(status().is(200))
            .check(jsonPath("$.result").saveAs("responseBody"))
        )
        .pause(1)

        .exec(session -> {
            // Use saved body
            String responseBody = session.getString("responseBody");
            return session;
        });

    {
        setUp(scenario.injectOpen(
            atOnceUsers(5)
        ))
            .protocols(httpProtocol)
            .assertions(
                global().failedRequests().count().is(0L),
                details("Search documents").responseTime().percentile4().lt(500)
            );
    }
}