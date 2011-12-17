/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.banque.dtos.ClientDTO;
import java.util.List;

/**
 *
 * @author bjurkovski
 */
public interface BanqueServiceAsync {
    public List<ClientDTO> findAllClients();
    
    public void createClient(ClientDTO client);
}
