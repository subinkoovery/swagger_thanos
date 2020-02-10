package com.swager.prethanos.repository;

import com.swager.prethanos.entity.SwaggerSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwaggerSpecRepository extends JpaRepository<SwaggerSpec, Long> {

    List<SwaggerSpec> findAllByOrderByPriorityAscNameAsc();
}
