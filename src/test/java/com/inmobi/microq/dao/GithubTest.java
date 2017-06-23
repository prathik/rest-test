package com.inmobi.microq.dao;

import com.inmobi.microq.models.GithubStatus;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * @author prathik.raj
 */
@Test
public class GithubTest {
    @Test(enabled = false)
    void updateStatusTest() throws IOException {
        GithubStatus githubStatus = new GithubStatus();
        githubStatus.setContext("mstest");
        githubStatus.setDescription("Works!");
        githubStatus.setTargetUrl("http://www.thiscoder.rocks");
        githubStatus.setState("success");
        Github github = new Github();
        github.updateStatus("https://github.corp.inmobi.com/api/v3/repos/" +
                        "prathik-raj/clarity-client/statuses/30b5d06f2ee32cd7e11d9ed1c6508c6113334a6e",
                githubStatus);

    }
}
