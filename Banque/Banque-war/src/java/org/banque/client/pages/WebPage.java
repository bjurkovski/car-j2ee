/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client.pages;

import com.google.gwt.user.client.ui.Widget;
import org.banque.client.SessionManager;

/**
 *
 * @author bjurkovski
 */
public abstract class WebPage {
    protected static SessionManager sessionManager = new SessionManager();
    
    public abstract void setupPage();
    
    public abstract Widget getWidget();
}
