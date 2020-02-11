package com.swager.prethanos.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Dashboard {

    Integer noOfMicroservice;
    Long totalNoOfAPIs;
}
