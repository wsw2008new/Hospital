package com.nemanja.hospital.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nemanja.hospital.domain.Examinations;
import com.nemanja.hospital.repository.ExaminationsRepository;
import com.nemanja.hospital.web.rest.util.HeaderUtil;
import com.nemanja.hospital.web.rest.util.PaginationUtil;
import com.nemanja.hospital.web.rest.dto.ExaminationsDTO;
import com.nemanja.hospital.web.rest.mapper.ExaminationsMapper;
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
 * REST controller for managing Examinations.
 */
@RestController
@RequestMapping("/api")
public class ExaminationsResource {

    private final Logger log = LoggerFactory.getLogger(ExaminationsResource.class);
        
    @Inject
    private ExaminationsRepository examinationsRepository;
    
    @Inject
    private ExaminationsMapper examinationsMapper;
    
    /**
     * POST  /examinations : Create a new examinations.
     *
     * @param examinationsDTO the examinationsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new examinationsDTO, or with status 400 (Bad Request) if the examinations has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/examinations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExaminationsDTO> createExaminations(@RequestBody ExaminationsDTO examinationsDTO) throws URISyntaxException {
        log.debug("REST request to save Examinations : {}", examinationsDTO);
        if (examinationsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("examinations", "idexists", "A new examinations cannot already have an ID")).body(null);
        }
        Examinations examinations = examinationsMapper.examinationsDTOToExaminations(examinationsDTO);
        examinations = examinationsRepository.save(examinations);
        ExaminationsDTO result = examinationsMapper.examinationsToExaminationsDTO(examinations);
        return ResponseEntity.created(new URI("/api/examinations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("examinations", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /examinations : Updates an existing examinations.
     *
     * @param examinationsDTO the examinationsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated examinationsDTO,
     * or with status 400 (Bad Request) if the examinationsDTO is not valid,
     * or with status 500 (Internal Server Error) if the examinationsDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/examinations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExaminationsDTO> updateExaminations(@RequestBody ExaminationsDTO examinationsDTO) throws URISyntaxException {
        log.debug("REST request to update Examinations : {}", examinationsDTO);
        if (examinationsDTO.getId() == null) {
            return createExaminations(examinationsDTO);
        }
        Examinations examinations = examinationsMapper.examinationsDTOToExaminations(examinationsDTO);
        examinations = examinationsRepository.save(examinations);
        ExaminationsDTO result = examinationsMapper.examinationsToExaminationsDTO(examinations);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("examinations", examinationsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /examinations : get all the examinations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of examinations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/examinations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ExaminationsDTO>> getAllExaminations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Examinations");
        Page<Examinations> page = examinationsRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/examinations");
        return new ResponseEntity<>(examinationsMapper.examinationsToExaminationsDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /examinations/:id : get the "id" examinations.
     *
     * @param id the id of the examinationsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the examinationsDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/examinations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExaminationsDTO> getExaminations(@PathVariable Long id) {
        log.debug("REST request to get Examinations : {}", id);
        Examinations examinations = examinationsRepository.findOne(id);
        ExaminationsDTO examinationsDTO = examinationsMapper.examinationsToExaminationsDTO(examinations);
        return Optional.ofNullable(examinationsDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /examinations/:id : delete the "id" examinations.
     *
     * @param id the id of the examinationsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/examinations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteExaminations(@PathVariable Long id) {
        log.debug("REST request to delete Examinations : {}", id);
        examinationsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("examinations", id.toString())).build();
    }

}
