/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.managers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.banque.entities.Person;
import org.banque.entities.Person.Gender;
import org.banque.exceptions.BanqueException;

/**
 *
 * @author bjurkovski
 * @author wasser
 */
@Stateless
public class PersonManager implements IPersonManagerLocal {

    @PersistenceContext(unitName = "BanquePU")
    private EntityManager em;

    @Override
    public Person createPerson(Person person) throws BanqueException {
        person.setPassword(hashPassword(person.getPassword()));
        em.persist(person);
        return person;
    }

    @Override
    public Person createPerson(String name, String lastName, String password, Date dateOfBirth, Gender gender) throws BanqueException {
        Person aPerson = new Person(name, lastName, password, gender, dateOfBirth);
        return createPerson(aPerson);
    }

    @Override
    public Person findPerson(long id) {
        return em.find(Person.class, id);
    }

    @Override
    public void deletePerson(long id) {
        Person aPerson = findPerson(id);
        if (aPerson != null) {
            em.remove(em.merge(aPerson));
        }
    }

    /**
     * Hashes a text using the SHA-1 Algorithm
     * @param password Text to hash
     * @return the hashed password
     * @throws BanqueException if the password could not be hashed
     */
    private String hashPassword(String password) throws BanqueException {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Password Encryption Failed " + ex.getMessage());
            throw new BanqueException(0);
        }

        byte[] digest = md.digest(password.getBytes());

        //Converting to a normalized hex string (for reading and storing purposes)
        //http://stackoverflow.com/questions/332079/in-java-how-do-i-convert-a-byte-array-to-a-string-of-hex-digits-while-keeping-l
        StringBuilder hexString = new StringBuilder();
        String hex;
        for (int i = 0; i < digest.length; i++) {
            hex = Integer.toHexString(0xFF & digest[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}