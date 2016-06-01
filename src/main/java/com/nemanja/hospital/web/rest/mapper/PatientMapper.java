package com.nemanja.hospital.web.rest.mapper;

import com.nemanja.hospital.domain.*;
import com.nemanja.hospital.web.rest.dto.PatientDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Patient and its DTO PatientDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PatientMapper {

    @Mapping(source = "doctor.id", target = "doctorId")
    PatientDTO patientToPatientDTO(Patient patient);

    List<PatientDTO> patientsToPatientDTOs(List<Patient> patients);

    @Mapping(source = "doctorId", target = "doctor")
    @Mapping(target = "examinations", ignore = true)
    Patient patientDTOToPatient(PatientDTO patientDTO);

    List<Patient> patientDTOsToPatients(List<PatientDTO> patientDTOs);

    default Doctor doctorFromId(Long id) {
        if (id == null) {
            return null;
        }
        Doctor doctor = new Doctor();
        doctor.setId(id);
        return doctor;
    }
}
