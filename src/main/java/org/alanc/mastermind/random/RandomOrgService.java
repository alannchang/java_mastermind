package org.alanc.mastermind.random;

import okhttp3.*;
import java.io.IOException;

import org.alanc.mastermind.util.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomOrgService implements RandomNumberService {
    private static final Logger logger = LoggerFactory.getLogger(RandomOrgService.class);

    private static final String RANDOM_ORG_API = "https://www.random.org/integers";
    private static final OkHttpClient client = new OkHttpClient();

    private final String randomNumberApi;

    public RandomOrgService() {
        this(RANDOM_ORG_API);
    }

    // for custom API urls if that ever happens
    public RandomOrgService(String randomNumberApi) {
        this.randomNumberApi = randomNumberApi;
    }

    @Override
    public String generate(int quantity, int min, int max) {
        logger.debug("Generating {} random numbers from {} to {} using random.org api.", quantity, min, max);

        if (quantity < 1 || quantity > 10000) {
            throw new IllegalArgumentException("Quantity must be between 1 and 10000");
        }
        if (min < -1_000_000_000 || min > 1_000_000_000) {
            throw new IllegalArgumentException("Min value must be between -1,000,000,000 and 1,000,000,000, got: " + min);
        }
        if (max < -1_000_000_000 || max > 1_000_000_000) {
            throw new IllegalArgumentException("Max value must be between -1,000,000,000 and 1,000,000,000, got: " + max);
        }
        if (min > max) {
            throw new IllegalArgumentException("Min value (" + min + ") cannot be greater than max value (" + max + ")");
        }

        HttpUrl baseUrl = HttpUrl.parse(randomNumberApi);

        HttpUrl url = baseUrl.newBuilder()
                .addQueryParameter("num", String.valueOf(quantity))
                .addQueryParameter("min", String.valueOf(min))
                .addQueryParameter("max", String.valueOf(max))
                .addQueryParameter("col", "1")
                .addQueryParameter("base", "10")
                .addQueryParameter("format", "plain")
                .addQueryParameter("rnd", "new")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (!response.isSuccessful() || responseBody == null) {
                logger.warn("Error encountered when generating numbers using random.org api. Response code {}", response.code());
                return null;
            }
            String responseString = responseBody.string();
            String formattedRandomNumbers = responseString
                    .replaceAll("[\\r\\n]+", " ")  // Replace any line endings with space
                    .replaceAll("\\s+", " ")       // Collapse multiple whitespace to single space
                    .trim();
            logger.debug("Successfully generated {} random numbers using random.org api: {}", quantity, formattedRandomNumbers);
            return formattedRandomNumbers;
        } catch (IOException e) {
            ErrorHandler.handleNetworkError(logger, "Random.org API", e, true);
        }
        return null;
    }

    public static void shutdown() {
        logger.debug("Shutting down RandomOrgService HTTP client");
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
    }
}
