package com.nemanja.hospital.web.rest;

import com.nemanja.hospital.HospitalApp;
import com.nemanja.hospital.domain.Examinations;
import com.nemanja.hospital.repository.ExaminationsRepository;
import com.nemanja.hospital.web.rest.dto.ExaminationsDTO;
import com.nemanja.hospital.web.rest.mapper.ExaminationsMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ExaminationsResource REST controller.
 *
 * @see ExaminationsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HospitalApp.class)
@WebAppConfiguration
@IntegrationTest
public class ExaminationsResourceIntTest {


    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_CONCLUSION = "AAAAA";
    private static final String UPDATED_CONCLUSION = "BBBBB";

    @Inject
    private ExaminationsRepository examinationsRepository;

    @Inject
    private ExaminationsMapper examinationsMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restExaminationsMockMvc;

    private Examinations examinations;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExaminationsResource examinationsResource = new ExaminationsResource();
        ReflectionTestUtils.setField(examinationsResource, "examinationsRepository", examinationsRepository);
        ReflectionTestUtils.setField(examinationsResource, "examinationsMapper", examinationsMapper);
        this.restExaminationsMockMvc = MockMvcBuilders.standaloneSetup(examinationsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        examinations = new Examinations();
        examinations.setDate(DEFAULT_DATE);
        examinations.setConclusion(DEFAULT_CONCLUSION);
    }

    @Test
    @Transactional
    public void createExaminations() throws Exception {
        int databaseSizeBeforeCreate = examinationsRepository.findAll().size();

        // Create the Examinations
        ExaminationsDTO examinationsDTO = examinationsMapper.examinationsToExaminationsDTO(examinations);

        restExaminationsMockMvc.perform(post("/api/examinations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(examinationsDTO)))
                .andExpect(status().isCreated());

        // Validate the Examinations in the database
        List<Examinations> examinations = examinationsRepository.findAll();
        assertThat(examinations).hasSize(databaseSizeBeforeCreate + 1);
        Examinations testExaminations = examinations.get(examinations.size() - 1);
        assertThat(testExaminations.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testExaminations.getConclusion()).isEqualTo(DEFAULT_CONCLUSION);
    }

    @Test
    @Transactional
    public void getAllExaminations() throws Exception {
        // Initialize the database
        examinationsRepository.saveAndFlush(examinations);

        // Get all the examinations
        restExaminationsMockMvc.perform(get("/api/examinations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(examinations.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].conclusion").value(hasItem(DEFAULT_CONCLUSION.toString())));
    }

    @Test
    @Transactional
    public void getExaminations() throws Exception {
        // Initialize the database
        examinationsRepository.saveAndFlush(examinations);

        // Get the examinations
        restExaminationsMockMvc.perform(get("/api/examinations/{id}", examinations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(examinations.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.conclusion").value(DEFAULT_CONCLUSION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExaminations() throws Exception {
        // Get the examinations
        restExaminationsMockMvc.perform(get("/api/examinations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExaminations() throws Exception {
        // Initialize the database
        examinationsRepository.saveAndFlush(examinations);
        int databaseSizeBeforeUpdate = examinationsRepository.findAll().size();

        // Update the examinations
        Examinations updatedExaminations = new Examinations();
        updatedExaminations.setId(examinations.getId());
        updatedExaminations.setDate(UPDATED_DATE);
        updatedExaminations.setConclusion(UPDATED_CONCLUSION);
        ExaminationsDTO examinationsDTO = examinationsMapper.examinationsToExaminationsDTO(updatedExaminations);

        restExaminationsMockMvc.perform(put("/api/examinations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(examinationsDTO)))
                .andExpect(status().isOk());

        // Validate the Examinations in the database
        List<Examinations> examinations = examinationsRepository.findAll();
        assertThat(examinations).hasSize(databaseSizeBeforeUpdate);
        Examinations testExaminations = examinations.get(examinations.size() - 1);
        assertThat(testExaminations.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testExaminations.getConclusion()).isEqualTo(UPDATED_CONCLUSION);
    }

    @Test
    @Transactional
    public void deleteExaminations() throws Exception {
        // Initialize the database
        examinationsRepository.saveAndFlush(examinations);
        int databaseSizeBeforeDelete = examinationsRepository.findAll().size();

        // Get the examinations
        restExaminationsMockMvc.perform(delete("/api/examinations/{id}", examinations.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Examinations> examinations = examinationsRepository.findAll();
        assertThat(examinations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
