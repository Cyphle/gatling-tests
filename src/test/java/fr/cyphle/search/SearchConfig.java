package fr.cyphle.search;

import java.util.List;

public class SearchConfig {
    public static final String BASE_URL = "https://api.stonal-test.io";
    public static final String SEARCH_URL = BASE_URL + "/document-storage/organizations/UNOFI/documents/search";

    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 100;
    public static final String SORT_ORDER = "ASC";
    public static final String COLUMN_TO_SORT = "identifier";

    public static final List<String> DOCUMENTATION_IDENTIFIERS = List.of("303ccee5-a5be-445d-99e0-9235c623979b");
}
