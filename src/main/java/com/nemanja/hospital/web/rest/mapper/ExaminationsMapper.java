package com.nemanja.hospital.web.rest.mapper;

import com.nemanja.hospital.domain.*;
import com.nemanja.hospital.web.rest.dto.ExaminationsDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Examinations and its DTO ExaminationsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ExaminationsMapper {

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "patient.id", target = "patientId")
    ExaminationsDTO examinationsToExaminationsDTO(Examinations examinations);

    List<ExaminationsDTO> examinationsToExaminationsDTOs(List<Examinations> examinations);

    @Mapping(source = "doctorId", target = "doctor")
    @Mapping(source = "patientId", target = "patient")
    Examinations examinationsDTOToExaminations(ExaminationsDTO examinationsDTO);

    List<Examinations> examinationsDTOsToExaminations(List<ExaminationsDTO> examinationsDTOs);

    default Doctor doctorFromId(Long id) {
        if (id == null) {
            return null;
        }
        Doctor doctor = new Doctor();
        doctor.setId(id);
        return doctor;
    }

    default Patient patientFromId(Long id) {
        if (id == null) {
            return null;
        }
        Patient patient = new Patient();
        patient.setId(id);
        return patient;
    }
}
