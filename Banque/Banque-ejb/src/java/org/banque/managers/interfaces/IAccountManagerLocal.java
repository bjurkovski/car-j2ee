package org.banque.managers.interfaces;

import java.util.List;
import javax.ejb.Local;
import org.banque.dtos.AccountDTO;
import org.banque.dtos.ClientDTO;
import org.banque.dtos.TransactionDTO;
import org.banque.exceptions.BanqueException;

/**
 *
 * @author wasser
 */
@Local
public interface IAccountManagerLocal {
    public final static int ID = 0;
    public final static int NOM_CLIENT = 1;
    public final static int PRENOM_CLIENT = 2;
    public final static int BALANCE_POSITIVE = 3;
    public final static int BALANCE_NEGATIVE = 4;
    public final static int ALL = 6;

    /**
     * Creates a new account in the database
     * @param owner The client that owns an account
     * @return the newly created account
     * @throws BanqueException CLIENT_NOT_FOUND or DATABASE_ERROR
     **/
    public AccountDTO createAccount(ClientDTO owner) throws BanqueException;

    /**
     * Creates a new account in the database
     * @param owner The client that owns an account
     * @param alertWhenNegative Alert the client via email (if supplied) when the
     * account is negative
     * @return the newly created account
     * @throws BanqueException CLIENT_NOT_FOUND or DATABASE_ERROR
     **/
    public AccountDTO createAccount(ClientDTO owner, boolean alertWhenNegative) throws BanqueException;

    /**
     * Updates a new account with the newly given information
     * @param account an already existing account (ie. Needs to have an id set)
     * @return the updated account
     * @throws BanqueException CLIENT_NOT_FOUND or DATABASE_ERROR
     */
    public AccountDTO updateAccount(AccountDTO account) throws BanqueException;

    /**
     * Deletes an account from the database from the database
     * @param id the id of the account to be deleted.
     * @throws BanqueException ACCOUNT_NOT_FOUND or DATABASE_ERROR
     */
    public void deleteAccount(Long id) throws BanqueException;

    /**
     * Finds the account with the given id
     * @param id the id of the account to be searched
     * @return the account with the given id or null if it can not be found
     * @throws BanqueException ACCOUNT_NOT_FOUND
     */
    public AccountDTO findAccount(Long id) throws BanqueException;

    /**
     * Finds all the accounts of a client
     * @param client the client whose accounts will be searched
     * @return a list of accounts of the client
     * @throws BanqueException CLIENT_NOT_FOUND
     */
    public List<AccountDTO> findAccounts(ClientDTO client) throws BanqueException;

    /**
     * Finds all the accounts in the system
     * @return a list with all the accounts
     * @throws BanqueException DATABASE_ERROR
     */
    public List<AccountDTO> findAllAccounts() throws BanqueException;
    
    public List<AccountDTO> findAccountsByCriteria(String searchString, int criteria) throws BanqueException;
    
    /**
     * Finds all the accounts in the system that have a negative balance
     * @return a list with the accounts
     * @throws BanqueException DATABASE_ERROR
     */
    public List<AccountDTO> findNegativeBalanceAccounts() throws BanqueException;

    /**
     * Finds all the accounts in the system that have a positive (>=0) balance
     * @return a list with the accounts
     * @throws BanqueException DATABASE_ERROR
     */
    public List<AccountDTO> findPositiveBalanceAccounts() throws BanqueException;
    
    public List<AccountDTO> findAccountsByOwnerName(String name) throws BanqueException;
    
    public List<AccountDTO> findAccountsByOwnerLastName(String lastName) throws BanqueException;

    /**
     * Finds the transaction with the given id
     * @param id the id of the transaction to be found
     * @return the transaction whose id was given
     * @throws BanqueException TRANSACTION_NOT_FOUND
     */
    public TransactionDTO findTransaction(Long id) throws BanqueException;

    /**
     * Finds the transactions associated with an account
     * @param a the account related to the transactions
     * @return A list of transactions related to the account a
     * @throws BanqueException ACCOUNT_NOT_FOUND
     */
    public List<TransactionDTO> findTransactions(AccountDTO a) throws BanqueException;

    /**
     * Finds all the transactions in the system
     * @return a list with all the transactions
     * @throws BanqueException DATABASE_ERROR
     */
    public List<TransactionDTO> findAllTransactions() throws BanqueException;
    
    public List<TransactionDTO> findTransactionsByCriteria(String searchStr, int criteria) throws BanqueException;
    
    public List<TransactionDTO> findTransactionsByOwnerName(String name) throws BanqueException;
    
    public List<TransactionDTO> findTransactionsByOwnerLastName(String lastName) throws BanqueException;

    /**
     * Creates a new transaction. the amount will be transfer from the source to the destination.
     * The source account can end with a negative balance because of this action.
     * If the amount is negative, src and dst will be inverted
     * @param source the account from where the money will be debited
     * @param dest the account where the money will be credited
     * @param amount the amount of money that will be transfered. (!0)
     * @return The newly created transaction. The related accounts
     * have been updated to reflect the transaction
     * @throws BanqueException CLIENT_NOT_FOUND, DATABASE_ERROR, TRANSACTION_AMOUNT_INVALID or TRANSACTION_ACCOUNTS_EQUAL
     */
    public TransactionDTO makeTransaction(AccountDTO source, AccountDTO dest, double amount) throws BanqueException;

    /**
     * Creates a new transaction. the amount will be transfer from the source to the destination.
     * The source account can end with a negative balance because of this action.
     * If the amount is negative, src will have the money credited and dst the money debited and
     * the transaction will be saved in a standard way
     * @return The newly created transaction. The related accounts
     * have been updated to reflect the transaction
     * @throws BanqueException CLIENT_NOT_FOUND, DATABASE_ERROR, TRANSACTION_AMOUNT_INVALID or TRANSACTION_ACCOUNTS_EQUAL
     */
    public TransactionDTO makeTransaction(TransactionDTO t) throws BanqueException;

    public TransactionDTO makeTransaction(Long sourceId, Long dstId, double amount) throws BanqueException;
    
    /**
     * Deletes a transaction, returning the money to the owner and debiting it from
     * the destination.
     * @param id The id of the transaction to be deleted
     * @throws BanqueException DATABASE_ERROR or TRANSACTION_NOT_FOUND
     **/
    public void deleteTransaction(Long id) throws BanqueException;
}
