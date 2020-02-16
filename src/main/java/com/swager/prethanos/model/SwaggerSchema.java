package com.swager.prethanos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Class to hold the swagger json schema.
 * @author Subin Chalil
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SwaggerSchema {

@JsonProperty("swagger")
public String swagger;
@JsonProperty("host")
public String host;
@JsonProperty("basePath")
public String basePath;
@JsonProperty("tags")
public List<Tag> tags = null;
@JsonProperty("paths")
public Map<String,Map<String,Object>> paths;

}