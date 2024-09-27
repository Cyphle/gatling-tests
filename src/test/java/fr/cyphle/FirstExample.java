package fr.cyphle;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class FirstExample extends Simulation {
    // Define the base URL for the application
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://platform.stonal-test.io")  // Replace with your app's base URL
            .inferHtmlResources()  // Fetch static resources (CSS, JS)
            .acceptHeader("application/json")
            .contentTypeHeader("application/x-www-form-urlencoded")
            .userAgentHeader("Mozilla/5.0");

    // Scenario that handles OAuth2 Authorization Code Flow
    ScenarioBuilder scn = scenario("OAuth2 Authorization Code Flow Scenario")
            // Step 1: Perform the initial request to the OAuth2 Authorization Server
            .exec(http("Request Authorization Code")
                    .get("https://sso.stonal-test.io/oauth/authorize")  // Replace with your OAuth2 authorization endpoint
                    .queryParam("response_type", "code")
                    .queryParam("client_id", "api-gateway")  // Replace with your client ID
                    .queryParam("redirect_uri", "https://platform.stonal-test.io/login")  // Your app's redirect URI
                    .queryParam("scope", "openid profile user")  // Requested scopes
                    .queryParam("state", "someStateValue")  // CSRF prevention
                    .check(status().is(302))  // Ensure redirect status
                    .check(header("Location").saveAs("authRedirectUrl"))  // Extract redirect URL
            )
            .pause(1)

            // Step 2: Handle the redirect and extract the authorization code from the redirect URL
            .exec(session -> {
                String authRedirectUrl = session.getString("authRedirectUrl");
                String authorizationCode = authRedirectUrl.substring(authRedirectUrl.indexOf("code=") + 5, authRedirectUrl.indexOf("&", authRedirectUrl.indexOf("code=")));
                return session.set("authorizationCode", authorizationCode);  // Store the authorization code
            })

            // Step 3: Exchange the authorization code for an access token
            .exec(http("Exchange Authorization Code for Token")
                    .post("https://auth-server.com/oauth/token")  // Replace with the token endpoint of your OAuth2 server
                    .formParam("grant_type", "authorization_code")
                    .formParam("client_id", "api-gateway")  // Replace with your client ID
                    .formParam("client_secret", "api-gateway-secret")  // Replace with your client secret
                    .formParam("redirect_uri", "https://your-app.com/callback")  // Must match the one used in Step 1
                    .formParam("code", "#{authorizationCode}")  // Authorization code from Step 2
                    .check(status().is(200))
                    .check(jsonPath("$.access_token").saveAs("accessToken"))  // Extract access token from response
            )
            .pause(1)

            // Step 4: Make an authenticated request with the access token
            .exec(http("Authenticated API Request with Access Token")
                            .get("/api/protected/resource")  // Replace with your protected resource URL
                            .header("Authorization", "Bearer #{accessToken}")  // Use the access token for authorization
                            .check(status().is(200))  // Ensure the request succeeds
                    // Step 5: No need to manually save cookies; Gatling manages cookies automatically
            )
            .pause(1)

            // Step 5: Use the session cookie in a subsequent request
            .exec(http("Subsequent Request with Session Cookie")
                    .get("/api/protected/resource")  // Replace with another protected resource
                    .header("Cookie", "SESSION=#{sessionCookie}")  // Use the extracted session cookie
                    .check(status().is(200))  // Ensure the request is successful
            );

    {
        // Define the load simulation
        setUp(
                scn.injectOpen(atOnceUsers(1))  // Start with 1 user for testing purposes
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