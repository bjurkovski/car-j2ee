/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 * Describes the basic characteristics of the users
 * @author bjurkovski
 * @author wasser
 * @author ricardo
 */
@Entity
public class Person implements Serializable {

    public enum Gender {

        MALE, FEMALE
    }
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String lastName;
    //TODO: Address
    private String password;
    private Gender gender;
    //TODO: BirthCountry?
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateOfBirth;

    public Person() {
    }

    public Person(String name, String lastName, String password, Gender gender, Date dateOfBirth) {
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
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
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
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
