/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.banque.client.BanqueService;
import org.banque.dtos.ClientDTO;
import org.banque.entities.Person;
import org.banque.exceptions.BanqueException;
import org.banque.managers.ClientManager;
import org.banque.managers.PersonManager;

/**
 *
 * @author bjurkovski
 */
public class BanqueServiceImpl extends RemoteServiceServlet implements BanqueService {
    @EJB
    ClientManager clientManager;

    @Override
    public List<ClientDTO> findAllClients() {
        try {
            return clientManager.findAllClients();
        } catch (BanqueException ex) {
            return null;
        }
    }

    @Override
    public void createClient(ClientDTO client) {
        try {
            clientManager.createClient(client);
        } catch (BanqueException ex) {
        }
    }
}
