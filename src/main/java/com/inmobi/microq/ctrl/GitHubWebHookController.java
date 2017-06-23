package com.inmobi.microq.ctrl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmobi.microq.dao.MSTSDaoException;
import com.inmobi.microq.models.MSTestCase;
import com.inmobi.microq.MSTestException;
import com.inmobi.microq.dao.Github;
import com.inmobi.microq.dao.MSTestCaseDAO;
import com.inmobi.microq.models.GithubStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * @author prathik.raj
 */
@RequiredArgsConstructor
@Slf4j
public class GitHubWebHookController {
    private final Github github;
    private final MSTestCaseDAO dao;

    public boolean managePullRequestHook(String webHookJson) throws IOException, MSTestException {
        log.debug(webHookJson);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(webHookJson);
        JsonNode pullRequest = jsonNode.get("pull_request");
        if(pullRequest != null) {
            /**
             * Logic for pull request
             */
            JsonNode action = jsonNode.get("action");
            if(action == null) {
                throw new MSTestException("Action not present");
            } else if(!action.textValue().equals("closed")) {
                if(pullRequest.get("statuses_url") == null) {
                    throw new MSTestException("Statuses URL not present");
                }

                if(pullRequest.get("number") == null) {
                    throw new MSTestException("Pull request number not present");
                } else {
                    Integer number = pullRequest.get("number").asInt();
                    if(number == 0) {
                        throw new MSTestException("Invalid Pull Request Number");
                    }
                }

                String statusUrl = pullRequest.get("statuses_url").asText();
                log.debug(statusUrl);
                github.pendingStatus(statusUrl);

                if(jsonNode.get("repository") == null) {
                    throw new MSTestException("Repo not present");
                } else if(jsonNode.get("repository").get("name") == null) {
                    throw new MSTestException("Repo name not present");
                }

                List<MSTestCase> msTestCases = null;

                String repo = jsonNode.get("repository").get("name").asText();
                log.debug(repo);

                try {
                    msTestCases = dao.getTestCasesForPullRequest(repo,
                            0);
                    log.debug(String.valueOf(msTestCases));
                } catch (MSTSDaoException e) {
                    GithubStatus githubStatus = new GithubStatus();
                    githubStatus.setState("failure");
                    githubStatus.setDescription(e.getMessage());
                    githubStatus.setTargetUrl("https://clarity.corp.inmobi.com");
                    githubStatus.setContext("mstest");
                    github.updateStatus(statusUrl, githubStatus);
                    throw new MSTestException(e);
                }

                if(msTestCases.size() == 0) {
                    GithubStatus githubStatus = new GithubStatus();
                    githubStatus.setState("failure");
                    githubStatus.setDescription("New test cases for this pull request not present");
                    githubStatus.setTargetUrl("http://dw1002.app.uh1.inmobi.com:8011/tc/" + repo + "/");
                    githubStatus.setContext("mstest");
                    github.updateStatus(statusUrl, githubStatus);
                    return false;
                } else {
                    for(MSTestCase msTestCase: msTestCases) {

                        msTestCase.setPullRequest(pullRequest.get("number").asInt());
                        try {
                            dao.updateTestCase(msTestCase);
                        } catch (MSTSDaoException e) {
                            GithubStatus githubStatus = new GithubStatus();
                            githubStatus.setState("failure");
                            githubStatus.setDescription(e.getMessage());
                            githubStatus.setTargetUrl("https://clarity.corp.inmobi.com");
                            githubStatus.setContext("mstest");
                            github.updateStatus(statusUrl, githubStatus);
                            throw new MSTestException(e);
                        }
                        log.debug("Set pull request as " + pullRequest.get("number").asText() + " for "
                                + msTestCase.toString());
                    }

                    GithubStatus githubStatus = new GithubStatus();
                    githubStatus.setState("success");
                    githubStatus.setDescription("New tests were detected for this pull request");
                    githubStatus.setTargetUrl("http://dw1002.app.uh1.inmobi.com:8011/tc/" + repo + "/");
                    githubStatus.setContext("mstest");
                    github.updateStatus(statusUrl, githubStatus);
                    return true;
                }
            } else {
                log.debug("Action close");
            }
        }
        return false;
    }
}
