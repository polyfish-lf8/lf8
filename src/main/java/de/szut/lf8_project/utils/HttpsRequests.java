package de.szut.lf8_project.utils;

import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HttpsRequests {
    private final OkHttpClient client;

    public HttpsRequests() {
        client = new OkHttpClient.Builder()
                .connectionSpecs(List.of(ConnectionSpec.MODERN_TLS))
                .cache(new Cache(new File("http-cache"), 104857600))
                .build();
    }

    public Response makeRequest(Request request) throws IOException {
        return client.newCall(request).execute();
    }
}
