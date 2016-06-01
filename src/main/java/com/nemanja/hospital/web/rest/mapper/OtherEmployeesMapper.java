package com.nemanja.hospital.web.rest.mapper;

import com.nemanja.hospital.domain.*;
import com.nemanja.hospital.web.rest.dto.OtherEmployeesDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity OtherEmployees and its DTO OtherEmployeesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OtherEmployeesMapper {

    OtherEmployeesDTO otherEmployeesToOtherEmployeesDTO(OtherEmployees otherEmployees);

    List<OtherEmployeesDTO> otherEmployeesToOtherEmployeesDTOs(List<OtherEmployees> otherEmployees);

    OtherEmployees otherEmployeesDTOToOtherEmployees(OtherEmployeesDTO otherEmployeesDTO);

    List<OtherEmployees> otherEmployeesDTOsToOtherEmployees(List<OtherEmployeesDTO> otherEmployeesDTOs);
}
