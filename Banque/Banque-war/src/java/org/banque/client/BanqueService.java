/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *
 * @author bjurkovski
 */
@RemoteServiceRelativePath("banqueservice")
public interface BanqueService extends RemoteService {

    public String myMethod(String s);
}
