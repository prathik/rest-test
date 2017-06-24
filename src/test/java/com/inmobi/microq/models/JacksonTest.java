package com.inmobi.microq.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmobi.microq.scm.github.models.GithubStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author prathik.raj
 */
@Test
public class JacksonTest {
    @Test
    void githubStatusTest() throws JsonProcessingException {
        GithubStatus githubStatus = new GithubStatus();
        githubStatus.setContext("test");
        githubStatus.setDescription("Test object");
        githubStatus.setTargetUrl("www.thiscoder.rocks");
        githubStatus.setState("success");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(githubStatus);
        System.out.println("json = " + json);
        Assert.assertEquals(json, "{\"state\":\"success\",\"target_url\":\"www.thiscoder.rocks\"" +
                ",\"description\":\"Test object\",\"context\":\"test\"}");
    }
}
