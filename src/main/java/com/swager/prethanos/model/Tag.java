package com.swager.prethanos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tag {

@JsonProperty("name")
public String name;
@JsonProperty("description")
public String description;

}