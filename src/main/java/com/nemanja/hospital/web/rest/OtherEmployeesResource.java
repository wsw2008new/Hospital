package com.nemanja.hospital.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nemanja.hospital.domain.OtherEmployees;
import com.nemanja.hospital.repository.OtherEmployeesRepository;
import com.nemanja.hospital.web.rest.util.HeaderUtil;
import com.nemanja.hospital.web.rest.util.PaginationUtil;
import com.nemanja.hospital.web.rest.dto.OtherEmployeesDTO;
import com.nemanja.hospital.web.rest.mapper.OtherEmployeesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing OtherEmployees.
 */
@RestController
@RequestMapping("/api")
public class OtherEmployeesResource {

    private final Logger log = LoggerFactory.getLogger(OtherEmployeesResource.class);
        
    @Inject
    private OtherEmployeesRepository otherEmployeesRepository;
    
    @Inject
    private OtherEmployeesMapper otherEmployeesMapper;
    
    /**
     * POST  /other-employees : Create a new otherEmployees.
     *
     * @param otherEmployeesDTO the otherEmployeesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new otherEmployeesDTO, or with status 400 (Bad Request) if the otherEmployees has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/other-employees",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OtherEmployeesDTO> createOtherEmployees(@RequestBody OtherEmployeesDTO otherEmployeesDTO) throws URISyntaxException {
        log.debug("REST request to save OtherEmployees : {}", otherEmployeesDTO);
        if (otherEmployeesDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("otherEmployees", "idexists", "A new otherEmployees cannot already have an ID")).body(null);
        }
        OtherEmployees otherEmployees = otherEmployeesMapper.otherEmployeesDTOToOtherEmployees(otherEmployeesDTO);
        otherEmployees = otherEmployeesRepository.save(otherEmployees);
        OtherEmployeesDTO result = otherEmployeesMapper.otherEmployeesToOtherEmployeesDTO(otherEmployees);
        return ResponseEntity.created(new URI("/api/other-employees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("otherEmployees", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /other-employees : Updates an existing otherEmployees.
     *
     * @param otherEmployeesDTO the otherEmployeesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated otherEmployeesDTO,
     * or with status 400 (Bad Request) if the otherEmployeesDTO is not valid,
     * or with status 500 (Internal Server Error) if the otherEmployeesDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/other-employees",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OtherEmployeesDTO> updateOtherEmployees(@RequestBody OtherEmployeesDTO otherEmployeesDTO) throws URISyntaxException {
        log.debug("REST request to update OtherEmployees : {}", otherEmployeesDTO);
        if (otherEmployeesDTO.getId() == null) {
            return createOtherEmployees(otherEmployeesDTO);
        }
        OtherEmployees otherEmployees = otherEmployeesMapper.otherEmployeesDTOToOtherEmployees(otherEmployeesDTO);
        otherEmployees = otherEmployeesRepository.save(otherEmployees);
        OtherEmployeesDTO result = otherEmployeesMapper.otherEmployeesToOtherEmployeesDTO(otherEmployees);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("otherEmployees", otherEmployeesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /other-employees : get all the otherEmployees.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of otherEmployees in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/other-employees",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<OtherEmployeesDTO>> getAllOtherEmployees(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of OtherEmployees");
        Page<OtherEmployees> page = otherEmployeesRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/other-employees");
        return new ResponseEntity<>(otherEmployeesMapper.otherEmployeesToOtherEmployeesDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /other-employees/:id : get the "id" otherEmployees.
     *
     * @param id the id of the otherEmployeesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the otherEmployeesDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/other-employees/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OtherEmployeesDTO> getOtherEmployees(@PathVariable Long id) {
        log.debug("REST request to get OtherEmployees : {}", id);
        OtherEmployees otherEmployees = otherEmployeesRepository.findOne(id);
        OtherEmployeesDTO otherEmployeesDTO = otherEmployeesMapper.otherEmployeesToOtherEmployeesDTO(otherEmployees);
        return Optional.ofNullable(otherEmployeesDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /other-employees/:id : delete the "id" otherEmployees.
     *
     * @param id the id of the otherEmployeesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/other-employees/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOtherEmployees(@PathVariable Long id) {
        log.debug("REST request to delete OtherEmployees : {}", id);
        otherEmployeesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("otherEmployees", id.toString())).build();
    }

}
