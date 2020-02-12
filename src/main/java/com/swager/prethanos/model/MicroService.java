package com.swager.prethanos.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MicroService {

    String name;
    Long noOfAPIs;
    Long noOfReusableAPIs;
    String error;
    Long associatedSpecId;

}
