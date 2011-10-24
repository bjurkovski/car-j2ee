/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author bjurkovski
 */
public interface BanqueServiceAsync {

    public void myMethod(String s, AsyncCallback<String> callback);
}
