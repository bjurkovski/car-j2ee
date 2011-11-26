/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.managers.interfaces;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import org.banque.entities.Client;
import org.banque.entities.Person.Gender;
import org.banque.exceptions.BanqueException;

/**
 *
 * @author wasser
 */
@Local
public interface IClientManagerLocal {

    /**
     * Creates a new client in the database assigning him an id
     * @param client a client with all the fields filled (password in clear text)
     * @return the persisted client with a unique id
     * @throws BanqueException DATABASE_ERROR
     */
    public Client createClient(Client client) throws BanqueException;

    /**
     * Creates a new client in the database assigning him an id
     * @param password - in clear text
     * @return the persisted client with a unique id
     * @throws BanqueException DATABASE_ERROR
     */
    public Client createClient(String name, String lastName, String password, Gender gender, Date dateOfBirth, String address, String email) throws BanqueException;

    /**
     * Searches the database for the client and deletes him
     * @param id the id for the client to be deleted
     * @throws BanqueException DATABASE_ERROR or CLIENT_NOT_FOUND
     */
    public void deleteClient(Long id) throws BanqueException;

    /**
     * Updates the client stored in the database identified by <client> overwriting
     * all the fields.
     * To update a client the password must be supplied in clear text.
     * @param client a persisted client (ie. must have a unique identifier)
     * @return the updated client
     * @throws BanqueException DATABASE_ERROR or CLIENT_NOT_FOUND
     */
    public Client updateClient(Client client) throws BanqueException;

    /**
     * Finds the client whose id is given
     * @param id the id of the client to search
     * @return the client or null if the client doesn't exist
     * @throws BanqueException DATABASE_ERROR
     */
    public Client findClient(Long id) throws BanqueException;

    /**
     * Finds all the clients of the Bank.
     * @return a list of all the clients ordered by id
     * @throws BanqueException DATABASE_ERROR
     */
    public List<Client> findAllClients() throws BanqueException;

    /**
     * Gets a list of all the clients with the partial name/last name
     * @param name a part of the client's name
     * @return a list of clients that comply to the query
     * @throws BanqueException DATABASE_ERROR
     */
    public List<Client> findClientsByName(String name) throws BanqueException;

    /**
     * Gets a list of all the clients with the partial name/last name
     * @param name a part of the client's name
     * @return a list of clients that comply to the query
     * @throws BanqueException DATABASE_ERROR
     */
    public List<Client> findClientsByLastName(String lastName) throws BanqueException;

    /**
     * Gets a list of all the clients that opened an account on the <date>
     * @param date Date to be used for search the clients
     * @return a list of clients that comply to the query
     * @throws BanqueException DATABASE_ERROR
     */
    public List<Client> findClientsByDateOfSubscription(Date date) throws BanqueException;

    /**
     * Used for logging in to the client's page. This method validates the login
     * of the client.
     * @param id the client's id to be processed
     * @param password the client's password (in clear text)
     * @return client that has been authenticated or null if authentication failed.
     * @throws BanqueException DATABASE_ERROR
     */
    public Client authenticateClient(Long id, String password) throws BanqueException;

    /**
     * Changes the client's password and updates the information on the database
     * @param id the client's id whose password will be changed
     * @param password the password in clear text
     * @return the updated client
     * @throws BanqueException DATABASE_ERROR or CLIENT_NOT_FOUND
     */
    public Client changePassword(Long id, String password) throws BanqueException;
    
    /**
     * Validates a client before trying to store it or update it on the database
     * @param client the client to be analyzed
     * @return true if its possible to save the client or an exception otherwise
     * @throws BanqueException
     */
    public boolean validateClient( Client client ) throws BanqueException;
}