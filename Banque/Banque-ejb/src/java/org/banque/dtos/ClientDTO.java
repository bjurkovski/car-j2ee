package org.banque.dtos;

import java.util.Date;

/**
 *
 * @author wasser
 */
public class ClientDTO extends PersonDTO {

    private Date dateOfSubscription;
    private boolean admin;
    private String email;

    public ClientDTO() {
    }

    public ClientDTO(String name, String lastName, String password, Gender gender, Date dateOfBirth, String address, String email) {
        this(name, lastName, password, gender, dateOfBirth, address, email, false);
    }

    public ClientDTO(String name, String lastName, String password, Gender gender, Date dateOfBirth, String address, String email, boolean admin) {
        super(name, lastName, password, gender, dateOfBirth, address);
        dateOfSubscription = new Date();
        this.email = email;
        this.admin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}