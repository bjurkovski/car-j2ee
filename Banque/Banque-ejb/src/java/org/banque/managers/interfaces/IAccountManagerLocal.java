package org.banque.managers.interfaces;

import java.util.List;
import javax.ejb.Local;
import org.banque.dtos.AccountDTO;
import org.banque.dtos.ClientDTO;
import org.banque.entities.Account;
import org.banque.entities.Transaction;
import org.banque.exceptions.BanqueException;

/**
 *
 * @author wasser
 */
@Local
public interface IAccountManagerLocal {

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
     * @throws BanqueException 
     */
    public List<AccountDTO> findAccounts(ClientDTO client) throws BanqueException;

    /**
     * Finds all the accounts in the system
     * @return a list with all the accounts
     * @throws BanqueException DATABASE_ERROR
     */
    public List<AccountDTO> findAllAccounts() throws BanqueException;

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

    /**
     * Finds the transaction with the given id
     * @param id the id of the transaction to be found
     * @return the transaction whose id was given or null if it could not been found
     * @throws BanqueException DATABASE_ERROR
     */
    public Transaction findTransaction(Long id) throws BanqueException;

    /**
     * Creates a new transaction. the amount will be transfer from the source to the destination.
     * The source account can end with a negative balance because of this action.
     * @param source the account from where the money will be debited
     * @param dest the account where the money will be credited
     * @param amount the amount of money that will be transfered. Positive value
     * @return The newly created transaction with all the fields set. The accounts
     * have already been updated to reflect the transaction
     * @throws BanqueException CLIENT_NOT_FOUND or DATABASE_ERROR
     */
    public Transaction makeTransaction(Account source, Account dest, double amount) throws BanqueException;

    /**
     * Deletes a transaction, returning the money to the owner and debiting it from
     * the destination.
     * @param id The id of the transaction to be deleted
     * @throws BanqueException DATABASE_ERROR or TRANSACTION_NOT_FOUND
     **/
    public void deleteTransaction(Long id) throws BanqueException;
}
