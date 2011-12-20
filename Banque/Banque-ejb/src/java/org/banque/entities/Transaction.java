package org.banque.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author wasser
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Transaction.FIND_ALL, query = "SELECT t FROM Transaction t")})
public class Transaction implements Serializable {

    public static final String FIND_ALL = "findAllTransactions";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double amount;
    @OneToOne
    private Account source;
    @OneToOne
    private Account destination;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateOfTransaction;

    public Transaction() {
    }

    public Transaction(double amount, Account source, Account destination) {
        this.amount = amount;
        this.source = source;
        this.destination = destination;
        this.dateOfTransaction = new Date();
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return dateOfTransaction;
    }

    public void setDate(Date date) {
        this.dateOfTransaction = date;
    }

    public Account getDestination() {
        return destination;
    }

    public void setDestination(Account destination) {
        this.destination = destination;
    }

    public Account getSource() {
        return source;
    }

    public void setSource(Account source) {
        this.source = source;
    }

    public Long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Transaction)) {
            return false;
        }
        Transaction other = (Transaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.banque.entities.Transaction[ id=" + id + " ]";
    }
}
