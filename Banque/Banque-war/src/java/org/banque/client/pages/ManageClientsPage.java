/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.banque.client.BanqueService;
import org.banque.client.BanqueServiceAsync;

/**
 *
 * @author bjurkovski
 */
public class ManageClientsPage implements WebPage {
    private VerticalPanel panel = new VerticalPanel();
    private BanqueServiceAsync b = GWT.create(BanqueService.class);
    
    public ManageClientsPage() {
        panel.add(new Label("Manage Clients"));
        
        b.myMethod("uma chamda de teste", new AsyncCallback<String>() {
            public void onSuccess(String result) {
                panel.add(new Label(result));
            }
            
            public void onFailure(Throwable caught) {
                panel.add(new Label("deu caca"));
            }
        });
    }
    
    public Widget getWidget() {
        return panel;
    }
}
