/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client.pages.transaction;

import org.banque.client.pages.clients.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Iterator;
import java.util.List;
import org.banque.client.BanqueService;
import org.banque.client.BanqueServiceAsync;
import org.banque.client.SessionManager;
import org.banque.client.pages.WebPage;
import org.banque.dtos.ClientDTO;
import org.banque.dtos.TransactionDTO;
import org.banque.managers.ClientManager;
import org.banque.managers.interfaces.IAccountManagerLocal;
import org.banque.managers.interfaces.IClientManagerLocal;

/**
 *
 * @author bjurkovski
 */
public class TransactionsLogPage extends WebPage {
    private VerticalPanel vPanel;
    private Label pageTitle;
    private TextBox searchBox;
    private ListBox categoriesList;
    private Button searchButton;
    private Button listAllButton;
    private VerticalPanel resultsPanel;
    private AsyncCallback<List<TransactionDTO>> updateTransactionsCallback;
    
    private void search() {
        resultsPanel.clear();
        final Label l = new Label("Transactions:");
        l.setStyleName("title");
        resultsPanel.add(l);
        int selectedCategory = categoriesList.getSelectedIndex();
        int sc = Integer.valueOf(categoriesList.getValue(selectedCategory));
        getService().findTransactionsByCriteria(searchBox.getText(), sc, updateTransactionsCallback);
    }
    
    private void findAll() {
        resultsPanel.clear();
        final Label l = new Label("All Transactions:");
        l.setStyleName("title");
        resultsPanel.add(l);
        getService().findAllTransactions(updateTransactionsCallback);
    }
    
    public static BanqueServiceAsync getService() {
        return GWT.create(BanqueService.class);
    }
    
    public TransactionsLogPage() {
        setupPage();
    }
    
    public void setupPage() {
        vPanel = new VerticalPanel();
        pageTitle = new Label("Transactions Log");
        pageTitle.setStyleName("title");
        searchBox = new TextBox();
        categoriesList = new ListBox();
        searchButton = new Button("Filter");
        listAllButton = new Button("List All");
        resultsPanel = new VerticalPanel();
        
        categoriesList.addItem("Name", String.valueOf(IAccountManagerLocal.PRENOM_CLIENT));
        categoriesList.addItem("Last Name", String.valueOf(IAccountManagerLocal.NOM_CLIENT));
        categoriesList.addItem("ID", String.valueOf(IAccountManagerLocal.ID));
        
        final HorizontalPanel searchPanel = new HorizontalPanel();
        searchPanel.add(searchBox);
        searchPanel.add(categoriesList);
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
        
        updateTransactionsCallback = new AsyncCallback<List<TransactionDTO>>() {
            @Override
            public void onFailure(Throwable caught) {
                resultsPanel.add(new Label("A problem ocurred."));
            }

            @Override
            public void onSuccess(List<TransactionDTO> result) {
                for(TransactionDTO t : result) {
                    String src = t.getSource().getOwner().getEmail();
                    String dst = t.getDestination().getOwner().getEmail();
                    String usr = sessionManager.getUsername();
                    if(sessionManager.isAdmin() || src.equals(usr) || dst.equals(usr)) {
                        resultsPanel.add(new Label(t.getAmount() + "$ from '" + t.getSource() + "' to '" + t.getDestination() + "'"));
                    }
                }
            }
        };
    }
    
    @Override
    public Widget getWidget() {
        findAll();
        return vPanel;
    }
    
}
