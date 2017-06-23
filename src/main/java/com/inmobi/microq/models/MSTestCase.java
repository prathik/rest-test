package com.inmobi.microq.models;

import lombok.Data;

import java.util.Map;

/**
 * @author prathik.raj
 */

@Data
public class MSTestCase {
    Integer id;
    String name;
    String httpMethod;
    String expectedResponse;
    String host;
    String user;
    String service;
    Integer pullRequest;
    String body;
}
