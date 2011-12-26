/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.banque.client.BanqueService;
import org.banque.dtos.AccountDTO;
import org.banque.dtos.ClientDTO;
import org.banque.dtos.TransactionDTO;
import org.banque.entities.Person;
import org.banque.exceptions.BanqueException;
import org.banque.managers.ClientManager;
import org.banque.managers.interfaces.IAccountManagerLocal;
import org.banque.managers.interfaces.IClientManagerLocal;

/**
 *
 * @author bjurkovski
 */
public class BanqueServiceImpl extends RemoteServiceServlet implements BanqueService {
    @EJB
    IClientManagerLocal clientManager;
    
    @EJB
    IAccountManagerLocal accountManager;

    @Override
    public void createClient(ClientDTO client) throws BanqueException {
        clientManager.createClient(client);
    }
    
    @Override
    public void removeClient(Long clientId) {
        try {
            clientManager.deleteClient(clientId);
        } catch (BanqueException ex) {
        }
    }
    
    @Override
    public List<ClientDTO> findAllClients() {
        try {
            return clientManager.findAllClients();
        } catch (BanqueException ex) {
            return null;
        }
    }
    
    @Override
    public List<ClientDTO> findClientsByCriteria(String searchStr, int criteria) {
        try {
            return clientManager.findClientsByCriteria(searchStr, criteria);
        } catch (BanqueException ex) {
            return null;
        }
    }
    
    @Override
    public void createAccount(Long ownerId) {
        try {
            ClientDTO owner = clientManager.findClient(ownerId);
            accountManager.createAccount(owner);
        } catch (BanqueException ex) {
        }
    }
    
    @Override
    public void removeAccount(Long accountId) {
        try {
            accountManager.deleteAccount(accountId);
        } catch (BanqueException ex) {
            Logger.getLogger(BanqueServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public AccountDTO findAccount(Long accountId) {
        try {
            return accountManager.findAccount(accountId);
        } catch (BanqueException ex) {
            return null;
        }
    }
    
    @Override
    public List<AccountDTO> findAccounts(List<Long> accountIds) {
        ArrayList<AccountDTO> accounts = new ArrayList<AccountDTO>();
        try {
            for(Long id : accountIds) {
                accounts.add(accountManager.findAccount(id));
            }
            return accounts;
        } catch(BanqueException ex) {
            return null;
        }
    }
    
    @Override
    public List<AccountDTO> findAllAccounts() {
        try {
            return accountManager.findAllAccounts();
        } catch (BanqueException ex) {
            return null;
        }
    }
    
    @Override
    public List<AccountDTO> findAccountsByCriteria(String searchStr, int criteria) {
        try {
            return accountManager.findAccountsByCriteria(searchStr, criteria);
        } catch (BanqueException ex) {
            return null;
        }
    }
    
    public TransactionDTO makeTransaction(Long srcAccountId, Long dstAccountId, double amount) {
        try {
            return accountManager.makeTransaction(srcAccountId, dstAccountId, amount);
        } catch (BanqueException ex) {
            return null;
        }
    }
    
    public List<TransactionDTO> findAllTransactions() {
        try {
            return accountManager.findAllTransactions();
        } catch (BanqueException ex) {
            return null;
        }
    }
    
    public List<TransactionDTO> findTransactionsByCriteria(String searchStr, int criteria) {
        try {
            return accountManager.findTransactionsByCriteria(searchStr, criteria);
        } catch (BanqueException ex) {
            return null;
        }
    }
}
