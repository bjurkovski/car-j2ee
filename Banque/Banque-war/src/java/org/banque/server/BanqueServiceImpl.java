/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.banque.client.BanqueService;

/**
 *
 * @author bjurkovski
 */
public class BanqueServiceImpl extends RemoteServiceServlet implements BanqueService {

    public String myMethod(String s) {
        // Do something interesting with 's' here on the server.
        return "Server says: " + s;
    }
}
