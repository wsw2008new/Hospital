package com.nemanja.hospital.service;

import com.nemanja.hospital.domain.Doctor;
import com.nemanja.hospital.repository.DoctorRepository;
import com.nemanja.hospital.web.rest.dto.DoctorDTO;
import com.nemanja.hospital.web.rest.mapper.DoctorMapper;
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
 * Service Implementation for managing Doctor.
 */
@Service
@Transactional
public class DoctorService {

    private final Logger log = LoggerFactory.getLogger(DoctorService.class);
    
    @Inject
    private DoctorRepository doctorRepository;
    
    @Inject
    private DoctorMapper doctorMapper;
    
    /**
     * Save a doctor.
     * 
     * @param doctorDTO the entity to save
     * @return the persisted entity
     */
    public DoctorDTO save(DoctorDTO doctorDTO) {
        log.debug("Request to save Doctor : {}", doctorDTO);
        Doctor doctor = doctorMapper.doctorDTOToDoctor(doctorDTO);
        doctor = doctorRepository.save(doctor);
        DoctorDTO result = doctorMapper.doctorToDoctorDTO(doctor);
        return result;
    }

    /**
     *  Get all the doctors.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Doctor> findAll(Pageable pageable) {
        log.debug("Request to get all Doctors");
        Page<Doctor> result = doctorRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one doctor by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public DoctorDTO findOne(Long id) {
        log.debug("Request to get Doctor : {}", id);
        Doctor doctor = doctorRepository.findOne(id);
        DoctorDTO doctorDTO = doctorMapper.doctorToDoctorDTO(doctor);
        return doctorDTO;
    }

    /**
     *  Delete the  doctor by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Doctor : {}", id);
        doctorRepository.delete(id);
    }
}
