package com.javalab.tutorial.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgrammaticallyManagedServerTest {

    private static final String WIRE_MOCK_SERVER_PATH = "/wiremock";
    private WireMockServer server = new WireMockServer();
    private CloseableHttpClient httpClient = HttpClients.createDefault();

    @Test
    public void givenProgrammaticallyManagedServer_whenUsingSimpleStubbing_thenCorrect() throws IOException {
        server.start();
        configureFor("localhost", 8080);
        stubFor(get(urlEqualTo(WIRE_MOCK_SERVER_PATH)).willReturn(aResponse().withBody("Wiremock server!")));

        HttpGet request = new HttpGet("http://localhost:8080/wiremock");
        HttpResponse httpResponse = httpClient.execute(request);
        String response = convertResponseToString(httpResponse);
        verify(getRequestedFor(urlEqualTo(WIRE_MOCK_SERVER_PATH)));
        assertEquals(response, "Wiremock server!");
        server.stop();
    }

    private static String convertResponseToString(HttpResponse httpResponse) throws IOException {
        InputStream responseStream = httpResponse.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String response = scanner.useDelimiter("\\Z").next();
        return response;
    }
}
