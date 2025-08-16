package org.alanc.mastermind.random;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

class QuotaCheckerTest {

    private MockWebServer mockWebServer;
    private QuotaChecker service;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // Create service with custom URLs pointing to our mock server
        String baseUrl = mockWebServer.url("/").toString();
        service = new QuotaChecker(baseUrl + "quota/");
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Nested
    @DisplayName("Quota Checking Tests")
    class QuotaTests {

        @Test
        @DisplayName("Should handle successful quota check")
        void testSuccessfulQuotaCheck() throws InterruptedException {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("9850"));

            int quota = service.getQuota();

            assertEquals(9850, quota);

            RecordedRequest request = mockWebServer.takeRequest();
            assertTrue(request.getPath().contains("getusage"));
            assertTrue(request.getPath().contains("format=plain"));
        }

        @Test
        @DisplayName("Should handle zero quota")
        void testZeroQuota() {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("0"));

            int quota = service.getQuota();

            assertEquals(0, quota);
        }

        @Test
        @DisplayName("Should handle quota check failure")
        void testQuotaCheckFailure() {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(500)
                    .setBody("Server Error"));

            int quota = service.getQuota();

            assertEquals(-1, quota);
        }

        @Test
        @DisplayName("Should handle quota check with invalid response")
        void testQuotaCheckInvalidResponse() {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("not-a-number"));

            int quota = service.getQuota();

            assertEquals(-1, quota);
        }

        @Test
        @DisplayName("Should handle quota check network error")
        void testQuotaCheckNetworkError() throws IOException {
            mockWebServer.shutdown();

            int quota = service.getQuota();

            assertEquals(-1, quota);
        }

        @Test
        @DisplayName("Should handle negative quota values")
        void testNegativeQuota() {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("-100"));

            int quota = service.getQuota();

            assertEquals(-100, quota);
        }
    }
}

