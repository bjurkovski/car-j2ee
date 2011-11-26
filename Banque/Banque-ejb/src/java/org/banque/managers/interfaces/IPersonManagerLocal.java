/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.managers.interfaces;

import java.util.Date;
import javax.ejb.Local;
import org.banque.entities.Person;
import org.banque.entities.Person.Gender;
import org.banque.exceptions.BanqueException;

/**
 *
 * @author bjurkovski
 * @author wasser
 */
@Local
public interface IPersonManagerLocal {

    /**
     * Creates a person in the database
     * @param name
     * @param lastName
     * @param password in clear text
     * @param dateOfBirth
     * @param gender Use Constants in Person
     * @return the newly created person or null if the creation failed
     */
    public Person createPerson(String name, String lastName, String password, Date dateOfBirth, Gender gender, String address) throws BanqueException;

    public void deletePerson(long id) throws BanqueException;

    public Person findPerson(long id) throws BanqueException;
}
