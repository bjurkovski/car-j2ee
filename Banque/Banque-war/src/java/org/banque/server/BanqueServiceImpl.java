/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.Date;
import org.banque.client.BanqueService;
import org.banque.entities.Person;
import org.banque.exceptions.BanqueException;
import org.banque.managers.PersonManager;

/**
 *
 * @author bjurkovski
 */
public class BanqueServiceImpl extends RemoteServiceServlet implements BanqueService {

    public String myMethod(String s) {
        // Do something interesting with 's' here on the server.
//        PersonManager pm = new PersonManager();
//        Person p = null;
//        try {
//            p = pm.createPerson("sasas", "sasa", "asas", null, Person.Gender.MALE, "sadsad");
//        }
//        catch(BanqueException e) {
//        }
        
        return "Server says: " + s + " ";
    }
}
