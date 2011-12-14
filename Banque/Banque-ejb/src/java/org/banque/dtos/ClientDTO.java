package org.banque.dtos;

import java.util.Date;

/**
 *
 * @author wasser
 */
public class ClientDTO extends PersonDTO {

    private Date dateOfSubscription;
    private String email;

    public ClientDTO() {
    }

    public ClientDTO(String name, String lastName, String password, Gender gender, Date dateOfBirth, String address, String email) {
        super(name, lastName, password, gender, dateOfBirth, address);
        dateOfSubscription = new Date();
        this.email = email;
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
}