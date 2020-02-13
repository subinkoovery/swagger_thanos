package com.swager.prethanos.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MicroService {

    String name;
    Long noOfAPIs;
    Long noOfReusableAPIs;
    String error;
    Long associatedSpecId;

}
