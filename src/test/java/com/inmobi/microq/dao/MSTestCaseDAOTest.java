package com.inmobi.microq.dao;

import com.inmobi.microq.models.MSTestCase;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author prathik.raj
 */
@Test(enabled = false)
public class MSTestCaseDAOTest {
    @Test(enabled = false)
    void getEmptyPullRequest() throws MSTSDaoException {
       MSTestCaseDAO msTestCaseDAO = new MSTestCaseDAO();
        List<MSTestCase> msTestCases = msTestCaseDAO.getTestCasesForPullRequest("test", 0);
        System.out.println("msTestCases = " + msTestCases);
    }

    @Test(enabled = false)
    void getForService() throws MSTSDaoException {
       MSTestCaseDAO msTestCaseDAO = new MSTestCaseDAO();
        List<MSTestCase> msTestCases = msTestCaseDAO.getTestCasesForService("test");
        System.out.println("msTestCases = " + msTestCases);
    }

    @Test(enabled = false)
    void createPullRequest() throws MSTSDaoException {
        MSTestCaseDAO msTestCaseDAO = new MSTestCaseDAO();
        MSTestCase msTestCase = new MSTestCase();
        msTestCase.setHttpMethod("GET");
        msTestCase.setExpectedResponse("sdfsf");
        msTestCase.setHost("sdfsf");
        msTestCase.setUser("prathik");
        msTestCase.setService("test");
        msTestCase.setBody("abc");
        msTestCaseDAO.storeTestCase(msTestCase);
    }

    @Test(enabled = false)
    void updatePullRequest() throws MSTSDaoException {
        MSTestCaseDAO msTestCaseDAO = new MSTestCaseDAO();
        MSTestCase msTestCase = new MSTestCase();
        msTestCase.setId(1);
        msTestCase.setHttpMethod("GET");
        msTestCase.setExpectedResponse("updated");
        msTestCase.setHost("sdfsf");
        msTestCase.setUser("prathik");
        msTestCase.setPullRequest(99);
        msTestCase.setService("test");
        msTestCase.setBody("abc");
        msTestCaseDAO.updateTestCase(msTestCase);
    }
}
