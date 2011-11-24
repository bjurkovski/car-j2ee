package org.banque.managers;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.banque.entities.Client;
import org.banque.entities.Person.Gender;
import org.banque.exceptions.BanqueException;
import org.banque.managers.interfaces.IClientManagerLocal;

/**
 *
 * @author wasser
 */
@Stateless
public class ClientManager implements IClientManagerLocal {

    @PersistenceContext(unitName = "BanquePU")
    private EntityManager em;

    @Override
    public Client createClient(Client client) throws BanqueException {
        return createClient(client.getName(), client.getLastName(), client.getPassword(), client.getGender(), client.getDateOfBirth(), client.getAddress(), client.getEmail());
    }

    @Override
    public Client createClient(String name, String lastName, String password, Gender gender, Date dateOfBirth, String address, String email) throws BanqueException {
        Client clientDB = new Client(name, lastName, PersonManager.hashPassword(password), gender, dateOfBirth, address, email);
        try {
            em.persist(clientDB);
            return clientDB;
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public void deleteClient(Long id) throws BanqueException {
        Client client = findClient(id);
        if (client == null) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_NOT_FOUND);
        }
        try {
            em.remove(em.merge(client));
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public Client updateClient(Client client) throws BanqueException {
        if (client == null) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_NOT_FOUND);
        }
        Client clientDB = findClient(client.getId());
        if (clientDB == null) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_NOT_FOUND);
        }
        try {
            clientDB.setAccounts(client.getAccounts());
            clientDB.setAddress(client.getAddress());
            clientDB.setDateOfBirth(client.getDateOfBirth());
            clientDB.setGender(client.getGender());
            clientDB.setLastName(client.getLastName());
            clientDB.setName(client.getName());
            clientDB.setPassword(client.getPassword());
            clientDB.setEmail(client.getEmail());
            return em.merge(clientDB);
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public Client findClient(Long id) throws BanqueException {
        try {
            return em.find(Client.class, id);
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public List<Client> findAllClients() throws BanqueException {
        try {
            Query query = em.createNamedQuery(Client.FIND_ALL);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public List<Client> findClientsByName(String name) throws BanqueException {
        try {
            Query query = em.createNamedQuery("findClientsByName");
            query.setParameter("name", name);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public List<Client> findClientsByLastName(String lastName) throws BanqueException {
        try {
            Query query = em.createNamedQuery("findClientsByLastName");
            query.setParameter("lastName", lastName);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public List<Client> findClientsByDateOfSubscription(Date date) throws BanqueException {
        try {
            Query query = em.createNamedQuery("findClientsBySubscriptionDate");
            query.setParameter("subsdate", date);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public Client authenticateClient(Long id, String password) throws BanqueException {
        Client client = findClient(id);
        if (client != null) {
            if (PersonManager.hashPassword(password).equals(client.getPassword())) {
                return client;
            }
        }
        return null;
    }

    @Override
    public Client changePassword(Long id, String password) throws BanqueException {
        Client client = findClient(id);

        if (client == null) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_NOT_FOUND);
        }
        client.setPassword(PersonManager.hashPassword(password));
        return updateClient(client);
    }

    @Override
    public boolean validateClient(Client client) throws BanqueException {
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
        //ID cannot exist and if it exists, must be the same as us
        if (findClient(client.getId()) != null && findClient(client.getId()).getId() != client.getId()) {
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
