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
    private boolean alertWhenNegative;

    public AccountDTO() {
    }

    public AccountDTO(ClientDTO owner) {
        this(owner, false);
    }

    public AccountDTO(ClientDTO owner, boolean alertWhenNegative) {
        this.owner = owner;
        this.balance = 0;
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

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccountDTO)) {
            return false;
        }
        AccountDTO other = (AccountDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
