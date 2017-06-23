package com.inmobi.microq.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmobi.microq.MSTestException;
import com.inmobi.microq.ctrl.GitHubWebHookController;
import com.inmobi.microq.dao.MSTSDaoException;
import com.inmobi.microq.dao.MSTestCaseDAO;
import com.inmobi.microq.dao.Github;
import com.inmobi.microq.models.MSTestCase;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * @author prathik.raj
 */

@Slf4j
@Path("/github/")
public class GithubHandler {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MSTSApiResponse handleGithubPush(String githubData) {
        log.debug(githubData);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(githubData);
            if(root.get("zen") != null) {
                MSTSApiResponse mstsApiResponse = new MSTSApiResponse();
                mstsApiResponse.setMessage("Zen received");
                mstsApiResponse.setCode(200);
                return mstsApiResponse;
            } else if(root.get("pull_request") != null) {
                MSTSApiResponse mstsApiResponse = new MSTSApiResponse();
                mstsApiResponse.setMessage("PR received");
                mstsApiResponse.setCode(200);
                log.debug("handle pr");
                OkHttpClient okHttpClient = new OkHttpClient();
                Github github = new Github(okHttpClient);
                MSTestCaseDAO msTestCaseDAO = new MSTestCaseDAO();
                GitHubWebHookController gitHubWebHookController = new GitHubWebHookController(github, msTestCaseDAO);
                gitHubWebHookController.managePullRequestHook(githubData);

                return mstsApiResponse;

            } else {
                log.debug("Following unhandled push received");
                log.debug(githubData);
                MSTSApiResponse mstsApiResponse = new MSTSApiResponse();
                mstsApiResponse.setMessage("Unhandled event");
                mstsApiResponse.setCode(200);
                return mstsApiResponse;
            }
        } catch (IOException | MSTestException e) {
            log.error(e.getMessage());
            MSTSApiResponse mstsApiResponse = new MSTSApiResponse();
            mstsApiResponse.setMessage("Unhandled event");
            mstsApiResponse.setCode(200);
            return mstsApiResponse;
        }
    }
}
