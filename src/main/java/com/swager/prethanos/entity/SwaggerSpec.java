package com.swager.prethanos.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * Class to Persist swagger json.
 * @author Subin Chalil
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "SWAGGER_SPEC")
public class SwaggerSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String url;
    @NotEmpty
    String version;
    Integer priority;
    String error;
    @Column(columnDefinition = "boolean default false")
    Boolean isAuthRequired;
    String userName;
    String password;
}
