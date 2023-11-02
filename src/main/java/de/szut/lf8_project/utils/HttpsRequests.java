package de.szut.lf8_project.utils;

import de.szut.lf8_project.Lf8ProjectApplication;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HttpsRequests {
    private final OkHttpClient client;

    public HttpsRequests() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectionSpecs(List.of(ConnectionSpec.MODERN_TLS));

        if(!Lf8ProjectApplication.DISABLE_CACHE)
            builder.cache(new Cache(new File("http-cache"), 104857600));

        client = builder.build();
    }

    public Response makeRequest(Request request) throws IOException {
        return client.newCall(request).execute();
    }
}
