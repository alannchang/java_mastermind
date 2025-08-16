package org.alanc.mastermind.random;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class QuotaChecker {
    private static final Logger logger = LoggerFactory.getLogger(QuotaChecker.class);

    private static final String QUOTA_API = "https://www.random.org/quota/";
    private static final OkHttpClient client = new OkHttpClient();

    private final String quotaApi;

    public QuotaChecker() {
        this(QUOTA_API);
    }

    public QuotaChecker(String quotaApi) {
        this.quotaApi = quotaApi;
    }


    public int getQuota() {
        logger.debug("Checking random.org API quota");
        HttpUrl baseUrl = HttpUrl.parse(quotaApi);
        if (baseUrl == null) throw new IllegalStateException("Invalid quotaApi URL: " + quotaApi);

        HttpUrl url = baseUrl.newBuilder()
                .addQueryParameter("getusage", "true")
                .addQueryParameter("format", "plain")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(new Request.Builder().url(url).build()).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                logger.warn("Error encountered when retrieving quota from random.org. Response code: {}", response.code());
                return -1;
            }
            int quota = Integer.parseInt(response.body().string().trim());
            logger.debug("Retrieved current random.org quota: {} bits", quota);
            return quota;
        } catch (IOException e) {
            // TODO: replace with proper error handling
            System.err.println("Random.org quota API failed.");
            return -1;
        } catch (NumberFormatException e) {
            // TODO: replace with proper error handling
            System.err.println("Invalid response format received.");
            return -1;
        }
    }
}