/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.managers;

import java.util.Date;
import javax.ejb.Remote;
import org.banque.entities.Person;
import org.banque.entities.Person.Gender;
import org.banque.exceptions.BanqueException;

/**
 *
 * @author wasser
 */
@Remote
public interface IPersonManagerRemote {

    /**
     * Creates a person in the database
     * @param name
     * @param lastName
     * @param password
     * @param dateOfBirth
     * @param gender Use Constants in Person
     * @return the newly created person or null if the creation failed
     */
    public Person createPerson(String name, String lastName, String password, Date dateOfBirth, Gender gender) throws BanqueException;

    /**
     * Creates a new person in the database.
     * @param person a Person with the password in CLEARTEXT (this method will encrypt the password)
     * @return the persisted Person
     */
    public Person createPerson(Person person) throws BanqueException;

    public void deletePerson(long id) throws BanqueException;

    public Person findPerson(long id) throws BanqueException;
}
