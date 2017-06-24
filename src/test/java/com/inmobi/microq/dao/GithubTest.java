package com.inmobi.microq.dao;

import com.inmobi.microq.scm.github.models.GithubStatus;
import com.inmobi.microq.scm.github.dao.Github;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * @author prathik.raj
 */
public class GithubTest {

    @Test
    void updateStatusTest() throws IOException {
        OkHttpClient client = mock(OkHttpClient.class);
        Call call = mock(Call.class);
        doReturn(call).when(client).newCall(any());

        GithubStatus githubStatus = new GithubStatus();
        githubStatus.setContext("mstest");
        githubStatus.setDescription("Works!");
        githubStatus.setTargetUrl("http://www.thiscoder.rocks");
        githubStatus.setState("success");

        Github github = new Github(client);
        github.updateStatus("https://github.corp.inmobi.com/api/v3/repos/" +
                        "prathik-raj/clarity-client/statuses/30b5d06f2ee32cd7e11d9ed1c6508c6113334a6e",
                githubStatus);

        github.pendingStatus("https://github.corp.inmobi.com/api/v3/repos/" +
                        "prathik-raj/clarity-client/statuses/30b5d06f2ee32cd7e11d9ed1c6508c6113334a6e");

    }
}
