package org.alanc.mastermind.random;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

@DisplayName("RandomOrgService Tests")
class RandomOrgServiceTest {

    private MockWebServer mockWebServer;
    private RandomOrgService service;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        
        String baseUrl = mockWebServer.url("/").toString();
        service = new RandomOrgService(baseUrl + "integers/");
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Nested
    @DisplayName("HTTP Integration")
    class HttpIntegrationTests {

        @Test
        @DisplayName("Should handle successful API response")
        void testSuccessfulGeneration() throws InterruptedException {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("5\n2\n8\n1\n"));

            String result = service.generate(4, 0, 8);

            assertEquals("5 2 8 1", result);
            
            // Verify correct request parameters
            RecordedRequest request = mockWebServer.takeRequest();
            assertTrue(request.getPath().contains("num=4"));
            assertTrue(request.getPath().contains("min=0"));
            assertTrue(request.getPath().contains("max=8"));
        }

        @Test
        @DisplayName("Should handle API failures gracefully")
        void testApiFailure() {
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(500));

            String result = service.generate(4, 0, 8);

            assertNull(result); // Should return null for fallback handling
        }

        @Test
        @DisplayName("Should handle network timeouts")
        void testNetworkTimeout() {
            // Don't enqueue any response to simulate timeout
            
            String result = service.generate(4, 0, 8);
            
            assertNull(result); // Should return null for fallback handling
        }
    }

    @Nested
    @DisplayName("Resource Management")
    class ResourceManagementTests {

        @Test
        @DisplayName("Should close HTTP client resources")
        void testResourceClosure() {
            assertDoesNotThrow(() -> service.close());
        }
    }
}