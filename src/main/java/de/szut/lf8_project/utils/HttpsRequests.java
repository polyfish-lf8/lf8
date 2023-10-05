package de.szut.lf8_project.utils;

import okhttp3.*;

import java.io.IOException;
import java.util.List;

public class HttpsRequests {
    private final OkHttpClient client;

    public HttpsRequests() {
        client = new OkHttpClient.Builder()
                .connectionSpecs(List.of(ConnectionSpec.MODERN_TLS))
                .build();
    }

    public Response makeRequest(Request request) throws IOException {
        return client.newCall(request).execute();
    }
}
