package org.banque.entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * @author wasser
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Account.FIND_ALL, query = "SELECT a FROM Account a"),
    @NamedQuery(name = Account.FIND_NEGATIVE, query = "SELECT a FROM Account a WHERE a.balance < 0"),
    @NamedQuery(name = Account.FIND_POSITIVE, query = "SELECT a FROM Account a WHERE a.balance >= 0")
})
public class Account implements Serializable {

    public static final String FIND_ALL = "findAllAccounts";
    public static final String FIND_NEGATIVE = "findAllNegativeAccounts";
    public static final String FIND_POSITIVE = "findAllPositiveAccounts";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double balance;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Transaction> transactions;
    @ManyToOne
    private Client owner;
    private boolean alertWhenNegative;

    public Account() {
        this(false);
    }

    public Account(boolean alertWhenNegative) {
        this.balance = 0;
        transactions = new LinkedList<Transaction>();
        this.alertWhenNegative = alertWhenNegative;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Long getId() {
        return id;
    }

    public boolean isAlertWhenNegative() {
        return alertWhenNegative;
    }

    public boolean isNegative() {
        return balance < 0;
    }

    public void setAlertWhenNegative(boolean alertWhenNegative) {
        this.alertWhenNegative = alertWhenNegative;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.banque.entities.Account[ id=" + id + " ]";
    }
}
