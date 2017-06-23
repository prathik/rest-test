package com.inmobi.microq.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmobi.microq.models.GithubStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;


/**
 * @author prathik.raj
 */
@RequiredArgsConstructor
@Slf4j
public class Github {
    final OkHttpClient client;

    public void pendingStatus(@NonNull String url) throws IOException {
        GithubStatus githubStatus = new GithubStatus();
        githubStatus.setState("pending");
        githubStatus.setDescription("Running tests for this service now");
        githubStatus.setTargetUrl("https://clarity.corp.inmobi.com");
        githubStatus.setContext("mstest");
        updateStatus(url, githubStatus);
    }

    public void updateStatus(@NonNull String url, @NonNull GithubStatus githubStatus) throws IOException {

        log.debug("updating status to github");
        log.debug("status url: " + url);
        log.debug(githubStatus.toString());

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(githubStatus);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonString);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Basic cHJhdGhpay5yYWo6Y2QzOTQ5YTFmYTM4MjdiNzc3MTE1ZTM3ZGQ1MWFiYmNlMzFjOGE1OQ==")
                .build();

        Call call = client.newCall(request);
        call.execute();
    }
}
