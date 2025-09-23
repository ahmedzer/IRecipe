package com.za.irecipe.services;

import static com.za.irecipe.services.ServicesExtKt.generateContentBlocking;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.za.irecipe.Data.remote.model.GeminiRequest;
import com.za.irecipe.Data.remote.model.GeminiResponse;
import com.za.irecipe.Data.remote.model.GenerationConfig;
import com.za.irecipe.Data.remote.services.GeminiApiService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class GeminiServiceTest {
    private MockWebServer mockWebServer;
    private GeminiApiService geminiApiService;


    @Before
    public void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        geminiApiService = retrofit.create(GeminiApiService.class);
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testGenerateContentReturnsExpectedResponse() throws Exception {
        String fakeResponseJson = "{\n" +
                "  \"candidates\": [\n" +
                "    { \"content\": { \"parts\": [{ \"text\": \"Hello Gemini!\" }] } }\n" +
                "  ]\n" +
                "}";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(fakeResponseJson)
        );

        GeminiRequest request = new GeminiRequest(Collections.emptyList(), new GenerationConfig(0.5));
        GeminiResponse response = generateContentBlocking(
                geminiApiService,
                "FAKE_KEY",
                request
        );

        assertNotNull(response);
        assertFalse(response.getCandidates().isEmpty());
        assertEquals("Hello Gemini!",
                response.getCandidates().get(0).getContent().getParts().get(0).getText());
    }
}
