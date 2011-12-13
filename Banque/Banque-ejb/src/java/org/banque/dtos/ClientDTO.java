package org.banque.dtos;

import java.util.Date;

/**
 *
 * @author wasser
 */
public class ClientDTO extends PersonDTO {

    private Date dateOfSubscription;
    //private List<AccountDTO> accounts;
    private String email;

    public ClientDTO() {
    }

    public ClientDTO(String name, String lastName, String password, Gender gender, Date dateOfBirth, String address, String email) {
        super(name, lastName, password, gender, dateOfBirth, address);
        dateOfSubscription = new Date();
        //accounts = new LinkedList<AccountDTO>();
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public List<AccountDTO> getAccounts() {
//        return accounts;
//    }
//
//    public void addAccount(AccountDTO account) {
//        if (accounts == null) {
//            accounts = new LinkedList<AccountDTO>();
//        }
//        accounts.add(account);
//    }
    public Date getDateOfSubscription() {
        return dateOfSubscription;
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
        if (!(object instanceof ClientDTO)) {
            return false;
        }
        ClientDTO other = (ClientDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ClientDTO [ id=" + id + " ]";
    }
}