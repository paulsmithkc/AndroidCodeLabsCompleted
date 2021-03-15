package edu.ranken.prsmith.whowroteit.model;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Google Books API
 * https://developers.google.com/books/docs/v1/reference/
 * https://developers.google.com/books/docs/v1/reference/volumes/list
 * https://developers.google.com/books/docs/v1/reference/volumes#resource
 *
 * Example Query
 * https://www.googleapis.com/books/v1/volumes?q=pride+prejudice&maxResults=5
 *
 * Gson User Guide
 * https://github.com/google/gson/blob/master/UserGuide.md
 */
public class BookDataSource {
    private static final String LOG_TAG = "WhoWroteIt";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes";

    // Full-text search query string.
    private static final String PARAM_QUERY = "q";

    // Filter search results.
    // "ebooks" - All Google eBooks.
    // "free-ebooks" - Google eBook with full volume text viewability.
    // "paid-ebooks" - Google eBook with a price.
    // "partial" - Public able to see parts of text.
    // "full" - Public can view entire volume text.
    private static final String PARAM_FILTER = "filter";
    public enum Filter {
        ALL, EBOOKS, FREE_EBOOKS, PAID_EBOOKS, PARTIAL, FULL
    }

    // Sort search results. Acceptable values are:
    // "newest" - Most recently published.
    // "relevance" - Relevance to search terms.
    private static final String PARAM_ORDER_BY = "orderBy";
    public enum OrderBy {
        NEWEST, RELEVANCE
    }

    // Maximum number of results to return. Acceptable values are 0 to 40, inclusive.
    private static final String PARAM_MAX_RESULTS = "maxResults";

    // Index of the first result to return (starts at 0).
    private static final String PARAM_START_INDEX = "startIndex";

    public BookApiResponse searchBooks(String query, int maxResults) throws IOException {
        String uri =
            Uri.parse(BASE_URL)
            .buildUpon()
            .appendQueryParameter(PARAM_QUERY, query)
            .appendQueryParameter(PARAM_MAX_RESULTS, String.valueOf(maxResults))
            .toString();

        URL url = new URL(uri);
        HttpURLConnection conn = null;
        try {
            // configure connection
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // send request
            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.i(LOG_TAG, "RESPONSE " + responseCode + " GET " + uri);

            // read response
            try (InputStream stream = conn.getInputStream()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                    Gson gson = new Gson();
                    BookApiResponse response = gson.fromJson(reader, BookApiResponse.class);
                    return response;
                }
            }

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
