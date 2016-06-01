package com.nemanja.hospital.service;

import com.nemanja.hospital.domain.Patient;
import com.nemanja.hospital.repository.PatientRepository;
import com.nemanja.hospital.web.rest.dto.PatientDTO;
import com.nemanja.hospital.web.rest.mapper.PatientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Patient.
 */
@Service
@Transactional
public class PatientService {

    private final Logger log = LoggerFactory.getLogger(PatientService.class);
    
    @Inject
    private PatientRepository patientRepository;
    
    @Inject
    private PatientMapper patientMapper;
    
    /**
     * Save a patient.
     * 
     * @param patientDTO the entity to save
     * @return the persisted entity
     */
    public PatientDTO save(PatientDTO patientDTO) {
        log.debug("Request to save Patient : {}", patientDTO);
        Patient patient = patientMapper.patientDTOToPatient(patientDTO);
        patient = patientRepository.save(patient);
        PatientDTO result = patientMapper.patientToPatientDTO(patient);
        return result;
    }

    /**
     *  Get all the patients.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Patient> findAll(Pageable pageable) {
        log.debug("Request to get all Patients");
        Page<Patient> result = patientRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one patient by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PatientDTO findOne(Long id) {
        log.debug("Request to get Patient : {}", id);
        Patient patient = patientRepository.findOne(id);
        PatientDTO patientDTO = patientMapper.patientToPatientDTO(patient);
        return patientDTO;
    }

    /**
     *  Delete the  patient by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Patient : {}", id);
        patientRepository.delete(id);
    }
}
