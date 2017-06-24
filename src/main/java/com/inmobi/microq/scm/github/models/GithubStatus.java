package com.inmobi.microq.scm.github.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author prathik.raj
 */
@Data
public class GithubStatus {
    @JsonProperty("state")
    String state;
    @JsonProperty("target_url")
    String targetUrl;
    @JsonProperty("description")
    String description;
    @JsonProperty("context")
    String context;
}
