package org.banque.managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.banque.dtos.ClientDTO;
import org.banque.entities.Client;
import org.banque.exceptions.BanqueException;
import org.banque.managers.interfaces.IClientManagerLocal;

/**
 *
 * @author wasser
 */
@Stateless
public class ClientManager extends PersonManager implements IClientManagerLocal {

    @PersistenceContext(unitName = "BanquePU")
    protected EntityManager em;

    @Override
    public ClientDTO createClient(ClientDTO client) throws BanqueException {
        return createClient(client.getName(), client.getLastName(), client.getPassword(), client.getGender(), client.getDateOfBirth(), client.getAddress(), client.getEmail(), client.isAdmin());
    }

    @Override
    public ClientDTO createClient(String name, String lastName, String password, ClientDTO.Gender gender, Date dateOfBirth, String address, String email) throws BanqueException {
        return createClient(name, lastName, password, gender, dateOfBirth, address, email, false);
    }

    @Override
    public ClientDTO createClient(String name, String lastName, String password, ClientDTO.Gender gender, Date dateOfBirth, String address, String email, boolean admin) throws BanqueException {
        Client clientDB = new Client(name, lastName, PersonManager.hashPassword(password), PersonManager.getGenderEntity(gender), dateOfBirth, address, email, admin);
        validateClient(clientDB);
        try {
            em.persist(clientDB);
            return createClientDTO(clientDB);
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public void deleteClient(Long id) throws BanqueException {
        Client client = findClientDB(id);
        try {
            em.remove(em.merge(client));
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public ClientDTO updateClient(ClientDTO client) throws BanqueException {
        Client clientDB = findClientDB(client.getId());


        try {
            clientDB.setAddress(client.getAddress());
            clientDB.setDateOfBirth(client.getDateOfBirth());
            clientDB.setGender(PersonManager.getGenderEntity(client.getGender()));
            clientDB.setLastName(client.getLastName());
            clientDB.setName(client.getName());
            clientDB.setPassword(client.getPassword());
            clientDB.setEmail(client.getEmail());
            clientDB.setAdmin(client.isAdmin());
            return createClientDTO(em.merge(clientDB));
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public ClientDTO findClient(Long id) throws BanqueException {
        return createClientDTO(findClientDB(id));
    }

    protected Client findClientDB(Long id) throws BanqueException {
        if (id == null) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_NOT_FOUND);
        }

        Client c = em.find(Client.class, id);
        if (c == null) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_NOT_FOUND);
        }
        return c;
    }

    @Override
    public List<ClientDTO> findAllClients() throws BanqueException {
        LinkedList<ClientDTO> _return = new LinkedList<ClientDTO>();
        try {
            List<Client> results = em.createNamedQuery(Client.FIND_ALL).getResultList();
            for (Client c : results) {
                _return.add(createClientDTO(c));
            }
            return _return;

        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }
    
    @Override
    public List<ClientDTO> findClientsByCriteria(String searchString, int criteria) throws BanqueException {
        switch(criteria) {
            case ID:
                List<ClientDTO> _return = new LinkedList<ClientDTO>();
                _return.add(findClient(Long.decode(searchString)));
                return _return;
            case PRENOM:
                return findClientsByName(searchString);
            case NOM:
                return findClientsByLastName(searchString);
            case EMAIL:
                return findClientsByEmail(searchString);
        }
        
        throw new BanqueException(BanqueException.ErrorType.INVALID_SEARCH_CRITERIA);
    }

    @Override
    public List<ClientDTO> findClients(String searchString) throws BanqueException {
        List<ClientDTO> _return = new LinkedList<ClientDTO>();
        try {
            Query query = em.createNamedQuery(Client.FIND_PARTIALLY).setParameter("partial", "%" + searchString + "%");
            List<Client> results = query.getResultList();
            for (Client c : results) {
                _return.add(createClientDTO(c));
            }
            return _return;
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }
    
    @Override
    public List<ClientDTO> findClientsByName(String searchString) throws BanqueException {
        List<ClientDTO> _return = new LinkedList<ClientDTO>();
        try {
            Query query = em.createNamedQuery(Client.FIND_BY_NAME).setParameter("name", "%" + searchString + "%");
            List<Client> results = query.getResultList();
            for (Client c : results) {
                _return.add(createClientDTO(c));
            }
            return _return;
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }
    
    @Override
    public List<ClientDTO> findClientsByLastName(String searchString) throws BanqueException {
        List<ClientDTO> _return = new LinkedList<ClientDTO>();
        try {
            Query query = em.createNamedQuery(Client.FIND_BY_LAST_NAME).setParameter("lastName", "%" + searchString + "%");
            List<Client> results = query.getResultList();
            for (Client c : results) {
                _return.add(createClientDTO(c));
            }
            return _return;
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }
    
    @Override
    public List<ClientDTO> findClientsByEmail(String searchString, boolean exactMatch) throws BanqueException {
        List<ClientDTO> _return = new LinkedList<ClientDTO>();
        try {
            Query query;
            if(exactMatch)
                query = em.createNamedQuery(Client.FIND_BY_EMAIL_EQUAL).setParameter("email", searchString);
            else
                query = em.createNamedQuery(Client.FIND_BY_EMAIL).setParameter("email", "%" + searchString + "%");
            List<Client> results = query.getResultList();
            for (Client c : results) {
                _return.add(createClientDTO(c));
            }
            return _return;
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }
    
    @Override
    public List<ClientDTO> findClientsByEmail(String searchString) throws BanqueException {
        return findClientsByEmail(searchString, false);
    }

    @Override
    public List<ClientDTO> findClientsByDateOfSubscription(Date date) throws BanqueException {
        LinkedList<ClientDTO> _return = new LinkedList<ClientDTO>();
        try {
            Query query = em.createNamedQuery(Client.FIND_BY_SUBSCRIPTION_DATE).setParameter("subsdate", date);
            List<Client> results = query.getResultList();
            for (Client c : results) {
                _return.add(createClientDTO(c));
            }
            return _return;
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public ClientDTO authenticateClient(Long id, String password) throws BanqueException {
        ClientDTO client = findClient(id);
        if (PersonManager.hashPassword(password).equals(client.getPassword())) {
            return client;
        }
        return null;
    }

    @Override
    public ClientDTO changePassword(Long id, String password) throws BanqueException {
        ClientDTO client = findClient(id);
        client.setPassword(PersonManager.hashPassword(password));
        return updateClient(client);
    }

    protected static ClientDTO createClientDTO(Client client) {
        ClientDTO clientDTO = new ClientDTO(client.getName(), client.getLastName(), client.getPassword(), PersonManager.getGenderDTO(client.getGender()), client.getDateOfBirth(), client.getAddress(), client.getEmail(), client.isAdmin());
        clientDTO.setId(client.getId());
        return clientDTO;
    }

    /**
     * Validates a client before trying to store it or update it on the database
     * @param client the client to be analyzed
     * @return true if its possible to save the client or an exception otherwise
     * @throws BanqueException
     */
    protected boolean validateClient(Client client) throws BanqueException {
        //Fields cannot be null
        if (client.getName() == null || client.getName().isEmpty()) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_NULL_NAME);
        }
        if (client.getLastName() == null || client.getLastName().isEmpty()) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_NULL_LAST_NAME);
        }
        if (client.getPassword() == null || client.getPassword().isEmpty()) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_NULL_PASSWORD);
        }
        if (client.getAddress() == null || client.getAddress().isEmpty()) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_NULL_ADDRESS);
        }
        if (client.getDateOfBirth() == null) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_NULL_DATE_OF_BIRTH);
        }
        if (client.getEmail() == null || client.getEmail().isEmpty()) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_NULL_EMAIL);
        }
        
        List<ClientDTO> lc = null;
        try {
            lc = findClientsByEmail(client.getEmail(), true);
        } catch(BanqueException e) {
        }
        
        if(lc != null && lc.size() > 0) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_EMAIL_ALREADY_USED);
        }
        
        ClientDTO c = null;
        try {
            c = findClient(client.getId());
        } catch(BanqueException e) {
        }
        
        //ID cannot exist and if it exists, must be the same as us
        if (c != null && c.getId() != client.getId()) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_ID_ALREADY_EXISTS);
        }

        //Email needs to have an arroba and a domain 
        Pattern pattern = Pattern.compile("\\w+@\\w+\\.\\w+");
        Matcher matcher = pattern.matcher(client.getEmail());
        if (!matcher.find()) {
            throw new BanqueException(BanqueException.ErrorType.INVALID_EMAIL);
        }

        return true;
    }
}
