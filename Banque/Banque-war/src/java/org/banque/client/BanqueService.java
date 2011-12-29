/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import org.banque.dtos.AccountDTO;
import org.banque.dtos.ClientDTO;
import org.banque.dtos.TransactionDTO;
import org.banque.exceptions.BanqueException;
import org.banque.managers.ClientManager;
import org.banque.managers.interfaces.IClientManagerLocal;

/**
 *
 * @author bjurkovski
 */
@RemoteServiceRelativePath("banqueservice")
public interface BanqueService extends RemoteService {
    public String[] login(String email, String password);
    
    public void logout();
    
    public void createClient(ClientDTO client) throws BanqueException;
    
    public void removeClient(Long clientId);
    
    public List<ClientDTO> findAllClients();
    
    public List<ClientDTO> findClientsByCriteria(String searchStr, int criteria);
    
    public void createAccount(Long ownerId, double initialBalance, boolean alertWhenNegative);
    
    public void removeAccount(Long accountId);
    
    public AccountDTO findAccount(Long accountId);
    
    public List<AccountDTO> findAccounts(List<Long> accountIds);
    
    public List<AccountDTO> findAllAccounts();
    
    public List<AccountDTO> findAccountsByCriteria(String searchStr, int criteria);
    
    public TransactionDTO makeTransaction(Long srcAccountId, Long dstAccountId, double amount);
    
    public List<TransactionDTO> findAllTransactions();
    
    public List<TransactionDTO> findTransactionsByCriteria(String searchStr, int criteria);
}
