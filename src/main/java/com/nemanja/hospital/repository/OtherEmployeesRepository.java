package com.nemanja.hospital.repository;

import com.nemanja.hospital.domain.OtherEmployees;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OtherEmployees entity.
 */
@SuppressWarnings("unused")
public interface OtherEmployeesRepository extends JpaRepository<OtherEmployees,Long> {

}
