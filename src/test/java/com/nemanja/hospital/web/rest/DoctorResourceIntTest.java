package com.nemanja.hospital.web.rest;

import com.nemanja.hospital.HospitalApp;
import com.nemanja.hospital.domain.Doctor;
import com.nemanja.hospital.repository.DoctorRepository;
import com.nemanja.hospital.service.DoctorService;
import com.nemanja.hospital.web.rest.dto.DoctorDTO;
import com.nemanja.hospital.web.rest.mapper.DoctorMapper;

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
 * Test class for the DoctorResource REST controller.
 *
 * @see DoctorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HospitalApp.class)
@WebAppConfiguration
@IntegrationTest
public class DoctorResourceIntTest {

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
    private static final String DEFAULT_DEGREE = "AAAAA";
    private static final String UPDATED_DEGREE = "BBBBB";
    private static final String DEFAULT_POSITION = "AAAAA";
    private static final String UPDATED_POSITION = "BBBBB";

    @Inject
    private DoctorRepository doctorRepository;

    @Inject
    private DoctorMapper doctorMapper;

    @Inject
    private DoctorService doctorService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDoctorMockMvc;

    private Doctor doctor;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DoctorResource doctorResource = new DoctorResource();
        ReflectionTestUtils.setField(doctorResource, "doctorService", doctorService);
        ReflectionTestUtils.setField(doctorResource, "doctorMapper", doctorMapper);
        this.restDoctorMockMvc = MockMvcBuilders.standaloneSetup(doctorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        doctor = new Doctor();
        doctor.setFname(DEFAULT_FNAME);
        doctor.setLname(DEFAULT_LNAME);
        doctor.setDateOfBirth(DEFAULT_DATE_OF_BIRTH);
        doctor.setEmail(DEFAULT_EMAIL);
        doctor.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        doctor.setDegree(DEFAULT_DEGREE);
        doctor.setPosition(DEFAULT_POSITION);
    }

    @Test
    @Transactional
    public void createDoctor() throws Exception {
        int databaseSizeBeforeCreate = doctorRepository.findAll().size();

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.doctorToDoctorDTO(doctor);

        restDoctorMockMvc.perform(post("/api/doctors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
                .andExpect(status().isCreated());

        // Validate the Doctor in the database
        List<Doctor> doctors = doctorRepository.findAll();
        assertThat(doctors).hasSize(databaseSizeBeforeCreate + 1);
        Doctor testDoctor = doctors.get(doctors.size() - 1);
        assertThat(testDoctor.getFname()).isEqualTo(DEFAULT_FNAME);
        assertThat(testDoctor.getLname()).isEqualTo(DEFAULT_LNAME);
        assertThat(testDoctor.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testDoctor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDoctor.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testDoctor.getDegree()).isEqualTo(DEFAULT_DEGREE);
        assertThat(testDoctor.getPosition()).isEqualTo(DEFAULT_POSITION);
    }

    @Test
    @Transactional
    public void getAllDoctors() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctors
        restDoctorMockMvc.perform(get("/api/doctors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
                .andExpect(jsonPath("$.[*].fname").value(hasItem(DEFAULT_FNAME.toString())))
                .andExpect(jsonPath("$.[*].lname").value(hasItem(DEFAULT_LNAME.toString())))
                .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].degree").value(hasItem(DEFAULT_DEGREE.toString())))
                .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())));
    }

    @Test
    @Transactional
    public void getDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get the doctor
        restDoctorMockMvc.perform(get("/api/doctors/{id}", doctor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(doctor.getId().intValue()))
            .andExpect(jsonPath("$.fname").value(DEFAULT_FNAME.toString()))
            .andExpect(jsonPath("$.lname").value(DEFAULT_LNAME.toString()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.degree").value(DEFAULT_DEGREE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDoctor() throws Exception {
        // Get the doctor
        restDoctorMockMvc.perform(get("/api/doctors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Update the doctor
        Doctor updatedDoctor = new Doctor();
        updatedDoctor.setId(doctor.getId());
        updatedDoctor.setFname(UPDATED_FNAME);
        updatedDoctor.setLname(UPDATED_LNAME);
        updatedDoctor.setDateOfBirth(UPDATED_DATE_OF_BIRTH);
        updatedDoctor.setEmail(UPDATED_EMAIL);
        updatedDoctor.setPhoneNumber(UPDATED_PHONE_NUMBER);
        updatedDoctor.setDegree(UPDATED_DEGREE);
        updatedDoctor.setPosition(UPDATED_POSITION);
        DoctorDTO doctorDTO = doctorMapper.doctorToDoctorDTO(updatedDoctor);

        restDoctorMockMvc.perform(put("/api/doctors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
                .andExpect(status().isOk());

        // Validate the Doctor in the database
        List<Doctor> doctors = doctorRepository.findAll();
        assertThat(doctors).hasSize(databaseSizeBeforeUpdate);
        Doctor testDoctor = doctors.get(doctors.size() - 1);
        assertThat(testDoctor.getFname()).isEqualTo(UPDATED_FNAME);
        assertThat(testDoctor.getLname()).isEqualTo(UPDATED_LNAME);
        assertThat(testDoctor.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testDoctor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDoctor.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testDoctor.getDegree()).isEqualTo(UPDATED_DEGREE);
        assertThat(testDoctor.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void deleteDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);
        int databaseSizeBeforeDelete = doctorRepository.findAll().size();

        // Get the doctor
        restDoctorMockMvc.perform(delete("/api/doctors/{id}", doctor.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Doctor> doctors = doctorRepository.findAll();
        assertThat(doctors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
