package org.banque.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author wasser
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Client.FIND_ALL, query = "SELECT c FROM Client c"),
    @NamedQuery(name = Client.FIND_BY_NAME, query = "SELECT c FROM Client c WHERE c.name LIKE :name"),
    @NamedQuery(name = Client.FIND_BY_LAST_NAME, query = "SELECT c FROM Client c WHERE c.lastName LIKE :lastName"),
    @NamedQuery(name = Client.FIND_BY_EMAIL, query = "SELECT c FROM Client c WHERE c.email LIKE :email"),
    @NamedQuery(name = Client.FIND_BY_EMAIL_EQUAL, query = "SELECT c FROM Client c WHERE c.email = :email"),
    @NamedQuery(name = Client.FIND_PARTIALLY, query = "SELECT c FROM Client c WHERE c.name LIKE :partial OR c.lastName LIKE :partial"),
    @NamedQuery(name = Client.FIND_BY_SUBSCRIPTION_DATE, query = "SELECT c FROM Client c WHERE c.dateOfSubscription = :subsdate")
})
public class Client extends Person {

    public static final String FIND_ALL = "findAllClients";
    public static final String FIND_BY_NAME = "findByName";
    public static final String FIND_BY_LAST_NAME = "findByLastName";
    public static final String FIND_BY_EMAIL = "findByEmail";
    public static final String FIND_BY_EMAIL_EQUAL = "findByEmailEqual";
    public static final String FIND_PARTIALLY = "findClientsPartially";
    public static final String FIND_BY_SUBSCRIPTION_DATE = "findClientsBySubscriptionDate";
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateOfSubscription;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Account> accounts;
    private String email;
    private boolean admin;

    public Client(String name, String lastName, String password, Gender gender, Date dateOfBirth, String address, String email) {
        this(name, lastName, password, gender, dateOfBirth, address, email, false);
    }

    public Client(String name, String lastName, String password, Gender gender, Date dateOfBirth, String address, String email, boolean admin) {
        super(name, lastName, password, gender, dateOfBirth, address);
        this.dateOfSubscription = new Date();
        this.email = email;
        this.admin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Client() {
        super();
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Date getDateOfSubscription() {
        return dateOfSubscription;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
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
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.banque.entities.Client[ id=" + id + " ]";
    }
}
