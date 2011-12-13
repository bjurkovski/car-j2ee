package org.banque.managers;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    private EntityManager em;

    @Override
    public Account createAccount(Client owner) throws BanqueException {
        return createAccount(owner, false);
    }

    @Override
    public Account createAccount(Client owner, boolean alertWhenNegative) throws BanqueException {
        if (owner == null) {
            throw new BanqueException(BanqueException.ErrorType.CLIENT_NOT_FOUND);
        }

        Account account = new Account(owner, alertWhenNegative);
        try {
//            owner.getAccounts().add(account);
            em.persist(account);
            return account;
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public Account updateAccount(Account account) throws BanqueException {
        if (account == null) {
            throw new BanqueException(BanqueException.ErrorType.ACCOUNT_NOT_FOUND);
        }

        Account accountDB = findAccount(account.getId());
        if (accountDB == null) {
            throw new BanqueException(BanqueException.ErrorType.ACCOUNT_NOT_FOUND);
        }
        try {
            accountDB.setBalance(account.getBalance());
            accountDB.setAlertWhenNegative(account.isAlertWhenNegative());
            accountDB.setTransactions(account.getTransactions());
            return em.merge(accountDB);
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public void deleteAccount(Long id) throws BanqueException {
        Account account = findAccount(id);
        if (account == null) {
            throw new BanqueException(BanqueException.ErrorType.ACCOUNT_NOT_FOUND);
        }
        try {
            Client owner = account.getOwner();
//            owner.getAccounts().remove(account);
            em.remove(em.merge(account));
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public Account findAccount(Long id) throws BanqueException {
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
    public List<Account> findAllAccounts() throws BanqueException {
        try {
            return em.createNamedQuery(Account.FIND_ALL).getResultList();
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public List<Account> findNegativeBalanceAccounts() throws BanqueException {
        try {
            return em.createNamedQuery(Account.FIND_NEGATIVE).getResultList();
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public List<Account> findPositiveBalanceAccounts() throws BanqueException {
        try {
            return em.createNamedQuery(Account.FIND_POSITIVE).getResultList();
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public Transaction findTransaction(Long id) throws BanqueException {
        try {
            return em.find(Transaction.class, id);
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
    }

    @Override
    public Transaction makeTransaction(Account source, Account dest, double amount) throws BanqueException {
        Transaction transaction = new Transaction(amount, source, dest, new Date());

        //This method can make one of the accounts negative.
        if (source == null || dest == null || findAccount(source.getId()) == null || findAccount(dest.getId()) == null) {
            throw new BanqueException(BanqueException.ErrorType.ACCOUNT_NOT_FOUND);
        }
        source.setBalance(source.getBalance() - amount);
        dest.setBalance(dest.getBalance() + amount);
        source.getTransactions().add(transaction);
        dest.getTransactions().add(transaction);

        try {
            em.persist(transaction);
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }

        updateAccount(dest);
        updateAccount(source);
        if (source.isNegative() && source.isAlertWhenNegative()) {
            alertNegative(source);
        }

        return transaction;
    }

    @Override
    public void deleteTransaction(Long id) throws BanqueException {
        Transaction transaction = findTransaction(id);
        if (transaction == null) {
            throw new BanqueException(BanqueException.ErrorType.TRANSACTION_NOT_FOUND);
        }

        //Makes the rollback
        double amount = transaction.getAmount();
        transaction.getSource().setBalance(transaction.getSource().getBalance() + amount);
        transaction.getDestination().setBalance(transaction.getDestination().getBalance() - amount);
        transaction.getSource().getTransactions().remove(transaction);
        transaction.getDestination().getTransactions().remove(transaction);
        try {
            em.remove(em.merge(transaction));
        } catch (Exception e) {
            System.out.println("Original Error Message: " + e.getMessage());
            throw new BanqueException(BanqueException.ErrorType.DATABASE_ERROR);
        }
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
}