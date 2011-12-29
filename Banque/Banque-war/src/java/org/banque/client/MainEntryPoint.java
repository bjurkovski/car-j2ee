/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import java.util.Date;
import org.banque.client.pages.MainPage;
import org.banque.dtos.ClientDTO;
import org.banque.managers.*;

/**
 * Main entry point.
 *
 * @author bjurkovski
 */
public class MainEntryPoint implements EntryPoint {

    /** 
     * Creates a new instance of MainEntryPoint
     */
    public MainEntryPoint() {
    }

    /** 
     * The entry point method, called automatically by loading a module
     * that declares an implementing class as an entry-point
     */
    public void onModuleLoad() {
        final MainPage page = new MainPage();
        final Label label = new Label("Hello, GWT!!!");
        final Button button = new Button("Click me!");
        
        button.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                label.setVisible(!label.isVisible());
            }
        });
        
        /*
        RootPanel.get().add(button);
        RootPanel.get().add(label);
         */
        
        RootPanel.get().add(page.getWidget());
        
        createAdminAccount();
    }
    
    /*
     * This is just a development method. DO NOT use in production.
     * It checks if the database is empty and then creates a default
     * admin account.
     */
    private void createAdminAccount() {
        ClientDTO admin = new ClientDTO("Root", "Admin", "admin", ClientDTO.Gender.MALE, new Date(), "My Address", "root@admin.com", true);
        getService().createClient(admin, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) { }
            @Override
            public void onSuccess(Void result) { }
        });
    }
    
    private BanqueServiceAsync getService() {
        return GWT.create(BanqueService.class);
    }
}
