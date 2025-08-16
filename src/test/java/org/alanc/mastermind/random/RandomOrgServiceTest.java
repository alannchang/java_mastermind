package org.alanc.mastermind.random;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

class RandomOrgServiceTest {

    private MockWebServer mockWebServer;
    private RandomOrgService service;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // Create service with custom URLs pointing to our mock server
        String baseUrl = mockWebServer.url("/").toString();
        service = new RandomOrgService(baseUrl + "integers/");
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Nested
    @DisplayName("Successful API Response Tests")
    class SuccessfulResponseTests {

        @Test
        @DisplayName("Should handle successful random number generation")
        void testSuccessfulGeneration() throws InterruptedException {
            // Mock a successful response
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("5\n2\n8\n1\n"));

            String result = service.generate(4, 0, 8);

            assertNotNull(result);
            assertEquals("5 2 8 1", result);

            // Verify the request was made correctly
            RecordedRequest request = mockWebServer.takeRequest();
            assertEquals("GET", request.getMethod());
            assertTrue(request.getPath().contains("num=4"));
            assertTrue(request.getPath().contains("min=0"));
            assertTrue(request.getPath().contains("max=8"));
        }

        @Test
        @DisplayName("Should handle single number generation")
        void testSingleNumber() throws InterruptedException {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("7\n"));

            String result = service.generate(1, 5, 10);

            assertNotNull(result);
            assertEquals("7", result);

            RecordedRequest request = mockWebServer.takeRequest();
            assertTrue(request.getPath().contains("num=1"));
            assertTrue(request.getPath().contains("min=5"));
            assertTrue(request.getPath().contains("max=10"));
        }

        @Test
        @DisplayName("Should handle response with extra whitespace")
        void testResponseWithWhitespace() {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("  3  \n  1  \n  4  \n"));

            String result = service.generate(3, 0, 5);

            assertNotNull(result);
            assertEquals("3 1 4", result);
        }

        @Test
        @DisplayName("Should handle Windows line endings")
        void testWindowsLineEndings() {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("2\r\n6\r\n8\r\n"));

            String result = service.generate(3, 0, 9);

            assertNotNull(result);
            assertEquals("2 6 8", result);
        }
    }

    @Nested
    @DisplayName("API Failure Tests")
    class FailureTests {

        @Test
        @DisplayName("Should handle HTTP error responses")
        void testHttpError() {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(500)
                    .setBody("Internal Server Error"));

            String result = service.generate(4, 0, 8);

            assertNull(result);
        }

        @Test
        @DisplayName("Should handle quota exceeded")
        void testQuotaExceeded() {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(403)
                    .setBody("Quota exceeded"));

            String result = service.generate(4, 0, 8);

            assertNull(result);
        }

        @Test
        @DisplayName("Should handle rate limiting")
        void testRateLimiting() {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(429)
                    .setBody("Too Many Requests"));

            String result = service.generate(4, 0, 8);

            assertNull(result);
        }

        @Test
        @DisplayName("Should handle empty response body")
        void testEmptyResponse() {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody(""));

            String result = service.generate(4, 0, 8);

            assertNotNull(result);
            assertEquals("", result);
        }

        @Test
        @DisplayName("Should handle malformed response")
        void testMalformedResponse() {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("not-numbers\ninvalid\nresponse\n"));

            String result = service.generate(3, 0, 8);

            assertNotNull(result);
            assertEquals("not-numbers invalid response", result);
        }
    }

    @Nested
    @DisplayName("Network Error Tests")
    class NetworkErrorTests {

        @Test
        @DisplayName("Should handle connection timeout")
        void testConnectionTimeout() throws IOException {
            // Shutdown the server to simulate connection failure
            mockWebServer.shutdown();

            String result = service.generate(4, 0, 8);

            assertNull(result);
        }

        @Test
        @DisplayName("Should handle slow responses")
        void testSlowResponse() {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("1\n2\n3\n4\n")
                    .setBodyDelay(100, java.util.concurrent.TimeUnit.MILLISECONDS));

            String result = service.generate(4, 0, 8);

            // Should still work, just slower
            assertNotNull(result);
            assertEquals("1 2 3 4", result);
        }
    }

    @Nested
    @DisplayName("URL Construction Tests")
    class UrlConstructionTests {

        @Test
        @DisplayName("Should construct correct URL for generation")
        void testGenerationUrlConstruction() throws InterruptedException {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("1\n2\n3\n"));

            service.generate(3, 5, 15);

            RecordedRequest request = mockWebServer.takeRequest();
            String path = request.getPath();

            assertTrue(path.contains("integers/"));
            assertTrue(path.contains("num=3"));
            assertTrue(path.contains("min=5"));
            assertTrue(path.contains("max=15"));
            assertTrue(path.contains("col=1"));
            assertTrue(path.contains("base=10"));
            assertTrue(path.contains("format=plain"));
            assertTrue(path.contains("rnd=new"));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Integration Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle large number generation")
        void testLargeNumberGeneration() throws InterruptedException {
            StringBuilder response = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                response.append(i % 10).append("\n");
            }

            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody(response.toString()));

            String result = service.generate(100, 0, 9);

            assertNotNull(result);
            String[] numbers = result.split(" ");
            assertEquals(100, numbers.length);

            RecordedRequest request = mockWebServer.takeRequest();
            assertTrue(request.getPath().contains("num=100"));
        }

        @Test
        @DisplayName("Should handle response with mixed line endings")
        void testMixedLineEndings() {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("1\n2\r\n3\r4\n"));

            String result = service.generate(4, 0, 5);

            assertNotNull(result);
            assertEquals("1 2 3 4", result);
        }

        @Test
        @DisplayName("Should handle boundary values correctly")
        void testBoundaryValues() throws InterruptedException {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("0\n999\n"));

            String result = service.generate(2, 0, 999);

            assertNotNull(result);
            assertEquals("0 999", result);

            RecordedRequest request = mockWebServer.takeRequest();
            assertTrue(request.getPath().contains("min=0"));
            assertTrue(request.getPath().contains("max=999"));
        }

        @Test
        @DisplayName("Should handle consecutive API calls")
        void testConsecutiveCalls() {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("1\n2\n"));
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("3\n4\n"));

            String result1 = service.generate(2, 0, 5);
            String result2 = service.generate(2, 0, 5);

            assertNotNull(result1);
            assertNotNull(result2);
            assertEquals("1 2", result1);
            assertEquals("3 4", result2);
        }
    }
}
