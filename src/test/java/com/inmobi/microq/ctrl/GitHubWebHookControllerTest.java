package com.inmobi.microq.ctrl;

import com.inmobi.microq.models.MSTestCase;
import com.inmobi.microq.MSTestException;
import com.inmobi.microq.dao.Github;
import com.inmobi.microq.dao.MSTSDaoException;
import com.inmobi.microq.dao.MSTestCaseDAO;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author prathik.raj
 */
@Test
public class GitHubWebHookControllerTest {

    @Test
    void updateSuccessStatusTest() throws IOException, MSTestException, MSTSDaoException {
        URL resource = getClass().getClassLoader().getResource("pullRequestOpenedEvent.json");
        byte[] encoded = Files.readAllBytes(Paths.get(resource.getPath()));
        String json = new String(encoded);

        Github github = mock(Github.class);

        MSTestCaseDAO msTestCaseDAO = mock(MSTestCaseDAO.class);
        MSTestCase msTestCase = new MSTestCase();
        msTestCase.setPullRequest(null);
        msTestCase.setService("public-repo");
        List<MSTestCase> msTestCases = new ArrayList<>();
        msTestCases.add(msTestCase);
        doReturn(msTestCases).when(msTestCaseDAO).getTestCasesForPullRequest(
                "public-repo", 0);
        doNothing().when(msTestCaseDAO).updateTestCase(any());

        GitHubWebHookController gitHubWebHookController = new GitHubWebHookController(github, msTestCaseDAO);
        Assert.assertTrue(gitHubWebHookController.managePullRequestHook(json));
        Assert.assertEquals(msTestCase.getPullRequest(), Integer.valueOf(1));
    }

    @Test
    void noTestCasesTest() throws MSTSDaoException, IOException, MSTestException {
        URL resource = getClass().getClassLoader().getResource("pullRequestOpenedEvent.json");
        byte[] encoded = Files.readAllBytes(Paths.get(resource.getPath()));
        String json = new String(encoded);

        Github github = mock(Github.class);

        MSTestCaseDAO msTestCaseDAO = mock(MSTestCaseDAO.class);
        List<MSTestCase> msTestCases = new ArrayList<>();
        doReturn(msTestCases).when(msTestCaseDAO).getTestCasesForPullRequest(
                "public-repo", 0);
        doNothing().when(msTestCaseDAO).updateTestCase(any());

        GitHubWebHookController gitHubWebHookController = new GitHubWebHookController(github, msTestCaseDAO);
        Assert.assertFalse(gitHubWebHookController.managePullRequestHook(json));
    }

    @Test
    void closePullRequestTest() throws IOException, MSTestException, MSTSDaoException {
        URL resource = getClass().getClassLoader().getResource("pullRequestClosedEvent.json");
        byte[] encoded = Files.readAllBytes(Paths.get(resource.getPath()));
        String json = new String(encoded);

        Github github = mock(Github.class);

        MSTestCaseDAO msTestCaseDAO = mock(MSTestCaseDAO.class);
        MSTestCase msTestCase = new MSTestCase();
        msTestCase.setPullRequest(null);
        msTestCase.setService("public-repo");
        List<MSTestCase> msTestCases = new ArrayList<>();
        msTestCases.add(msTestCase);
        doReturn(msTestCases).when(msTestCaseDAO).getTestCasesForPullRequest(
                "public-repo", 0);
        doNothing().when(msTestCaseDAO).updateTestCase(any());

        GitHubWebHookController gitHubWebHookController = new GitHubWebHookController(github, msTestCaseDAO);
        Assert.assertFalse(gitHubWebHookController.managePullRequestHook(json));
    }

    @Test(expectedExceptions = MSTestException.class, expectedExceptionsMessageRegExp = "Pull request number not present")
    void pullRequestNumberNotPresentTest() throws IOException, MSTestException, MSTSDaoException {
         URL resource = getClass().getClassLoader().getResource("pullRequestNumberNotPresent.json");
        byte[] encoded = Files.readAllBytes(Paths.get(resource.getPath()));
        String json = new String(encoded);

        Github github = mock(Github.class);

        MSTestCaseDAO msTestCaseDAO = mock(MSTestCaseDAO.class);
        MSTestCase msTestCase = new MSTestCase();
        msTestCase.setPullRequest(null);
        msTestCase.setService("public-repo");
        List<MSTestCase> msTestCases = new ArrayList<>();
        msTestCases.add(msTestCase);
        doReturn(msTestCases).when(msTestCaseDAO).getTestCasesForPullRequest(
                "public-repo", 0);

        GitHubWebHookController gitHubWebHookController = new GitHubWebHookController(github, msTestCaseDAO);
        Assert.assertTrue(gitHubWebHookController.managePullRequestHook(json));
    }

