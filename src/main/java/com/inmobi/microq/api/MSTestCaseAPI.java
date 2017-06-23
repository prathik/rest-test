package com.inmobi.microq.api;

import com.inmobi.microq.dao.MSTSDaoException;
import com.inmobi.microq.dao.MSTestCaseDAO;
import com.inmobi.microq.models.MSTestCase;
import com.inmobi.microq.dao.Github;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author prathik.raj
 */
@Path("tc")
@Slf4j
public class MSTestCaseAPI {
    // TODO: Better DI?
    final MSTestCaseDAO dao = new MSTestCaseDAO();

    @POST
    @Path("/{service}/")
    @Consumes(MediaType.APPLICATION_JSON)
    public MSTSApiResponse createNewTestCase(MSTestCase testCase, @PathParam("service") String service) {
        MSTSApiResponse response = new MSTSApiResponse();

        if(testCase.getId() != null) {
            response.setCode(400);
            response.setMessage("ID present, maybe you wanted to make a PUT call?");
        }

        testCase.setService(service);

        try {
            dao.storeTestCase(testCase);
            response.setCode(200);
            response.setMessage("Store successful");
        } catch (MSTSDaoException e) {
            response.setCode(500);
            response.setMessage("Error while storing the test case");
            e.printStackTrace();
        }
        return response;
    }

    @GET
    @Path("/{service}/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MSTestCase> getTestCasesForService(@PathParam("service") String service) {
        try {
            return dao.getTestCasesForService(service);
        } catch (MSTSDaoException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GET
    @Path("/{service}/{pr}/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MSTestCase> getTestCasesForServiceAndPullRequest(@PathParam("service") String service,
                                                                 @PathParam("pr") Integer pr) {
        try {
            return dao.getTestCasesForPullRequest(service, pr);
        } catch (MSTSDaoException e) {
            e.printStackTrace();
        }
        return null;
    }
}
