package com.nemanja.hospital.web.rest.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

import com.nemanja.hospital.domain.enumeration.EmployeePositions;

/**
 * A DTO for the OtherEmployees entity.
 */
public class OtherEmployeesDTO implements Serializable {

    private Long id;

    private String fname;

    private String lname;

    private LocalDate dateOfBirth;

    private String email;

    private String phoneNumber;

    private EmployeePositions position;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public EmployeePositions getPosition() {
        return position;
    }

    public void setPosition(EmployeePositions position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OtherEmployeesDTO otherEmployeesDTO = (OtherEmployeesDTO) o;

        if ( ! Objects.equals(id, otherEmployeesDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OtherEmployeesDTO{" +
            "id=" + id +
            ", fname='" + fname + "'" +
            ", lname='" + lname + "'" +
            ", dateOfBirth='" + dateOfBirth + "'" +
            ", email='" + email + "'" +
            ", phoneNumber='" + phoneNumber + "'" +
            ", position='" + position + "'" +
            '}';
    }
}
