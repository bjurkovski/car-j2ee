/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client.pages.clients;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Iterator;
import java.util.List;
import org.banque.client.BanqueService;
import org.banque.client.BanqueServiceAsync;
import org.banque.client.pages.WebPage;
import org.banque.dtos.ClientDTO;

/**
 *
 * @author bjurkovski
 */
public class SearchClientsPage implements WebPage {
    private VerticalPanel vPanel;
    private Label pageTitle;
    private TextBox searchBox;
    private Button searchButton;
    private Button listAllButton;
    private VerticalPanel resultsPanel;
    private AsyncCallback<List<ClientDTO>> updateSearchResultsCallback;
    
    public SearchClientsPage() {
        vPanel = new VerticalPanel();
        pageTitle = new Label("Search Clients");
        pageTitle.setStyleName("title");
        searchBox = new TextBox();
        searchButton = new Button("Search");
        listAllButton = new Button("List All");
        resultsPanel = new VerticalPanel();
        
        final HorizontalPanel searchPanel = new HorizontalPanel();
        searchPanel.add(searchBox);
        searchPanel.add(searchButton);
        searchPanel.add(listAllButton);
        
        vPanel.add(pageTitle);
        vPanel.add(searchPanel);
        vPanel.add(resultsPanel);
        
        searchButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                search();
            }
        });
        
        listAllButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                findAll();
            }
        });
        
        updateSearchResultsCallback = new AsyncCallback<List<ClientDTO>>() {
            @Override
            public void onFailure(Throwable caught) {
                resultsPanel.add(new Label("A problem ocurred."));
            }

            @Override
            public void onSuccess(List<ClientDTO> result) {
                for(Iterator<ClientDTO> i = result.iterator(); i.hasNext(); ) {
                    ClientDTO c = i.next();
                    resultsPanel.add(new Label(c.getName() + " " + c.getLastName()));
                }
            }
        };
    }
    
    private void search() {
        resultsPanel.clear();
        resultsPanel.add(new Label("Search Results:"));
        getService().findAllClients(updateSearchResultsCallback);
    }
    
    private void findAll() {
        resultsPanel.clear();
        resultsPanel.add(new Label("All Clients:"));
        getService().findAllClients(updateSearchResultsCallback);
    }
    
    public static BanqueServiceAsync getService() {
        return GWT.create(BanqueService.class);
    }
    
    @Override
    public Widget getWidget() {
        return vPanel;
    }
    
}
