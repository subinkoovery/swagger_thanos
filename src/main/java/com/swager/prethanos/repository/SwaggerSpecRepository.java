package com.swager.prethanos.repository;

import com.swager.prethanos.entity.SwaggerSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Interface to hold the SwaggerSpecRepository.
 * @author Subin Chalil
 */
@Repository
public interface SwaggerSpecRepository extends JpaRepository<SwaggerSpec, Long> {

    List<SwaggerSpec> findAllByOrderByPriorityAscNameAsc();
}
