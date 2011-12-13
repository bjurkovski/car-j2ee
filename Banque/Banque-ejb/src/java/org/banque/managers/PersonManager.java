/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.managers;

import java.security.MessageDigest;
import org.banque.dtos.PersonDTO;
import org.banque.entities.Person;
import org.banque.exceptions.BanqueException;

/**
 *
 * @author bjurkovski
 * @author wasser
 */
public abstract class PersonManager {

    /**
     * Returns a gender that can be used in the DTO objects instead of the entities
     * one
     * @param gender
     * @return 
     */
    protected static PersonDTO.Gender getGenderDTO(Person.Gender gender) {
        if (gender == Person.Gender.MALE) {
            return PersonDTO.Gender.MALE;
        } else {
            return PersonDTO.Gender.FEMALE;
        }
    }

    /**
     * Returns a gender that can be used in the entities instead of the dtos
     * @param gender
     * @return 
     */
    protected static Person.Gender getGenderEntity(PersonDTO.Gender gender) {
        if (gender == PersonDTO.Gender.MALE) {
            return Person.Gender.MALE;
        } else {
            return Person.Gender.FEMALE;
        }
    }

    /**
     * Hashes a text using the SHA-1 Algorithm
     * @param password Text to hash
     * @return the hashed password
     * @throws BanqueException if the password could not be hashed
     */
    protected static String hashPassword(String password) throws BanqueException {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (Exception ex) {
            System.out.println("Password Encryption Failed " + ex.getMessage());
            throw new BanqueException(BanqueException.ErrorType.ERROR_HASHING_PASSWORD);
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