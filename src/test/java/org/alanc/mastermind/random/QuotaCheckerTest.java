package org.alanc.mastermind.random;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

@DisplayName("QuotaChecker Tests")
class QuotaCheckerTest {

    private MockWebServer mockWebServer;
    private QuotaChecker service;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        
        String baseUrl = mockWebServer.url("/").toString();
        service = new QuotaChecker(baseUrl + "quota/");
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("Should handle successful quota check")
    void testSuccessfulQuotaCheck() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("9850"));

        int quota = service.getQuota();

        assertEquals(9850, quota);
    }

    @Test
    @DisplayName("Should handle API failures")
    void testApiFailure() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));

        int quota = service.getQuota();

        assertEquals(-1, quota); // Error indicator
    }

    @Test
    @DisplayName("Should handle invalid response format")
    void testInvalidResponseFormat() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("not-a-number"));

        int quota = service.getQuota();

        assertEquals(-1, quota); // Error indicator
    }
}