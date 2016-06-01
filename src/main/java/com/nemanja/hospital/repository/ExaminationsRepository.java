package com.nemanja.hospital.repository;

import com.nemanja.hospital.domain.Examinations;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Examinations entity.
 */
@SuppressWarnings("unused")
public interface ExaminationsRepository extends JpaRepository<Examinations,Long> {

}
