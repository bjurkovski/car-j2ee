/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.dtos;

import java.io.Serializable;

/**
 *
 * @author wasser
 */
public class AccountDTO implements Serializable {

    private Long id;
    private double balance;
    private ClientDTO owner;
//    private List<Transaction> transactions;
    private boolean alertWhenNegative;

    public AccountDTO() {
    }

    public AccountDTO(ClientDTO owner) {
        this(owner, false);
    }

    public AccountDTO(ClientDTO owner, boolean alertWhenNegative) {
        this.owner = owner;
        this.balance = 0;
        //transactions = new LinkedList<Transaction>();
        this.alertWhenNegative = alertWhenNegative;
    }

    public ClientDTO getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

//    public List<Transaction> getTransactions() {
//        return transactions;
//    }
//
//    public void setTransactions(List<Transaction> transactions) {
//        this.transactions = transactions;
//    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
