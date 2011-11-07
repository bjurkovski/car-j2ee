/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.managers;

import java.util.Date;
import javax.ejb.Stateless;
import org.banque.entities.Person;

/**
 *
 * @author bjurkovski
 */
@Stateless
public class PersonManager implements PersonManagerLocal {

    public PersonManager() {
    }

    /**
     * Creates a person in the database
     * @param name
     * @param lastName
     * @param password
     * @param dateOfBirth
     * @param gender Use Constants in Person
     * @return the newly created person
     */
    public Person createPerson(String name, String lastName, String password, Date dateOfBirth, int gender) {
        EntityManager.getEntityManager().getTransaction().begin();
        Person aPerson = new Person(name, lastName, password, gender, dateOfBirth);
        EntityManager.getEntityManager().persist(aPerson);
        EntityManager.getEntityManager().getTransaction().commit();
        return aPerson;
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    //TODO VERIFY EXISTENCE IN DATABASE AND CORRECTNESS
}
