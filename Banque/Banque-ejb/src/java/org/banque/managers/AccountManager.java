package org.banque.managers;

import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.banque.dtos.AccountDTO;
import org.banque.dtos.ClientDTO;
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
        Client c = em.find(Client.class, owner.getId());
        if (c == null) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_NOT_FOUND);
        }
        Account account = new Account(alertWhenNegative);
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
            //accountDB.setTransactions(account.getTransactions());
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
            if (accounts != null) {
                for (Account a : accounts) {
                    toReturn.add(createAccountDTO(a));
                }
            }
            return toReturn;
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
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

//    @Override
//    public Transaction findTransaction(Long id) throws BanqueException {
//        try {
//            return em.find(Transaction.class, id);
//        } catch (Exception e) {
//            System.out.println("Original Error Message: " + e.getMessage());
//            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
//        }
//    }
//
//    @Override
//    public Transaction makeTransaction(Account source, Account dest, double amount) throws BanqueException {
//        Transaction transaction = new Transaction(amount, source, dest, new Date());
//
//        //This method can make one of the accounts negative.
//        if (source == null || dest == null || findAccount(source.getId()) == null || findAccount(dest.getId()) == null) {
//            throw new BanqueException(BanqueException.ErrorType.ACCOUNT_NOT_FOUND);
//        }
//        source.setBalance(source.getBalance() - amount);
//        dest.setBalance(dest.getBalance() + amount);
//        source.getTransactions().add(transaction);
//        dest.getTransactions().add(transaction);
//
//        try {
//            em.persist(transaction);
//        } catch (Exception e) {
//            System.out.println("Original Error Message: " + e.getMessage());
//            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
//        }
//
//        updateAccount(dest);
//        updateAccount(source);
//        if (source.isNegative() && source.isAlertWhenNegative()) {
//            alertNegative(source);
//        }
//
//        return transaction;
//    }
//
//    @Override
//    public void deleteTransaction(Long id) throws BanqueException {
//        Transaction transaction = findTransaction(id);
//        if (transaction == null) {
//            throw new BanqueException(BanqueException.ErrorType.TRANSACTION_NOT_FOUND);
//        }
//
//        //Makes the rollback
//        double amount = transaction.getAmount();
//        transaction.getSource().setBalance(transaction.getSource().getBalance() + amount);
//        transaction.getDestination().setBalance(transaction.getDestination().getBalance() - amount);
//        transaction.getSource().getTransactions().remove(transaction);
//        transaction.getDestination().getTransactions().remove(transaction);
//        try {
//            em.remove(em.merge(transaction));
//        } catch (Exception e) {
//            System.out.println("Original Error Message: " + e.getMessage());
//            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
//        }
//    }
    protected static AccountDTO createAccountDTO(Account account) {
        ClientDTO owner;
        if (ClientManager.clientMap.containsKey(account.getOwner().getId())) {
            owner = ClientManager.clientMap.get(account.getOwner().getId());
        } else {
            owner = ClientManager.createClientDTO(account.getOwner());
        }
        AccountDTO _return = new AccountDTO(owner, account.isAlertWhenNegative());
        _return.setBalance(account.getBalance());
        _return.setId(account.getId());
        return _return;
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
                + "has become negative, please... do something";

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

    @Override
    public Transaction findTransaction(Long id) throws BanqueException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Transaction makeTransaction(Account source, Account dest, double amount) throws BanqueException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteTransaction(Long id) throws BanqueException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}