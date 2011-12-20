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
    public void findAllClients(AsyncCallback<List<ClientDTO>> callback);
    
    public void createClient(ClientDTO client, AsyncCallback<Void> callback);
    
    public void removeClient(Long clientId, AsyncCallback<Void> callback);
    
    public void findClients(String searchStr, AsyncCallback<List<ClientDTO>> callback);
}
