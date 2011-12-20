/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.banque.client.BanqueService;
import org.banque.dtos.ClientDTO;
import org.banque.entities.Person;
import org.banque.exceptions.BanqueException;
import org.banque.managers.interfaces.IClientManagerLocal;

/**
 *
 * @author bjurkovski
 */
public class BanqueServiceImpl extends RemoteServiceServlet implements BanqueService {
    @EJB
    IClientManagerLocal clientManager;

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
    
    @Override
    public void removeClient(Long clientId) {
        try {
            clientManager.deleteClient(clientId);
        } catch (BanqueException ex) {
        }
    }
    
    @Override
    public List<ClientDTO> findClients(String searchStr) {
        return null;
    }
}
