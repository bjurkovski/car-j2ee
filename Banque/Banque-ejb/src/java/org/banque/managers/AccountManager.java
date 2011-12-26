package org.banque.managers;

import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.banque.dtos.AccountDTO;
import org.banque.dtos.ClientDTO;
import org.banque.dtos.TransactionDTO;
import org.banque.entities.Account;
import org.banque.entities.Client;
import org.banque.entities.Transaction;
import org.banque.exceptions.BanqueException;
import org.banque.managers.interfaces.IAccountManagerLocal;
import org.banque.utils.Email;

/**
 *
 * @author wasser
 */
@Stateless
public class AccountManager implements IAccountManagerLocal {

    @PersistenceContext(unitName = "BanquePU")
    protected EntityManager em;

    @Override
    public AccountDTO createAccount(ClientDTO owner) throws BanqueException {
        return createAccount(owner, false);
    }

    @Override
    public AccountDTO createAccount(ClientDTO owner, boolean alertWhenNegative) throws BanqueException {
        return createAccount(owner, 0, alertWhenNegative);
    }

    @Override
    public AccountDTO createAccount(ClientDTO owner, double balance, boolean alertWhenNegative) throws BanqueException {
        Client c = em.find(Client.class, owner.getId());
        if (c == null) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_NOT_FOUND);
        }
        Account account = new Account(balance, alertWhenNegative);
        account.setOwner(c);
        try {
            em.persist(account);
            c.getAccounts().add(account);
            c = em.merge(c);
            return createAccountDTO(account);
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public AccountDTO updateAccount(AccountDTO account) throws BanqueException {
        Account accountDB = findAccountDB(account.getId());
        try {
            accountDB.setBalance(account.getBalance());
            accountDB.setAlertWhenNegative(account.isAlertWhenNegative());
            return createAccountDTO(em.merge(accountDB));
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public void deleteAccount(Long id) throws BanqueException {
        Account account = findAccountDB(id);
        try {
            em.remove(em.merge(account));
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public AccountDTO findAccount(Long id) throws BanqueException {
        return createAccountDTO(findAccountDB(id));
    }

    protected Account findAccountDB(Long id) throws BanqueException {
        if (id == null) {
            throw new BanqueException(BanqueException.ErrorType.ACCOUNT_NOT_FOUND);
        }

        Account a = em.find(Account.class, id);
        if (a != null) {
            return a;
        } else {
            throw new BanqueException(BanqueException.ErrorType.ACCOUNT_NOT_FOUND);
        }
    }

    @Override
    public List<AccountDTO> findAllAccounts() throws BanqueException {
        List<AccountDTO> toReturn = new LinkedList<AccountDTO>();
        try {
            List<Account> accounts = em.createNamedQuery(Account.FIND_ALL).getResultList();
            for (Account a : accounts) {
                toReturn.add(createAccountDTO(a));
            }
            return toReturn;
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public List<AccountDTO> findAccountsByCriteria(String searchString, int criteria) throws BanqueException {
        switch (criteria) {
            case IAccountManagerLocal.BALANCE_NEGATIVE:
                return findNegativeBalanceAccounts();
            case IAccountManagerLocal.BALANCE_POSITIVE:
                return findPositiveBalanceAccounts();
            case IAccountManagerLocal.NOM_CLIENT:
                return findAccountsByOwnerLastName(searchString);
            case IAccountManagerLocal.PRENOM_CLIENT:
                return findAccountsByOwnerName(searchString);
            case IAccountManagerLocal.ID:
                List<AccountDTO> _return = new LinkedList<AccountDTO>();
                _return.add(findAccount(Long.valueOf(searchString)));
                return _return;
        }

        throw new BanqueException(BanqueException.ErrorType.INVALID_SEARCH_CRITERIA);
    }

    @Override
    public List<AccountDTO> findNegativeBalanceAccounts() throws BanqueException {
        List<AccountDTO> toReturn = new LinkedList<AccountDTO>();
        try {
            List<Account> accounts = em.createNamedQuery(Account.FIND_NEGATIVE).getResultList();
            for (Account a : accounts) {
                toReturn.add(createAccountDTO(a));
            }
            return toReturn;
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public List<AccountDTO> findPositiveBalanceAccounts() throws BanqueException {
        List<AccountDTO> toReturn = new LinkedList<AccountDTO>();
        try {
            List<Account> accounts = em.createNamedQuery(Account.FIND_POSITIVE).getResultList();
            for (Account a : accounts) {
                toReturn.add(createAccountDTO(a));
            }
            return toReturn;
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public List<AccountDTO> findAccountsByOwnerName(String name) throws BanqueException {
        List<AccountDTO> toReturn = new LinkedList<AccountDTO>();
        try {
            List<Account> accounts = em.createNamedQuery(Account.FIND_BY_NAME).setParameter("name", "%" + name + "%").getResultList();
            for (Account a : accounts) {
                toReturn.add(createAccountDTO(a));
            }
            return toReturn;
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public List<AccountDTO> findAccountsByOwnerLastName(String lastName) throws BanqueException {
        List<AccountDTO> toReturn = new LinkedList<AccountDTO>();
        try {
            List<Account> accounts = em.createNamedQuery(Account.FIND_BY_LAST_NAME).setParameter("lastName", "%" + lastName + "%").getResultList();
            for (Account a : accounts) {
                toReturn.add(createAccountDTO(a));
            }
            return toReturn;
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public TransactionDTO findTransaction(Long id) throws BanqueException {
        return createTransactionDTO(findTransactionDB(id));
    }

    protected Transaction findTransactionDB(Long id) throws BanqueException {
        if (id == null) {
            throw new BanqueException(BanqueException.ErrorType.TRANSACTION_NOT_FOUND);
        }
        Transaction t = em.find(Transaction.class, id);

        if (t != null) {
            return t;
        } else {
            throw new BanqueException(BanqueException.ErrorType.TRANSACTION_NOT_FOUND);
        }
    }

    @Override
    public List<TransactionDTO> findTransactions(AccountDTO a) throws BanqueException {
        List<TransactionDTO> toReturn = new LinkedList<TransactionDTO>();
        Account account = findAccountDB(a.getId());

        for (Transaction t : account.getTransactions()) {
            toReturn.add(createTransactionDTO(t));
        }
        return toReturn;
    }

    @Override
    public List<TransactionDTO> findAllTransactions() throws BanqueException {
        List<TransactionDTO> toReturn = new LinkedList<TransactionDTO>();
        try {
            List<Transaction> transactions = em.createNamedQuery(Transaction.FIND_ALL).getResultList();
            for (Transaction t : transactions) {
                toReturn.add(createTransactionDTO(t));
            }
            return toReturn;
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public List<TransactionDTO> findTransactionsByCriteria(String searchStr, int criteria) throws BanqueException {
        switch (criteria) {
            case IAccountManagerLocal.ID:
                LinkedList<TransactionDTO> _return = new LinkedList<TransactionDTO>();
                _return.add(findTransaction(Long.valueOf(searchStr)));
                return _return;
            case IAccountManagerLocal.NOM_CLIENT:
                return findTransactionsByOwnerLastName(searchStr);
            case IAccountManagerLocal.PRENOM_CLIENT:
                return findTransactionsByOwnerName(searchStr);
        }

        throw new BanqueException(BanqueException.ErrorType.INVALID_SEARCH_CRITERIA);
    }

    @Override
    public List<TransactionDTO> findTransactionsByOwnerName(String name) throws BanqueException {
        List<TransactionDTO> toReturn = new LinkedList<TransactionDTO>();
        try {
            List<Transaction> transactions = em.createNamedQuery(Transaction.FIND_BY_CLIENT_NAME).setParameter("name", "%" + name + "%").getResultList();
            for (Transaction t : transactions) {
                toReturn.add(createTransactionDTO(t));
            }
            return toReturn;
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public List<TransactionDTO> findTransactionsByOwnerLastName(String lastName) throws BanqueException {
        List<TransactionDTO> toReturn = new LinkedList<TransactionDTO>();
        try {
            List<Transaction> transactions = em.createNamedQuery(Transaction.FIND_BY_CLIENT_LAST_NAME).setParameter("lastName", "%" + lastName + "%").getResultList();
            for (Transaction t : transactions) {
                toReturn.add(createTransactionDTO(t));
            }
            return toReturn;
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public TransactionDTO makeTransaction(TransactionDTO t) throws BanqueException {
        return makeTransaction(t.getSource(), t.getDestination(), t.getAmount());
    }

    @Override
    public TransactionDTO makeTransaction(Long sourceId, Long dstId, double amount) throws BanqueException {
        AccountDTO src = findAccount(sourceId);
        AccountDTO dst = findAccount(dstId);
        return makeTransaction(src, dst, amount);
    }

    @Override
    public TransactionDTO makeTransaction(AccountDTO source, AccountDTO dest, double amount) throws BanqueException {
        Account src;
        Account dst;
        if (amount == 0) {
            throw new BanqueException(BanqueException.ErrorType.TRANSACTION_AMOUNT_INVALID);
        }
        if (source == dest) {
            throw new BanqueException(BanqueException.ErrorType.TRANSACTION_ACCOUNTS_EQUAL);
        }
        if (amount > 0) {
            src = findAccountDB(source.getId());
            dst = findAccountDB(dest.getId());
        } else {
            src = findAccountDB(dest.getId());
            dst = findAccountDB(source.getId());
        }
        amount = Math.abs(amount);
        Transaction t = new Transaction(amount, src, dst);
        src.setBalance(src.getBalance() - amount);
        dst.setBalance(dst.getBalance() + amount);

        if (src.isNegative() && src.isAlertWhenNegative()) {
            alertNegative(src);
        }

        try {
            em.persist(t);
            src.getTransactions().add(t);
            src = em.merge(src);
            dst.getTransactions().add(t);
            dst = em.merge(dst);
            return createTransactionDTO(t);
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public void deleteTransaction(Long id) throws BanqueException {
        Transaction t = findTransactionDB(id);
        Account src = t.getSource();
        Account dst = t.getDestination();
        try {
            src.getTransactions().remove(t);
            dst.getTransactions().remove(t);
            src.setBalance(src.getBalance() + t.getAmount());
            dst.setBalance(dst.getBalance() - t.getAmount());
            em.remove(em.merge(t));
            em.merge(src);
            em.merge(dst);
            if (dst.isNegative() && dst.isAlertWhenNegative()) {
                alertNegative(dst);
            }
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    protected static AccountDTO createAccountDTO(Account account) {
        ClientDTO owner = ClientManager.createClientDTO(account.getOwner());
        AccountDTO _return = new AccountDTO(owner, account.isAlertWhenNegative());
        _return.setBalance(account.getBalance());
        _return.setId(account.getId());
        return _return;
    }

    protected static TransactionDTO createTransactionDTO(Transaction t) {
        TransactionDTO toReturn = new TransactionDTO(t.getAmount(), createAccountDTO(t.getSource()), createAccountDTO(t.getDestination()));
        toReturn.setDate(t.getDate());
        toReturn.setId(t.getId());
        return toReturn;
    }

    @Override
    public List<AccountDTO> findAccounts(ClientDTO client) throws BanqueException {
        Client c = em.find(Client.class, client.getId());
        if (c == null) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_NOT_FOUND);
        }
        return createAccountsDTO(c.getAccounts());
    }

    /**
     * Accessibility method to convert various accounts from the same owner
     * @param accounts Accounts to be converted to DTOs
     * @param owner the owner of all the accounts
     * @return a list with all the accounts converted.
     */
    protected static List<AccountDTO> createAccountsDTO(List<Account> accounts) {
        List<AccountDTO> toReturn = new LinkedList<AccountDTO>();

        for (Account a : accounts) {
            toReturn.add(createAccountDTO(a));
        }
        return toReturn;
    }

    /**
     * Sends an email to the user alerting him that his balance is negative.
     * This method executes in asynchronously.
     * @param account that has become negative
     */
    protected void alertNegative(Account account) {
        Client owner = account.getOwner();
        final String to = owner.getEmail();
        final String subject = "Alert From Banque EJB";
        final String content = "Dear " + owner.getName() + " " + owner.getLastName()
                + "\n Unfortunately, the balance in your account number " + account.getId()
                + "is negative, please... do something";

        Runnable sendEmail = new Runnable() {

            @Override
            public void run() {
                try {
                    Email.sendEmail(to, subject, content);
                } catch (Exception e) {
                    System.out.println("Error Sending the email: " + e.getMessage());
                }
            }
        };
        sendEmail.run();
    }
}
