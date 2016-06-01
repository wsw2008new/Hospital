package com.nemanja.hospital.web.rest;

import com.nemanja.hospital.HospitalApp;
import com.nemanja.hospital.domain.OtherEmployees;
import com.nemanja.hospital.repository.OtherEmployeesRepository;
import com.nemanja.hospital.web.rest.dto.OtherEmployeesDTO;
import com.nemanja.hospital.web.rest.mapper.OtherEmployeesMapper;

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

import com.nemanja.hospital.domain.enumeration.EmployeePositions;

/**
 * Test class for the OtherEmployeesResource REST controller.
 *
 * @see OtherEmployeesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HospitalApp.class)
@WebAppConfiguration
@IntegrationTest
public class OtherEmployeesResourceIntTest {

    private static final String DEFAULT_FNAME = "AAAAA";
    private static final String UPDATED_FNAME = "BBBBB";
    private static final String DEFAULT_LNAME = "AAAAA";
    private static final String UPDATED_LNAME = "BBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_PHONE_NUMBER = "AAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBB";

    private static final EmployeePositions DEFAULT_POSITION = EmployeePositions.nurse;
    private static final EmployeePositions UPDATED_POSITION = EmployeePositions.janitor;

    @Inject
    private OtherEmployeesRepository otherEmployeesRepository;

    @Inject
    private OtherEmployeesMapper otherEmployeesMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOtherEmployeesMockMvc;

    private OtherEmployees otherEmployees;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OtherEmployeesResource otherEmployeesResource = new OtherEmployeesResource();
        ReflectionTestUtils.setField(otherEmployeesResource, "otherEmployeesRepository", otherEmployeesRepository);
        ReflectionTestUtils.setField(otherEmployeesResource, "otherEmployeesMapper", otherEmployeesMapper);
        this.restOtherEmployeesMockMvc = MockMvcBuilders.standaloneSetup(otherEmployeesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        otherEmployees = new OtherEmployees();
        otherEmployees.setFname(DEFAULT_FNAME);
        otherEmployees.setLname(DEFAULT_LNAME);
        otherEmployees.setDateOfBirth(DEFAULT_DATE_OF_BIRTH);
        otherEmployees.setEmail(DEFAULT_EMAIL);
        otherEmployees.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        otherEmployees.setPosition(DEFAULT_POSITION);
    }

    @Test
    @Transactional
    public void createOtherEmployees() throws Exception {
        int databaseSizeBeforeCreate = otherEmployeesRepository.findAll().size();

        // Create the OtherEmployees
        OtherEmployeesDTO otherEmployeesDTO = otherEmployeesMapper.otherEmployeesToOtherEmployeesDTO(otherEmployees);

        restOtherEmployeesMockMvc.perform(post("/api/other-employees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(otherEmployeesDTO)))
                .andExpect(status().isCreated());

        // Validate the OtherEmployees in the database
        List<OtherEmployees> otherEmployees = otherEmployeesRepository.findAll();
        assertThat(otherEmployees).hasSize(databaseSizeBeforeCreate + 1);
        OtherEmployees testOtherEmployees = otherEmployees.get(otherEmployees.size() - 1);
        assertThat(testOtherEmployees.getFname()).isEqualTo(DEFAULT_FNAME);
        assertThat(testOtherEmployees.getLname()).isEqualTo(DEFAULT_LNAME);
        assertThat(testOtherEmployees.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testOtherEmployees.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOtherEmployees.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testOtherEmployees.getPosition()).isEqualTo(DEFAULT_POSITION);
    }

    @Test
    @Transactional
    public void getAllOtherEmployees() throws Exception {
        // Initialize the database
        otherEmployeesRepository.saveAndFlush(otherEmployees);

        // Get all the otherEmployees
        restOtherEmployeesMockMvc.perform(get("/api/other-employees?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(otherEmployees.getId().intValue())))
                .andExpect(jsonPath("$.[*].fname").value(hasItem(DEFAULT_FNAME.toString())))
                .andExpect(jsonPath("$.[*].lname").value(hasItem(DEFAULT_LNAME.toString())))
                .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())));
    }

    @Test
    @Transactional
    public void getOtherEmployees() throws Exception {
        // Initialize the database
        otherEmployeesRepository.saveAndFlush(otherEmployees);

        // Get the otherEmployees
        restOtherEmployeesMockMvc.perform(get("/api/other-employees/{id}", otherEmployees.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(otherEmployees.getId().intValue()))
            .andExpect(jsonPath("$.fname").value(DEFAULT_FNAME.toString()))
            .andExpect(jsonPath("$.lname").value(DEFAULT_LNAME.toString()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOtherEmployees() throws Exception {
        // Get the otherEmployees
        restOtherEmployeesMockMvc.perform(get("/api/other-employees/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOtherEmployees() throws Exception {
        // Initialize the database
        otherEmployeesRepository.saveAndFlush(otherEmployees);
        int databaseSizeBeforeUpdate = otherEmployeesRepository.findAll().size();

        // Update the otherEmployees
        OtherEmployees updatedOtherEmployees = new OtherEmployees();
        updatedOtherEmployees.setId(otherEmployees.getId());
        updatedOtherEmployees.setFname(UPDATED_FNAME);
        updatedOtherEmployees.setLname(UPDATED_LNAME);
        updatedOtherEmployees.setDateOfBirth(UPDATED_DATE_OF_BIRTH);
        updatedOtherEmployees.setEmail(UPDATED_EMAIL);
        updatedOtherEmployees.setPhoneNumber(UPDATED_PHONE_NUMBER);
        updatedOtherEmployees.setPosition(UPDATED_POSITION);
        OtherEmployeesDTO otherEmployeesDTO = otherEmployeesMapper.otherEmployeesToOtherEmployeesDTO(updatedOtherEmployees);

        restOtherEmployeesMockMvc.perform(put("/api/other-employees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(otherEmployeesDTO)))
                .andExpect(status().isOk());

        // Validate the OtherEmployees in the database
        List<OtherEmployees> otherEmployees = otherEmployeesRepository.findAll();
        assertThat(otherEmployees).hasSize(databaseSizeBeforeUpdate);
        OtherEmployees testOtherEmployees = otherEmployees.get(otherEmployees.size() - 1);
        assertThat(testOtherEmployees.getFname()).isEqualTo(UPDATED_FNAME);
        assertThat(testOtherEmployees.getLname()).isEqualTo(UPDATED_LNAME);
        assertThat(testOtherEmployees.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testOtherEmployees.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOtherEmployees.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testOtherEmployees.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void deleteOtherEmployees() throws Exception {
        // Initialize the database
        otherEmployeesRepository.saveAndFlush(otherEmployees);
        int databaseSizeBeforeDelete = otherEmployeesRepository.findAll().size();

        // Get the otherEmployees
        restOtherEmployeesMockMvc.perform(delete("/api/other-employees/{id}", otherEmployees.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OtherEmployees> otherEmployees = otherEmployeesRepository.findAll();
        assertThat(otherEmployees).hasSize(databaseSizeBeforeDelete - 1);
    }
}
