package fr.cyphle;

import io.gatling.javaapi.core.Simulation;

import static fr.cyphle.documentations.LoadDocumentationsTest.loadDocumentationScenario;
import static fr.cyphle.documentations.LoadDocumentationsTest.loadDocumentationsProtocol;
import static fr.cyphle.search.SearchDocumentsTest.searchDocumentsProtocol;
import static fr.cyphle.search.SearchDocumentsTest.searchDocumentsScenario;
import static io.gatling.javaapi.core.CoreDsl.*;

public class Simulations extends Simulation {
    {
        setUp(
            loadDocumentationScenario
                .injectOpen(atOnceUsers(1))
                .protocols(loadDocumentationsProtocol)
//            searchDocumentsScenario
//                .injectOpen(atOnceUsers(5))
//                .protocols(searchDocumentsProtocol)
        )
            .assertions(
                global().failedRequests().count().is(0L)
//                details("Search documents").responseTime().percentile4().lt(500)
            );
    }
}
