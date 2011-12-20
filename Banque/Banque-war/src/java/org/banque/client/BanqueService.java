/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import org.banque.dtos.ClientDTO;

/**
 *
 * @author bjurkovski
 */
@RemoteServiceRelativePath("banqueservice")
public interface BanqueService extends RemoteService {

    public List<ClientDTO> findAllClients();
    
    public void createClient(ClientDTO client);
    
    public void removeClient(Long clientId);
    
    public List<ClientDTO> findClients(String searchStr);
}
