/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.banque.dtos.ClientDTO;
import java.util.List;
import org.banque.dtos.AccountDTO;
import org.banque.dtos.TransactionDTO;
import org.banque.managers.ClientManager;
import org.banque.managers.interfaces.IClientManagerLocal;

/**
 *
 * @author bjurkovski
 */
public interface BanqueServiceAsync {
    public void createClient(ClientDTO client, AsyncCallback<Void> callback);
    
    public void removeClient(Long clientId, AsyncCallback<Void> callback);
    
    public void findAllClients(AsyncCallback<List<ClientDTO>> callback);
    
    public void findClientsByCriteria(String searchStr, int criteria, AsyncCallback<List<ClientDTO>> callback);
    
    public void createAccount(Long ownerId, AsyncCallback<Void> callback);
    
    public void removeAccount(Long accountId, AsyncCallback<Void> callback);
    
    public void findAccount(Long accountId, AsyncCallback<AccountDTO> callback);
    
    public void findAccounts(List<Long> accountIds, AsyncCallback<List<AccountDTO>> callback);
    
    public void findAllAccounts(AsyncCallback<List<AccountDTO>> callback);
    
    public void findAccountsByCriteria(String searchStr, int criteria, AsyncCallback<List<AccountDTO>> callback);
    
    public void makeTransaction(Long srcAccountId, Long dstAccountId, double amount, AsyncCallback<TransactionDTO> callback);
    
    public void findAllTransactions(AsyncCallback<List<TransactionDTO>> callback);
    
    public void findTransactionsByCriteria(String searchStr, int criteria, AsyncCallback<List<TransactionDTO>> callback);
}