    @Test(expectedExceptions = MSTestException.class, expectedExceptionsMessageRegExp = "Invalid Pull Request Number")
    void pullRequestInvalidNumberTest() throws IOException, MSTestException, MSTSDaoException {
         URL resource = getClass().getClassLoader().getResource("pullRequestInvalidNumber.json");
        byte[] encoded = Files.readAllBytes(Paths.get(resource.getPath()));
        String json = new String(encoded);

        Github github = mock(Github.class);

        MSTestCaseDAO msTestCaseDAO = mock(MSTestCaseDAO.class);
        MSTestCase msTestCase = new MSTestCase();
        msTestCase.setPullRequest(null);
        msTestCase.setService("public-repo");
        List<MSTestCase> msTestCases = new ArrayList<>();
        msTestCases.add(msTestCase);
        doReturn(msTestCases).when(msTestCaseDAO).getTestCasesForPullRequest(
                "public-repo", 0);

        GitHubWebHookController gitHubWebHookController = new GitHubWebHookController(github, msTestCaseDAO);
        Assert.assertTrue(gitHubWebHookController.managePullRequestHook(json));
    }

    @Test(expectedExceptions = IOException.class)
    void invalidJSONTest() throws IOException, MSTestException {
        URL resource = getClass().getClassLoader().getResource("invalid.json");
        byte[] encoded = Files.readAllBytes(Paths.get(resource.getPath()));
        String json = new String(encoded);
        Github github = mock(Github.class);
        MSTestCaseDAO msTestCaseDAO = mock(MSTestCaseDAO.class);
        GitHubWebHookController gitHubWebHookController = new GitHubWebHookController(github, msTestCaseDAO);
        gitHubWebHookController.managePullRequestHook(json);
    }

    @Test
    void validJSONInvalidSchemaTest() throws IOException, MSTestException {
        URL resource = getClass().getClassLoader().getResource("validJSONInvalidData.json");
        byte[] encoded = Files.readAllBytes(Paths.get(resource.getPath()));
        String json = new String(encoded);
        MSTestCaseDAO msTestCaseDAO = mock(MSTestCaseDAO.class);
        Github github = mock(Github.class);
        GitHubWebHookController gitHubWebHookController = new GitHubWebHookController(github, msTestCaseDAO);
        gitHubWebHookController.managePullRequestHook(json);
    }

    @Test(expectedExceptions = MSTestException.class, expectedExceptionsMessageRegExp = "Statuses URL not present")
    void pullRequestPresentStatusAbsentTest() throws IOException, MSTestException {
        URL resource = getClass().getClassLoader().getResource("pullRequestPresentStatusURLAbsent.json");
        byte[] encoded = Files.readAllBytes(Paths.get(resource.getPath()));
        String json = new String(encoded);
        Github github = mock(Github.class);
        MSTestCaseDAO msTestCaseDAO = mock(MSTestCaseDAO.class);
        GitHubWebHookController gitHubWebHookController = new GitHubWebHookController(github, msTestCaseDAO);
        gitHubWebHookController.managePullRequestHook(json);
    }

    @Test(expectedExceptions = MSTestException.class, expectedExceptionsMessageRegExp = "Action not present")
    void actionNotPresentTest() throws IOException, MSTestException {
        URL resource = getClass().getClassLoader().getResource("actionNotPresent.json");
        byte[] encoded = Files.readAllBytes(Paths.get(resource.getPath()));
        String json = new String(encoded);
        Github github = mock(Github.class);
        MSTestCaseDAO msTestCaseDAO = mock(MSTestCaseDAO.class);
        GitHubWebHookController gitHubWebHookController = new GitHubWebHookController(github, msTestCaseDAO);
        gitHubWebHookController.managePullRequestHook(json);
    }

