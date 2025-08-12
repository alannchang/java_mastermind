package org.alanc.mastermind.random;

import okhttp3.*;
import java.io.IOException;

public class RandomOrgService implements RandomNumberService {
    private static final String RANDOM_ORG_API = "https://www.random.org/integers";
    private static final String QUOTA_API = "https://www.random.org/quota/";
    private static final OkHttpClient client = new OkHttpClient();

    private final String randomNumberApi;
    private final String quotaApi;

    public RandomOrgService() {
        this(RANDOM_ORG_API, QUOTA_API);
    }

    public RandomOrgService(String randomNumberApi, String quotaApi) {
        this.randomNumberApi = randomNumberApi;
        this.quotaApi = quotaApi;
    }

    @Override
    public String generate(int quantity, int min, int max) {
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
