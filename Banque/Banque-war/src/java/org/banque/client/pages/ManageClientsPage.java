/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client.pages;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author bjurkovski
 */
public class ManageClientsPage implements WebPage {
    private VerticalPanel panel = new VerticalPanel();
    
    public ManageClientsPage() {
        panel.add(new Label("Manage Clients"));
    }
    
    public Widget getWidget() {
        return panel;
    }
}