    @Test(expectedExceptions = MSTestException.class)
    void daoExceptionOnFetchTest() throws MSTSDaoException, IOException, MSTestException {
        URL resource = getClass().getClassLoader().getResource("pullRequestOpenedEvent.json");
        byte[] encoded = Files.readAllBytes(Paths.get(resource.getPath()));
        String json = new String(encoded);

        Github github = mock(Github.class);

        MSTestCaseDAO msTestCaseDAO = mock(MSTestCaseDAO.class);
        doThrow(new MSTSDaoException("Get test cases exception")).when(msTestCaseDAO).getTestCasesForPullRequest(
                "public-repo", 0);
        doNothing().when(msTestCaseDAO).updateTestCase(any());

        GitHubWebHookController gitHubWebHookController = new GitHubWebHookController(github, msTestCaseDAO);
        Assert.assertTrue(gitHubWebHookController.managePullRequestHook(json));
    }

    @Test(expectedExceptions = MSTestException.class)
    void daoExceptionOnUpdate() throws IOException, MSTestException, MSTSDaoException {
         URL resource = getClass().getClassLoader().getResource("pullRequestOpenedEvent.json");
        byte[] encoded = Files.readAllBytes(Paths.get(resource.getPath()));
        String json = new String(encoded);

        Github github = mock(Github.class);

        MSTestCaseDAO msTestCaseDAO = mock(MSTestCaseDAO.class);
        MSTestCase msTestCase = new MSTestCase();
        msTestCase.setPullRequest(null);
        msTestCase.setService("public-repo");
        List<MSTestCase> msTestCases = new ArrayList<>();
        msTestCases.add(msTestCase);
        doReturn(msTestCases).when(msTestCaseDAO).getTestCasesForPullRequest(
                "public-repo", 0);
        doThrow(new MSTSDaoException("Error on update")).when(msTestCaseDAO).updateTestCase(any());

        GitHubWebHookController gitHubWebHookController = new GitHubWebHookController(github, msTestCaseDAO);
        Assert.assertTrue(gitHubWebHookController.managePullRequestHook(json));
        Assert.assertEquals(msTestCase.getPullRequest(), Integer.valueOf(1));
    }


    @Test(expectedExceptions = MSTestException.class, expectedExceptionsMessageRegExp = "Repo not present")
    void repoNullTest() throws IOException, MSTestException, MSTSDaoException {
         URL resource = getClass().getClassLoader().getResource("repositoryNullTest.json");
        byte[] encoded = Files.readAllBytes(Paths.get(resource.getPath()));
        String json = new String(encoded);

        Github github = mock(Github.class);

        MSTestCaseDAO msTestCaseDAO = mock(MSTestCaseDAO.class);
        MSTestCase msTestCase = new MSTestCase();
        msTestCase.setPullRequest(null);
        msTestCase.setService("public-repo");
        List<MSTestCase> msTestCases = new ArrayList<>();
        msTestCases.add(msTestCase);
        doReturn(msTestCases).when(msTestCaseDAO).getTestCasesForPullRequest(
                "public-repo", 0);
        doThrow(new MSTSDaoException("Error on update")).when(msTestCaseDAO).updateTestCase(any());

        GitHubWebHookController gitHubWebHookController = new GitHubWebHookController(github, msTestCaseDAO);
        Assert.assertTrue(gitHubWebHookController.managePullRequestHook(json));
    }

    @Test(expectedExceptions = MSTestException.class, expectedExceptionsMessageRegExp = "Repo name not present")
    void repoNameNullTest() throws IOException, MSTestException, MSTSDaoException {
         URL resource = getClass().getClassLoader().getResource("repoNameTest.json");
        byte[] encoded = Files.readAllBytes(Paths.get(resource.getPath()));
        String json = new String(encoded);

        Github github = mock(Github.class);

        MSTestCaseDAO msTestCaseDAO = mock(MSTestCaseDAO.class);
        MSTestCase msTestCase = new MSTestCase();
        msTestCase.setPullRequest(null);
        msTestCase.setService("public-repo");
        List<MSTestCase> msTestCases = new ArrayList<>();
        msTestCases.add(msTestCase);
        doReturn(msTestCases).when(msTestCaseDAO).getTestCasesForPullRequest(
                "public-repo", 0);
        doThrow(new MSTSDaoException("Error on update")).when(msTestCaseDAO).updateTestCase(any());

        GitHubWebHookController gitHubWebHookController = new GitHubWebHookController(github, msTestCaseDAO);
        Assert.assertTrue(gitHubWebHookController.managePullRequestHook(json));
    }
}
