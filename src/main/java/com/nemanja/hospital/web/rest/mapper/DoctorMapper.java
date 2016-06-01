package com.nemanja.hospital.web.rest.mapper;

import com.nemanja.hospital.domain.*;
import com.nemanja.hospital.web.rest.dto.DoctorDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Doctor and its DTO DoctorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DoctorMapper {

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    DoctorDTO doctorToDoctorDTO(Doctor doctor);

    List<DoctorDTO> doctorsToDoctorDTOs(List<Doctor> doctors);

    @Mapping(source = "departmentId", target = "department")
    @Mapping(target = "patients", ignore = true)
    @Mapping(target = "examinations", ignore = true)
    Doctor doctorDTOToDoctor(DoctorDTO doctorDTO);

    List<Doctor> doctorDTOsToDoctors(List<DoctorDTO> doctorDTOs);

    default Department departmentFromId(Long id) {
        if (id == null) {
            return null;
        }
        Department department = new Department();
        department.setId(id);
        return department;
    }
}
