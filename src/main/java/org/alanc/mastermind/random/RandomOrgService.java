package org.alanc.mastermind.random;

import okhttp3.*;
import java.io.IOException;
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
        logger.debug("Generating {} random numbers using Random.org api.", quantity);
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
            if (response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            System.out.println("Random.org API failed.");
        }
        return null;

    }
}
