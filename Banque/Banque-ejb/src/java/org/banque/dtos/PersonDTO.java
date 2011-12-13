package org.banque.dtos;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wasser
 */
public abstract class PersonDTO implements Serializable {

    public static enum Gender {

        MALE, FEMALE
    }
    protected Long id;
    protected String name;
    protected String lastName;
    protected String address;
    protected String password;
    protected Gender gender;
    protected Date dateOfBirth;

    public PersonDTO() {
    }

    protected PersonDTO(String name, String lastName, String password, Gender gender, Date dateOfBirth, String address) {
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Hashed Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password Already Hashed password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PersonDTO)) {
            return false;
        }
        PersonDTO other = (PersonDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", name=" + name + ", lastName=" + lastName + '}';
    }
}
