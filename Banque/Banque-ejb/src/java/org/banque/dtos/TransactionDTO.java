package org.banque.dtos;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author wasser
 */
public class TransactionDTO implements Serializable {

    private Long id;
    private double amount;
    private AccountDTO source;
    private AccountDTO destination;
    private Date dateOfTransaction;

    public TransactionDTO() {
    }

    public TransactionDTO(double amount, AccountDTO source, AccountDTO destination) {
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

    public AccountDTO getDestination() {
        return destination;
    }

    public void setDestination(AccountDTO destination) {
        this.destination = destination;
    }

    public AccountDTO getSource() {
        return source;
    }

    public void setSource(AccountDTO source) {
        this.source = source;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TransactionDTO)) {
            return false;
        }
        TransactionDTO other = (TransactionDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
