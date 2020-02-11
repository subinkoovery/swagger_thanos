package com.swager.prethanos.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tag {

@JsonProperty("name")
public String name;
@JsonProperty("description")
public String description;

}