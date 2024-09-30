package fr.cyphle;

import io.gatling.javaapi.core.Session;

import java.util.function.Function;

final class Helpers {
    public static final Function<Session, String> oauthPasswordFlowBodyBuilder = session -> {
        return "{ \"grant_type\": \"password\", \"username\": \"" + Config.USERNAME + "\", \"password\": \"" + Config.PASSWORD + "\" }";
    };
}
