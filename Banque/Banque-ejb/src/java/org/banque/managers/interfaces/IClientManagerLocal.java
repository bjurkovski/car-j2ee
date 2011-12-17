/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.managers.interfaces;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import org.banque.dtos.ClientDTO;
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
    public ClientDTO createClient(ClientDTO client) throws BanqueException;

    /**
     * Creates a new client in the database assigning him an id that will not be an administrator
     * @param password - in clear text
     * @return the persisted client with a unique id
     * @throws BanqueException DATABASE_ERROR
     */
    public ClientDTO createClient(String name, String lastName, String password, ClientDTO.Gender gender, Date dateOfBirth, String address, String email) throws BanqueException;

    /**
     * Creates a new client with the option to make him an admin
     * @param password - in clear text
     * @return the persisted client with a unique id
     * @throws BanqueException DATABASE_ERROR     
     */
    public ClientDTO createClient(String name, String lastName, String password, ClientDTO.Gender gender, Date dateOfBirth, String address, String email, boolean admin) throws BanqueException;

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
    public ClientDTO updateClient(ClientDTO client) throws BanqueException;

    /**
     * Finds the client whose id is given
     * @param id the id of the client to search
     * @return the client or null if the client doesn't exist
     * @throws BanqueException DATABASE_ERROR
     */
    public ClientDTO findClient(Long id) throws BanqueException;

    /**
     * Finds all the clients of the Bank.
     * @return a list of all the clients ordered by id
     * @throws BanqueException DATABASE_ERROR
     */
    public List<ClientDTO> findAllClients() throws BanqueException;

    /**
     * Gets a list of all the clients with the partial name/last name
     * @param searchString a part of the client's name or last name
     * @return a list of clients that comply to the query
     * @throws BanqueException DATABASE_ERROR
     */
    public List<ClientDTO> findClients(String searchString) throws BanqueException;

    /**
     * Gets a list of all the clients that opened an account on the <date>
     * @param date Date to be used for search the clients
     * @return a list of clients that comply to the query
     * @throws BanqueException DATABASE_ERROR
     */
    public List<ClientDTO> findClientsByDateOfSubscription(Date date) throws BanqueException;

    /**
     * Used for logging in to the client's page. This method validates the login
     * of the client.
     * @param id the client's id to be processed
     * @param password the client's password (in clear text)
     * @return client that has been authenticated or null if authentication failed.
     * @throws BanqueException DATABASE_ERROR
     */
    public ClientDTO authenticateClient(Long id, String password) throws BanqueException;

    /**
     * Changes the client's password and updates the information on the database
     * @param id the client's id whose password will be changed
     * @param password the password in clear text
     * @return the updated client
     * @throws BanqueException DATABASE_ERROR or CLIENT_NOT_FOUND
     */
    public ClientDTO changePassword(Long id, String password) throws BanqueException;
}